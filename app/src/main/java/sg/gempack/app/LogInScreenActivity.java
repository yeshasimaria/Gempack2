package sg.gempack.app;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.List;

import sg.gempack.app.Classes.GempackUser;
import sg.gempack.app.Utilities.PushNotificationManagement;


public class LogInScreenActivity extends AppCompatActivity {


    Button logInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_log_in_screen);

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

        //List of permissions you need form user's facebook account.
        List<String> permissions = Arrays.asList("public_profile", "email");

        //documentation: https://parse.com/docs/android/api/com/parse/LogInCallback.html
        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions, new LogInCallback() {
            @Override
            public void done(final ParseUser currentUser, ParseException e) {

                GempackApplication.getMainGempackUser().saveFacebookInfoToParse(LogInScreenActivity.this, new GempackUser.getFacebookInfoCallback() {
                    @Override
                    public void onSaveComplete() {
                        GempackFeedActivity.openGempackFeedActivity(LogInScreenActivity.this);
                    }

                    @Override
                    public void noNeedToSave() {
                        GempackFeedActivity.openGempackFeedActivity(LogInScreenActivity.this);
                    }

                    @Override
                    public void somethingWentWrong() {
                        //do nothing
                    }
                });

                //TODO: Load main activity
                progressDialog.dismiss();
            }
        });
    }


    public static void logOutFunction (final Context context){
        PushNotificationManagement push = new PushNotificationManagement();
        push.uninstallPushFromParseInstallation(new PushNotificationManagement.onSettingPushInstallationCallback() {
            @Override
            public void onSetupComplete() {
                ParseUser.logOut();
                openLogInActivity(context);
            }

            @Override
            public void somethingWentWrong() {
                Toast.makeText(context, "Failed to log out. Please check your internet connection and try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }





    public static void openLogInActivity(Context context){
        Intent startLogInScreenIntent = new Intent(context, LogInScreenActivity.class);
        context.startActivity(startLogInScreenIntent);
        ((Activity)context).finish();
    }
}
