package sg.gempack.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class UserProfileActivity extends AppCompatActivity {

    Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        toolbar = (Toolbar) findViewById(R.id.gempack_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Profile");


    }


    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);

        return super.onPrepareOptionsMenu(menu);
    }

    public static void openUserProfileActivity(Context context) {
        Intent startUserProfileIntent = new Intent(context, UserProfileActivity.class);
        context.startActivity(startUserProfileIntent);
        ((Activity) context).finish();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_feed) {
            GempackFeedActivity.openGempackFeedActivity(this);
        }
        if (id == R.id.action_addpack) {
            NewGemRequestActivity.openNewGemActivity(this);
        }

        return super.onOptionsItemSelected(item);
    }
}
