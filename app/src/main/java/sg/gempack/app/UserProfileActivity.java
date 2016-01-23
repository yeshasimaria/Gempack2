package sg.gempack.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

    }

    public static void openUserProfileActivity(Context context) {
        Intent startUserProfileIntent = new Intent(context, UserProfileActivity.class);
        context.startActivity(startUserProfileIntent);
        ((Activity) context).finish();
    }
}
