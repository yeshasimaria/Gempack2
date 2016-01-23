package sg.gempack.app.Classes;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import sg.gempack.app.GempackApplication;
import sg.gempack.app.Utilities.ParseACLHelper;
import sg.gempack.app.Utilities.ParseExceptionHandler;

/**
 * GempackPack Class
 *
 * Status Code for GempackPack:
 * 1. PACK_1 - Alive and active
 * 2. PACK_2 - Completed
 * 3. PACK_3 - Deleted
 *
 * Created by xiend_000 on 23/1/2016.
 */
public class GempackPack implements Parcelable{

    private static final String GEMPACK_PACK = "gempackPack"; //not to be used for ParseUser.


    public static String getGempackPackCode() { return GEMPACK_PACK; }

    //CONSTRUCTOR
    /**
     * Blank Pack Constructor, meant for creating a new one.
     */
    public GempackPack(){
        gempackPackParseObject = new ParseObject(GEMPACK_PACK);
    }

    public static GempackPack constructGempackPack (ParseObject packObject, Boolean skipCache){
        return new GempackPack(packObject);
    }

    private GempackPack(ParseObject packObject){
        this.gempackPackParseObject = packObject;
        this.gempackPackID = packOwnerID;
    }


    ParseObject gempackPackParseObject;
    String gempackPackID;

    String packStatus;

    GempackUser packOwner;
    String packOwnerID;

    String collectionPoint;
    DateTime createdTime;
    DateTime deadlineTime;
    Double requiredAmount;
    Double collectedAmount;
    String benefitsText;
    String descriptionText;

    String vendorName;

    List<GempackGem> gemsInPack = new ArrayList<>();

    private static final String OWNER_PARSE_OBJECT = "owner";
    private static final String COLLECTION_POINT = "collectionPoint";
    private static final String DEADLINE_TIME = "deadlineTime";
    private static final String REQUIRED_AMOUNT = "requiredAmount";
    private static final String COLLECTED_AMOUNT = "collectedAmount";
    private static final String BENEFITS_TEXT = "benefitsText";
    private static final String DESCRIPTIONS_TEXT = "descriptionsText";
    private static final String PACK_STATUS = "packStatus";
    private static final String VENDOR_NAME = "vendor";

    public static String getCOLLECTED_AMOUNT(){ return COLLECTED_AMOUNT;  }


    //Security

    /**
     * Constructs permission for the current parse user Permission
     * @return parseUserPermissions
     */




