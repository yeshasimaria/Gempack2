package sg.gempack.app.Classes;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.anupcowkur.reservoir.Reservoir;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import com.parse.ParseException;

import sg.gempack.app.GempackApplication;
import sg.gempack.app.R;
import sg.gempack.app.Utilities.ParseACLHelper;
import sg.gempack.app.Utilities.ParseExceptionHandler;

/**
 * A class to represent each user of Gempack
 *
 * TODO: Allow verification of user.
 *
 *
 * @author Wang Xien Dong
 */
public class GempackUser implements Parcelable {


    private static final String GEMPACK_USER = "gempackUser"; //not to be used for ParseUser.
    public static String getGempackUserCode() {
        return GEMPACK_USER;
    }

    private static final String PARSE_USER = "user"; //to be used for ParseUser.
    public static String getParseUserCode() {
        return GEMPACK_USER;
    }



    //CONSTRUCTORS
    public static GempackUser constructGempackUser(ParseUser parseUser){
        return new GempackUser(parseUser);
    }


    public GempackUser(ParseUser parseUser){
        extractParseUserInformation(parseUser);
    }


    private ParseUser parseUser;
    private String fullName;
    private String emailAddress;
    private String parseUserID;
    private String facebookID;
    private String phoneNumber;
    private Boolean facebookVerified;
    private Bitmap profilePhotoBitmap;

    private static final String FULL_NAME = "fullName";
    private static final String FACEBOOK_ID = "facebookID";
    private static final String PHONE_NUMBER = "phoneNumber";
    private static final String FACEBOOK_VERIFIED = "facebookVerified";
    private static final String EMAIL = "email";


    //Security

    /**
     * Constructs permission for the current parse user Permission
     * @return parseUserPermissions
     */



