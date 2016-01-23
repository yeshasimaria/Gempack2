package sg.gempack.app.Classes;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import sg.gempack.app.GempackApplication;
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

    List<GempackGem> gemsInPack = new ArrayList<>();

    private static final String OWNER_PARSE_OBJECT = "owner";
    private static final String COLLECTION_POINT = "collectionPoint";
    private static final String DEADLINE_TIME = "deadlineTime";
    private static final String REQUIRED_AMOUNT = "requiredAmount";
    private static final String BENEFITS_TEXT = "benefitsText";
    private static final String DESCRIPTIONS_TEXT = "descriptionsText";
    private static final String PACK_STATUS = "packStatus";


    //Security

    /**
     * Constructs permission for the current parse user Permission
     * @return parseUserPermissions
     */
    public ParseACL setParseObjectPermissions(){
        ParseACL parseUserPermissions = new ParseACL(ParseUser.getCurrentUser());
        parseUserPermissions.setPublicReadAccess(true);
        parseUserPermissions.setPublicWriteAccess(false);
        return parseUserPermissions;
    }



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
    public void savePackDetailsParse(final Context context, final String collectionPoint, final DateTime deadlineTime, final Double requiredAmount, final String benefitsText, final String descriptionText, final OnPackCreatedCallback callback){



        final Dialog loadingDialog = ProgressDialog.show(context, null, "Creating a Pack...", true);

        if (!gempackPackParseObject.has(OWNER_PARSE_OBJECT)) gempackPackParseObject.put(OWNER_PARSE_OBJECT, ParseUser.getCurrentUser());
        if (deadlineTime != null) gempackPackParseObject.put(DEADLINE_TIME, deadlineTime.getMillis());
        if (collectionPoint != null) gempackPackParseObject.put(COLLECTION_POINT, collectionPoint);
        if (requiredAmount != null) gempackPackParseObject.put(REQUIRED_AMOUNT, requiredAmount);
        if (benefitsText != null) gempackPackParseObject.put(BENEFITS_TEXT, benefitsText);
        if (descriptionText != null) gempackPackParseObject.put(DESCRIPTIONS_TEXT, descriptionText);
        gempackPackParseObject.put(PACK_STATUS, "PACK_1");
        gempackPackParseObject.setACL(setParseObjectPermissions());

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
                        savePackDetailsParse(context, collectionPoint, deadlineTime, requiredAmount, benefitsText, descriptionText, callback);
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

    public void loadPackDetailsFromParse(){

    }


    public interface GetGemsCallback{
        void successfullyGetGems(List<GempackGem> listOfGems);
        void somethingWentWrong();
    }

    public void getGemsFromParse(Context context, GetGemsCallback callback){
        callback.successfullyGetGems(null);
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
    }


}
