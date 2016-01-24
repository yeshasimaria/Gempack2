package sg.gempack.app;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.List;

import sg.gempack.app.Classes.GempackUser;
import sg.gempack.app.Utilities.ParseExceptionHandler;
import sg.gempack.app.Utilities.PushNotificationManagement;


public class LogInScreenActivity extends AppCompatActivity {


    Button logInButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_log_in_screen);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.grey_translucent));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.grey_translucent));
        }

        getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.gempack_login_background));

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.transparent));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.transparent));
        }


        logInButton = (Button) findViewById(R.id.log_in_facebook_button);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logInFunction();
            }
        });


    }


    private void logInFunction (){
        final Dialog progressDialog = ProgressDialog.show(this, "", "Logging in...", true);
        Log.d("log in activity", "Started");

        //List of permissions you need form user's facebook account.
        List<String> permissions = Arrays.asList("public_profile", "email");

        //documentation: https://parse.com/docs/android/api/com/parse/LogInCallback.html
        ParseFacebookUtils.logInWithReadPermissionsInBackground(LogInScreenActivity.this, permissions, new LogInCallback() {
            @Override
            public void done(final ParseUser currentUser, ParseException e) {

                progressDialog.dismiss();

                new ParseExceptionHandler(LogInScreenActivity.this).processPotentialParseExceptions(e, "log in", new ParseExceptionHandler.ExceptionCallback() {
                    @Override
                    public void doFirst() {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void retryLastStep() {
                        logInFunction();

                    }

                    @Override
                    public void abortLastStep() {
                        //do nothing
                        progressDialog.dismiss();
                    }

                    @Override
                    public void ranSuccessfully() {

                        final PushNotificationManagement pushNotificationManagement = new PushNotificationManagement();
                        final PushNotificationManagement.onSettingPushInstallationCallback callback = new PushNotificationManagement.onSettingPushInstallationCallback() {
                            @Override
                            public void onSetupComplete() {
                                GempackFeedActivity.openGempackFeedActivity(LogInScreenActivity.this);
                            }

                            @Override
                            public void somethingWentWrong() {
                                //do nothing
                            }
                        };

                        GempackApplication.getMainGempackUser().saveFacebookInfoToParse(LogInScreenActivity.this, new GempackUser.getFacebookInfoCallback() {
                            @Override
                            public void onSaveComplete() {
                                pushNotificationManagement.associateUserToParseInstallation(LogInScreenActivity.this, callback);

                            }

                            @Override
                            public void noNeedToSave() {
                                pushNotificationManagement.associateUserToParseInstallation(LogInScreenActivity.this, callback);
                            }

                            @Override
                            public void somethingWentWrong() {
                                //do nothing
                            }
                        });
                    }

                    @Override
                    public void finallyDo() {
                        //do nothing
                    }
                });
            }
        });
    }


    public static void logOutFunction (final Context context){
        PushNotificationManagement push = new PushNotificationManagement();
        push.uninstallPushFromParseInstallation(context, new PushNotificationManagement.onSettingPushInstallationCallback() {
            @Override
            public void onSetupComplete() {
                ParseUser.logOut();
                openLogInActivity(context);
            }

            @Override
            public void somethingWentWrong() {
                //do nothing.
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    public static void openLogInActivity(Context context){
        Intent startLogInScreenIntent = new Intent(context, LogInScreenActivity.class);
        context.startActivity(startLogInScreenIntent);
        ((Activity)context).finish();
    }



    //Back Press Code
    private boolean backPressedWarned = false;
    @Override
    public void onBackPressed() {
        if (!backPressedWarned) {

            Snackbar.make(findViewById(R.id.log_in_layout), this.getString(R.string.toast_back_press), Snackbar.LENGTH_SHORT)
                    .setAction("QUIT", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LogInScreenActivity.this.finish();
                            System.exit(0);
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    })
                    .setActionTextColor(getResources().getColor(R.color.gempack_brown))
                    .show();
            backPressedWarned = true;

        } else {
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
            backPressedWarned = false;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backPressedWarned = false;
            }
        }, 2000);

    }
}