    public interface getFacebookInfoCallback{
        void onSaveComplete();
        void noNeedToSave();
        void somethingWentWrong();
    }
    /**
     * saveFacebookInfoToParse
     * This method will always be called so to update facebook information to the app.
     */
    public void saveFacebookInfoToParse(final Context context, final getFacebookInfoCallback callback){

        if (ParseUser.getCurrentUser().isNew()){
            final Dialog loadingDialog = ProgressDialog.show(context, null, "Loading...", true);
            GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(final JSONObject jsonObject, GraphResponse graphResponse) {

                            if (graphResponse.getError() != null) {

                                new AlertDialog.Builder(context)
                                        .setTitle("Failed To Get Facebook Information")
                                        .setMessage("Something went wrong when accessing your information from Facebook. \n\n" + graphResponse.getError().getErrorMessage())
                                        .setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                saveFacebookInfoToParse(context, callback);
                                            }
                                        })
                                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                callback.somethingWentWrong();
                                            }
                                        })
                                        .create().show();

                            } /*else if (!jsonObject.has("email")){

                                new AlertDialog.Builder(context)
                                        .setTitle("Missing Email Address")
                                        .setMessage("Gempack requires your email address. Please try again.")
                                        .setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                saveFacebookInfoToParse(context, callback);
                                            }
                                        })
                                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                callback.somethingWentWrong();
                                            }
                                        })
                                        .create().show();

                            } */else {

                                try {

                                    parseUser.put(FULL_NAME, jsonObject.getString("name"));
                                    parseUser.put(FACEBOOK_ID, jsonObject.getString("id"));
                                    if (jsonObject.has("verified")) parseUser.put(FACEBOOK_VERIFIED, jsonObject.getBoolean("verified"));
                                    if (jsonObject.has("email")) parseUser.put(EMAIL, jsonObject.getString("email"));

                                    parseUser.setACL(ParseACLHelper.setParseObjectPermissions());

                                    parseUser.saveInBackground(new SaveCallback() {

                                        @Override
                                        public void done(ParseException e) {

                                            new ParseExceptionHandler(context).processPotentialParseExceptions(e, "get your information from Facebook", new ParseExceptionHandler.ExceptionCallback() {
                                                @Override
                                                public void doFirst() {
                                                    loadingDialog.dismiss();
                                                }

                                                @Override
                                                public void retryLastStep() {
                                                    saveFacebookInfoToParse(context, callback);
                                                }

                                                @Override
                                                public void abortLastStep() {
                                                    callback.somethingWentWrong();
                                                }

                                                @Override
                                                public void ranSuccessfully() {
                                                    try {
                                                        fullName = jsonObject.getString("name");
                                                        facebookID = jsonObject.getString("id");
                                                        if (jsonObject.has("verified")) facebookVerified = jsonObject.getBoolean("verified");
                                                        if (jsonObject.has("email")) emailAddress = jsonObject.getString("email");



                                                    } catch (JSONException e1) {
                                                        e1.printStackTrace();
                                                    }
                                                    callback.onSaveComplete();
                                                }

                                                @Override
                                                public void finallyDo() {
                                                    //do nothing
                                                }
                                            });

                                        }
                                    });


                                } catch (JSONException e) {
                                    e.printStackTrace();

                                    new AlertDialog.Builder(context)
                                            .setTitle("Failed To Get Facebook Information")
                                            .setMessage("Something went wrong when accessing your information from Facebook. \n" + e.getMessage())
                                            .setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    saveFacebookInfoToParse(context, callback);
                                                }
                                            })
                                            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    callback.somethingWentWrong();
                                                }
                                            })
                                            .create().show();
                                }
                            }
                        }
                    }
            );
            request.executeAsync();


        } else {
            callback.noNeedToSave();
        }

    }


    /**
     * extractParseUserInformation
     * This method will extract everything from ParseUser to the current object.
     * @param parseUser
     */
    private void extractParseUserInformation (ParseUser parseUser){
        this.parseUser = parseUser;
        this.parseUserID = parseUser.getObjectId();
        if (parseUser.has(FULL_NAME)) this.fullName = parseUser.getString(FULL_NAME);
        if (parseUser.has(EMAIL)) this.emailAddress = parseUser.getEmail();
        if (parseUser.has(FACEBOOK_ID)) this.facebookID = parseUser.getString(FACEBOOK_ID);
        if (parseUser.has(PHONE_NUMBER)) this.phoneNumber = parseUser.getString(PHONE_NUMBER);
        if (parseUser.has(FACEBOOK_VERIFIED)) this.facebookVerified = parseUser.getBoolean(FACEBOOK_VERIFIED);
    }



    public interface LoadUserInfoCallback{
        void onLoadedSuccessfully();
        void somethingWentWrong();
    }
    public void loadParseUserInformation(final Context context, final LoadUserInfoCallback callback){

        final Dialog loadingDialog = ProgressDialog.show(context, null, "Loading...", true);

        ParseQuery<ParseUser> query = new ParseQuery<>(ParseUser.class);
        query.getInBackground(parseUser.getObjectId(), new GetCallback<ParseUser>() {
            @Override
            public void done(final ParseUser object, ParseException e) {

                new ParseExceptionHandler(context).processPotentialParseExceptions(e, "loading user information", new ParseExceptionHandler.ExceptionCallback() {
                    @Override
                    public void doFirst() {
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void retryLastStep() {
                        loadParseUserInformation(context, callback);
                    }

                    @Override
                    public void abortLastStep() {
                        callback.somethingWentWrong();
                    }

                    @Override
                    public void ranSuccessfully() {
                        extractParseUserInformation(object);
                        callback.onLoadedSuccessfully();
                    }

                    @Override
                    public void finallyDo() {

                    }
                });

            }
        });
    }




    public void savePhoneNumberAndEmail(Context context){
        //TODO
    }

    private int mCancelledProfilePicCount = 0;
    public interface OnProfilePhotoLoadedFromFacebook{
        void onProfilePhotoLoadedFromParse(Bitmap profilePhoto);
        void somethingWentWrong();
    }

    /**
     *  Loads the profile photo of the user, returns it via the interface.
     * @param context
     * @param forcedRefresh true if you want to load a fresh copy of the image.
     * @param progressSpinner that is overlayed on an imageview. null if not implemented.
     * @param callback to be implemented to handle outcomes.
     */
    public void loadProfilePhotoFromFacebook(final Context context, final Boolean forcedRefresh, final ProgressBar progressSpinner, final OnProfilePhotoLoadedFromFacebook callback){

        if (profilePhotoBitmap!=null && !forcedRefresh) {
            callback.onProfilePhotoLoadedFromParse(profilePhotoBitmap);
            return;
        }


        String profilePhotoURL = "https://graph.facebook.com/"+ facebookID +"/picture?type=large";
        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.loadImage(profilePhotoURL, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                if (progressSpinner != null) {
                    progressSpinner.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                if (progressSpinner != null) {
                    progressSpinner.setVisibility(View.GONE);
                }
                try {
                    throw failReason.getCause();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                    Toast.makeText(context, "Failed to load profile photo.", Toast.LENGTH_SHORT).show();
                    callback.somethingWentWrong();
                }
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (progressSpinner != null) {
                    progressSpinner.setVisibility(View.GONE);
                }
                GempackUser.this.profilePhotoBitmap = loadedImage;
                callback.onProfilePhotoLoadedFromParse(loadedImage);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                if (progressSpinner != null) {
                    progressSpinner.setVisibility(View.GONE);
                }

                if (mCancelledProfilePicCount <= 5){
                    mCancelledProfilePicCount ++;
                    loadProfilePhotoFromFacebook(context, forcedRefresh, progressSpinner, callback);
                } else {
                    mCancelledProfilePicCount = 0;
                    Toast.makeText(context, "Profile Photo Loading Cancelled.", Toast.LENGTH_SHORT).show();
                    callback.somethingWentWrong();
                }
            }
        });
    }






    //ALL THE GETTERS
    public ParseUser getParseUser() {
        return parseUser;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getParseUserID() {
        return parseUserID;
    }

    public String getFacebookID() {
        return facebookID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Boolean getFacebookVerified() {
        return facebookVerified;
    }



    //CACHE CODE
    private void saveToCache(String objectID, Context context){
        GempackApplication.getGempackUserLruCache().put(objectID, GempackUser.this);
        Reservoir.putAsync(GEMPACK_USER + objectID, GempackUser.this, null);
        if (ParseUser.getCurrentUser().getObjectId().equals(objectID)){
            PreferenceManager.getDefaultSharedPreferences(context).edit().putString(GEMPACK_USER, new Gson().toJson(GempackUser.this)).apply();
        }
    }

    //PARCELABLE
    protected GempackUser(Parcel in) {
        fullName = in.readString();
        emailAddress = in.readString();
        parseUserID = in.readString();
        facebookID = in.readString();
        phoneNumber = in.readString();
        profilePhotoBitmap = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<GempackUser> CREATOR = new Creator<GempackUser>() {
        @Override
        public GempackUser createFromParcel(Parcel in) {
            return new GempackUser(in);
        }

        @Override
        public GempackUser[] newArray(int size) {
            return new GempackUser[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fullName);
        dest.writeString(emailAddress);
        dest.writeString(parseUserID);
        dest.writeString(facebookID);
        dest.writeString(phoneNumber);
        dest.writeParcelable(profilePhotoBitmap, flags);
    }


}
