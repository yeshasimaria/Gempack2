package sg.gempack.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class GempackFeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gempack_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    public static void openGempackFeedActivity(Context context){
        Intent intent = new Intent(context, GempackFeedActivity.class);
        context.startActivity(intent);
        ((Activity)context).finish();
    }

    //Back Press Code
    /*
    TODO: Implement this.
    private boolean backPressedWarned = false;
    @Override
    public void onBackPressed() {
        if (!backPressedWarned) {

            Snackbar.make(findViewById(R.id.log_in_layout), this.getString(R.string.toast_back_press), Snackbar.LENGTH_SHORT)
                    .setAction("QUIT", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            GempackFeedActivity.this.finish();
                            System.exit(0);
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    })
                    .setActionTextColor(getResources().getColor(R.color.gempack_orange))
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

    }*/

}
