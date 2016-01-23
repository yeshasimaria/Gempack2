package sg.gempack.app.Utilities;

import android.content.Context;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import sg.gempack.app.Classes.GempackUser;
import sg.gempack.app.GempackApplication;

/**
 * Created by xiend_000 on 23/1/2016.
 */
public class PushNotificationManagement {

    public interface onSettingPushInstallationCallback {
        void onSetupComplete();
        void somethingWentWrong();
    }
    public void associateUserToParseInstallation(Context context, final onSettingPushInstallationCallback callback){
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("user", GempackApplication.getMainGempackUser());
        if (GempackApplication.getMainGempackUser() != null) installation.put(GempackUser.getParseUserCode(), GempackApplication.getMainGempackUser());

        installation.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    callback.onSetupComplete();
                } else {
                    e.printStackTrace();
                    callback.somethingWentWrong();
                }
            }
        });
    }

    public void uninstallPushFromParseInstallation(final onSettingPushInstallationCallback callback){
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("user", ParseUser.createWithoutData(ParseUser.class, "0"));
        installation.put("userPublic", ParseUser.createWithoutData("otterUser", "0"));
        installation.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    callback.onSetupComplete();
                } else {
                    e.printStackTrace();
                    callback.somethingWentWrong();
                }
            }
        });

    }

}
