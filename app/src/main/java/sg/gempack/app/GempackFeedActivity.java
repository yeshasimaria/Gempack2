package sg.gempack.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import sg.gempack.app.Adapters.GempackPackAdapter;
import sg.gempack.app.Classes.GempackPack;

public class GempackFeedActivity extends AppCompatActivity {
    @InjectView(R.id.recyclerview)
    RecyclerView recyclerView;
    @InjectView(R.id.container)
    RelativeLayout container;

    ArrayList<GempackPack> gempackPacks;

    RecyclerView.LayoutManager layoutManager;
    GempackPackAdapter gempackPackAdapter;

    ActionBarDrawerToggle mDrawerToggle;

    private NavigationView mNavigationView;
    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        View v = getLayoutInflater().inflate(R.layout.activity_gempack_feed, container, true);
        setContentView(R.layout.activity_gempack_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.inject(this, v);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,  mDrawerLayout, mToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
        ) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle("Gempacks");
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle("Menu");
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
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
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
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
