package sg.gempack.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseUser;

import sg.gempack.app.Classes.GempackUser;

public class UserProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    GempackUser mainUser;

    ImageView profilePhotoView;
    TextView profileName;
    EditText emailEdit, phoneEdit;

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        toolbar = (Toolbar) findViewById(R.id.gempack_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Profile");

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.gempack_brown_dark));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.gempack_brown_dark));

        }

        profilePhotoView = (ImageView) findViewById(R.id.profile_picture);
        profileName = (TextView) findViewById(R.id.profile_name);
        emailEdit = (EditText) findViewById(R.id.profile_email_edittext);
        phoneEdit = (EditText) findViewById(R.id.profile_phone_edittext);

        mainUser = GempackUser.constructGempackUser(ParseUser.getCurrentUser());
        mainUser.loadParseUserInformation(this, new GempackUser.LoadUserInfoCallback() {
            @Override
            public void onLoadedSuccessfully() {
                profileName.setText(mainUser.getFullName());
                emailEdit.setText(mainUser.getEmailAddress());
                phoneEdit.setText(mainUser.getPhoneNumber());
                mainUser.loadProfilePhotoFromFacebook(UserProfileActivity.this, false, null, new GempackUser.OnProfilePhotoLoadedFromFacebook() {
                    @Override
                    public void onProfilePhotoLoadedFromParse(Bitmap profilePhoto) {
                        profilePhotoView.setImageBitmap(profilePhoto);
                    }

                    @Override
                    public void somethingWentWrong() {
                        profilePhotoView.setBackgroundColor(getResources().getColor(R.color.dark_grey));
                    }
                });
            }

            @Override
            public void somethingWentWrong() {
                finish();
            }
        });

    }


    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);

        return super.onPrepareOptionsMenu(menu);
    }

    public static void openUserProfileActivity(Context context) {
        Intent startUserProfileIntent = new Intent(context, UserProfileActivity.class);
        context.startActivity(startUserProfileIntent);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_feed) {
            GempackFeedActivity.openGempackFeedActivity(this);
        }
        if (id == R.id.action_addpack) {
            NewGemRequestActivity.openNewGemActivity(this);
        }
        if (id == R.id.action_logout) {
            LogInScreenActivity.logOutFunction(this);
        }

        return super.onOptionsItemSelected(item);
    }
}
