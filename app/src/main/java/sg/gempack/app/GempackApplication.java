package sg.gempack.app;

/**
 * Created by xiend_000 on 23/1/2016.
 */

import android.preference.PreferenceManager;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import android.util.LruCache;
import android.widget.Toast;

import com.anupcowkur.reservoir.Reservoir;
import com.nostra13.universalimageloader.cache.disc.impl.LimitedAgeDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.joda.time.DateTime;
import org.joda.time.Period;

import sg.gempack.app.Classes.GempackGem;
import sg.gempack.app.Classes.GempackPack;
import sg.gempack.app.Classes.GempackUser;

public class GempackApplication extends MultiDexApplication {




    private static LruCache<String, GempackUser> gempackUserLruCache;
    private static LruCache<String, GempackPack> gempackPackLruCache;
    private static LruCache<String, GempackGem> gempackGemLruCache;

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, getResources().getString(R.string.parse_app_id), getResources().getString(R.string.parse_client_key));
        ParseUser.enableRevocableSessionInBackground();
        ParseFacebookUtils.initialize(getApplicationContext());

        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });



        //ALL ABOUT CACHING

        //LRU CACHE
        int lruCacheSizeBig = 256;
        int lruCacheSizeSmall = 64;
        gempackUserLruCache = new LruCache<>(lruCacheSizeSmall); //Store with otterParseUserID / Object ID, except main Otter User
        gempackPackLruCache = new LruCache<>(lruCacheSizeBig);
        gempackGemLruCache = new LruCache<>(lruCacheSizeBig);

        //DISK LRU CACHE with RESERVOIR
        try {

            DateTime diskCacheTime = new DateTime(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getLong("DISK_CACHE_DATE", 0));
            Reservoir.init(this, 409600); //in bytes

            if (new Period(diskCacheTime, new DateTime()).getMillis() > new Period().plusMinutes(30).getMillis()){
                Reservoir.clear();
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                        .remove(GempackUser.getGempackUserCode())
                        .putLong("DISK_CACHE_DATE", new DateTime().getMillis()).apply();
            }


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Something went wrong: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        //ANDROID UNIVERSAL IMAGE LOADER - Deals with Getting, Loading, Caching of Images.
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(defaultOptions)
                .diskCache(new LimitedAgeDiscCache(getApplicationContext().getFilesDir(), 172800))
                .build();

        ImageLoader.getInstance().init(config);
    }


    //ALL THE MAIN GEMPACKUSER CODE.
    private static GempackUser mainGempackUser;

    /**
     * Clears the pointer to main Gempack User
     */
    public static void clearMainGempackUser(){
        mainGempackUser = null;
    }

    /**
     * Gets the main Gempack user for the app.
     * @return mainGempackUser
     */
    public static GempackUser getMainGempackUser(){
        if (mainGempackUser == null){
            mainGempackUser = new GempackUser(ParseUser.getCurrentUser());
        }
        return mainGempackUser;
    }
}
