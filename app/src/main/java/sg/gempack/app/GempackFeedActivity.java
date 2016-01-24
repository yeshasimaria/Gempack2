package sg.gempack.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import sg.gempack.app.Adapters.GempackPackAdapter;
import sg.gempack.app.Classes.GempackPack;

public class GempackFeedActivity extends AppCompatActivity {
    //@InjectView(R.id.recyclerview)
    RecyclerView recyclerView;
    //@InjectView(R.id.container)
    RelativeLayout container;

    ArrayList<GempackPack> gempackPacks;

    RecyclerView.LayoutManager layoutManager;
    GempackPackAdapter gempackPackAdapter;

    ActionBarDrawerToggle mDrawerToggle;

    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gempack_feed);

        toolbar = (Toolbar) findViewById(R.id.gempack_toolbar);
        setSupportActionBar(toolbar);


        container = (RelativeLayout) findViewById(R.id.container);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.gempack_brown_dark));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.gempack_brown_dark));
        }


        //View v = getLayoutInflater().inflate(R.layout.activity_gempack_feed, container, true);

        setup();
        GempackPack.getPacksFromParse(this, new GempackPack.GetPacksCallback() {
            @Override
            public void successfullyGetPacks(ArrayList<GempackPack> listOfGems) {
                setTab1IOUs(listOfGems);
            }

            @Override
            public void somethingWentWrong() {

            }
        });

    }


    public static void openGempackFeedActivity(Context context){
        Intent intent = new Intent(context, GempackFeedActivity.class);
        context.startActivity(intent);
        ((Activity)context).finish();
    }

    private void setup(){
        recyclerView.setLayoutManager(layoutManager = new LinearLayoutManager(this));
    }

    public void setTab1IOUs(ArrayList<GempackPack> ious) {
        this.gempackPacks = ious;
        gempackPackAdapter = new GempackPackAdapter(this, gempackPacks);
        recyclerView.setAdapter(gempackPackAdapter);
    }





    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_gempack_feed, menu);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_addpack) {
            NewGemRequestActivity.openNewGemActivity(this);
        }
        if (id == R.id.action_myprofile) {
            UserProfileActivity.openUserProfileActivity(this);
        }
        if (id == R.id.action_refresh) {
            //TODO
        }





        return super.onOptionsItemSelected(item);
    }



    //Back Press Code

    private boolean backPressedWarned = false;
    @Override
    public void onBackPressed() {
        if (!backPressedWarned) {

            Snackbar.make(findViewById(R.id.gempack_feed_layout), this.getString(R.string.toast_back_press), Snackbar.LENGTH_SHORT)
                    .setAction("QUIT", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            GempackFeedActivity.this.finish();
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