    public interface OnPackCreatedCallback{
        void createdSuccessfully(GempackPack gempackPack);
        void somethingWentWrong();
    }
    /**
     * Save/updates Pack to Parse
     * @param context
     * @param collectionPoint
     * @param deadlineTime
     * @param requiredAmount
     * @param benefitsText
     * @param descriptionText
     * @param callback
     */
    public void savePackDetailsParse(final Context context, final String collectionPoint, final DateTime deadlineTime, final Double requiredAmount, final String benefitsText, final String descriptionText, final String gempackVendor, final OnPackCreatedCallback callback){

        final Dialog loadingDialog = ProgressDialog.show(context, null, "Creating a Pack...", true);

        if (!gempackPackParseObject.has(OWNER_PARSE_OBJECT)) gempackPackParseObject.put(OWNER_PARSE_OBJECT, ParseUser.getCurrentUser());
        if (deadlineTime != null) gempackPackParseObject.put(DEADLINE_TIME, deadlineTime.getMillis());
        if (collectionPoint != null) gempackPackParseObject.put(COLLECTION_POINT, collectionPoint);
        if (requiredAmount != null) gempackPackParseObject.put(REQUIRED_AMOUNT, requiredAmount);
        if (benefitsText != null) gempackPackParseObject.put(BENEFITS_TEXT, benefitsText);
        if (descriptionText != null) gempackPackParseObject.put(DESCRIPTIONS_TEXT, descriptionText);
        if (gempackVendor != null) gempackPackParseObject.put(VENDOR_NAME, gempackVendor);
        gempackPackParseObject.put(PACK_STATUS, "PACK_1");
        gempackPackParseObject.setACL(ParseACLHelper.setParseObjectPermissions());

        gempackPackParseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                new ParseExceptionHandler(context).processPotentialParseExceptions(e, "creating a pack", new ParseExceptionHandler.ExceptionCallback() {
                    @Override
                    public void doFirst() {
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void retryLastStep() {
                        savePackDetailsParse(context, collectionPoint, deadlineTime, requiredAmount, benefitsText, descriptionText, gempackVendor, callback);
                    }

                    @Override
                    public void abortLastStep() {
                        callback.somethingWentWrong();
                    }

                    @Override
                    public void ranSuccessfully() {

                        gempackPackID = gempackPackParseObject.getObjectId();
                        packOwner = GempackApplication.getMainGempackUser();
                        packOwnerID = ParseUser.getCurrentUser().getObjectId();
                        if (collectionPoint != null) GempackPack.this.collectionPoint = collectionPoint;
                        if (deadlineTime != null) GempackPack.this.deadlineTime = deadlineTime;
                        if (requiredAmount != null) GempackPack.this.requiredAmount = requiredAmount;
                        createdTime = new DateTime(gempackPackParseObject.getCreatedAt());
                        GempackPack.this.collectedAmount = 0.0;
                        if (benefitsText != null) GempackPack.this.benefitsText = benefitsText;
                        if (descriptionText != null) GempackPack.this.descriptionText = descriptionText;
                        if (gempackVendor != null) {
                            GempackPack.this.vendorName = gempackVendor;
                        }


                        packStatus = "PACK_1";

                        callback.createdSuccessfully(GempackPack.this);
                    }

                    @Override
                    public void finallyDo() {

                    }
                });
            }
        });
    }

    public void addCollectedAmountToParse(double amount){
        collectedAmount += amount;
        gempackPackParseObject.put(COLLECTED_AMOUNT, amount);
        gempackPackParseObject.saveInBackground();
    }


    public interface PackDetailsLoadedCallback{
        void loadedSuccessfully(ParseObject packObject);
        void somethingWentWrong();
    }
    public void loadPackDetailsFromParse(final Context context, final boolean forcedRefresh, final String loadText, final PackDetailsLoadedCallback callback){

        final ProgressDialog progressDialog;

        if(loadText!=null && !loadText.equals("")){
            progressDialog = ProgressDialog.show(context, "", loadText, true);
        } else {
            progressDialog = null;
        }

        ParseQuery<ParseObject> query = new ParseQuery<>(GEMPACK_PACK);
        query.getInBackground(gempackPackParseObject.getObjectId(), new GetCallback<ParseObject>() {
            @Override
            public void done(final ParseObject object, ParseException e) {
                new ParseExceptionHandler(context).processPotentialParseExceptions(e, "loading pack details", new ParseExceptionHandler.ExceptionCallback() {
                    @Override
                    public void doFirst() {
                        if (progressDialog != null) progressDialog.dismiss();
                    }

                    @Override
                    public void retryLastStep() {
                        loadPackDetailsFromParse(context, forcedRefresh, loadText, callback);
                    }

                    @Override
                    public void abortLastStep() {
                        callback.somethingWentWrong();
                    }

                    @Override
                    public void ranSuccessfully() {

                        gempackPackParseObject = object;
                        gempackPackID = object.getObjectId();
                        packOwner = GempackUser.constructGempackUser(object.getParseUser(OWNER_PARSE_OBJECT), true);
                        packOwnerID = packOwner.getParseUserID();
                        collectionPoint = object.getString(COLLECTION_POINT);
                        deadlineTime = new DateTime(object.getLong(DEADLINE_TIME));
                        requiredAmount = object.getDouble(REQUIRED_AMOUNT);
                        createdTime = new DateTime(object.getCreatedAt());
                        benefitsText = object.getString(BENEFITS_TEXT);
                        descriptionText = object.getString(DESCRIPTIONS_TEXT);
                        vendorName = object.getString(VENDOR_NAME);
                        packStatus = object.getString(PACK_STATUS);
                        collectedAmount = object.getDouble(COLLECTED_AMOUNT);
                    }

                    @Override
                    public void finallyDo() {

                    }
                });
            }
        });

    }


    public interface GetGemsCallback{
        void successfullyGetGems(List<GempackGem> listOfGems);
        void somethingWentWrong();
    }
    public void getGemsFromParse(final Context context, final GetGemsCallback callback){

        final ParseQuery<ParseObject> query = ParseQuery.getQuery("otterBicycle");
        query.whereEqualTo(GEMPACK_PACK, gempackPackParseObject);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> objects, ParseException e) {
                new ParseExceptionHandler(context).processPotentialParseExceptions(e, "loading gems", new ParseExceptionHandler.ExceptionCallback() {
                    @Override
                    public void doFirst() {

                    }

                    @Override
                    public void retryLastStep() {
                        getGemsFromParse(context, callback);
                    }

                    @Override
                    public void abortLastStep() {
                        callback.somethingWentWrong();
                    }

                    @Override
                    public void ranSuccessfully() {

                        gemsInPack = new ArrayList<>();
                        collectedAmount = 0.0;
                        for (ParseObject parseObject : objects) {
                            GempackGem gem = new GempackGem(parseObject);
                            gemsInPack.add(gem);
                        }

                        callback.successfullyGetGems(gemsInPack);
                    }

                    @Override
                    public void finallyDo() {

                    }
                });
            }
        });
    }


    //Helper Function
    public String getStringForDate (DateTime dateTime){
        DateTimeFormatter formatter = DateTimeFormat.forPattern("d MMMM y");
        return formatter.print(dateTime);
    }


    //Getters
    public String getDescriptionText() {
        return descriptionText;
    }

    public ParseObject getGempackPackParseObject() {
        return gempackPackParseObject;
    }

    public String getGempackPackID() {
        return gempackPackID;
    }

    public String getPackStatus() {
        return packStatus;
    }

    public GempackUser getPackOwner() {
        return packOwner;
    }

    public String getPackOwnerID() {
        return packOwnerID;
    }

    public String getCollectionPoint() {
        return collectionPoint;
    }

    public DateTime getCreatedTime() {
        return createdTime;
    }

    public DateTime getDeadlineTime() {
        return deadlineTime;
    }

    public Double getRequiredAmount() {
        return requiredAmount;
    }

    public Double getCollectedAmount() {
        return collectedAmount;
    }

    public Double getRemainingAmount(){

        Double remainingAmount = requiredAmount - collectedAmount;

        if (remainingAmount < 0.0){
            return 0.0;
        } else {
            return remainingAmount;
        }
    }

    public String getBenefitsText() {
        return benefitsText;
    }


    //Parcelable Code
    protected GempackPack(Parcel in) {
        gempackPackID = in.readString();
        packStatus = in.readString();
        packOwner = in.readParcelable(GempackUser.class.getClassLoader());
        packOwnerID = in.readString();
        collectionPoint = in.readString();
        benefitsText = in.readString();
        descriptionText = in.readString();
        vendorName = in.readString();
        requiredAmount = in.readDouble();
        collectedAmount = in.readDouble();
    }

    public static final Creator<GempackPack> CREATOR = new Creator<GempackPack>() {
        @Override
        public GempackPack createFromParcel(Parcel in) {
            return new GempackPack(in);
        }

        @Override
        public GempackPack[] newArray(int size) {
            return new GempackPack[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(gempackPackID);
        dest.writeString(packStatus);
        dest.writeParcelable(packOwner, flags);
        dest.writeString(packOwnerID);
        dest.writeString(collectionPoint);
        dest.writeString(benefitsText);
        dest.writeString(descriptionText);
        dest.writeString(vendorName);
        dest.writeDouble(requiredAmount);
        dest.writeDouble(collectedAmount);
    }


}
