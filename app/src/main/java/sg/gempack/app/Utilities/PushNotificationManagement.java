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
    public void associateUserToParseInstallation(final Context context, final onSettingPushInstallationCallback callback){
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("user", GempackApplication.getMainGempackUser().getParseUser());
        if (GempackApplication.getMainGempackUser() != null) installation.put(GempackUser.getParseUserCode(), GempackApplication.getMainGempackUser().getParseUser());

        installation.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                new ParseExceptionHandler(context).processPotentialParseExceptions(e, "linking to push notification", new ParseExceptionHandler.ExceptionCallback() {
                    @Override
                    public void doFirst() {
                        //do nothing
                    }

                    @Override
                    public void retryLastStep() {
                        associateUserToParseInstallation(context, callback);
                    }

                    @Override
                    public void abortLastStep() {
                        callback.somethingWentWrong();
                    }

                    @Override
                    public void ranSuccessfully() {
                        callback.onSetupComplete();
                    }

                    @Override
                    public void finallyDo() {
                        //do nothing
                    }
                });
            }
        });
    }

    public void uninstallPushFromParseInstallation(final Context context, final onSettingPushInstallationCallback callback){
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("user", ParseUser.createWithoutData(ParseUser.class, "0"));
        installation.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                new ParseExceptionHandler(context).processPotentialParseExceptions(e, "linking to push notification", new ParseExceptionHandler.ExceptionCallback() {
                    @Override
                    public void doFirst() {
                        //do nothing
                    }

                    @Override
                    public void retryLastStep() {
                        associateUserToParseInstallation(context, callback);
                    }

                    @Override
                    public void abortLastStep() {
                        callback.somethingWentWrong();
                    }

                    @Override
                    public void ranSuccessfully() {
                        callback.onSetupComplete();
                    }

                    @Override
                    public void finallyDo() {
                        //do nothing
                    }
                });
            }
        });

    }

}
