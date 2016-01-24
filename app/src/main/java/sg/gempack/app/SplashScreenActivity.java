package sg.gempack.app;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class SplashScreenActivity extends AppCompatActivity {

    TextView versionNameText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(sg.gempack.app.R.layout.activity_splash_screen);

        versionNameText = (TextView) findViewById(R.id.splash_version_name);
        versionNameText.setText(BuildConfig.VERSION_NAME);
        Context context = getApplicationContext();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //If the user is logged in
                if (ParseUser.getCurrentUser() != null && ParseFacebookUtils.isLinked(ParseUser.getCurrentUser())){
                    //GempackFeedActivity.openGempackFeedActivity(SplashScreenActivity.this);
                    //TODO change to GempackFeedActivity

                } else {
                    //TODO: revert splash entry
                    //NewGemRequestActivity.openNewGemActivity(SplashScreenActivity.this);
                    LogInScreenActivity.openLogInActivity(SplashScreenActivity.this);
                }



            }
        }, 700);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(sg.gempack.app.R.menu.menu_splash_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == sg.gempack.app.R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
