package sg.gempack.app.Classes;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import sg.gempack.app.GempackApplication;
import sg.gempack.app.Utilities.ParseACLHelper;
import sg.gempack.app.Utilities.ParseExceptionHandler;

/**
 * Created by xiend_000 on 23/1/2016.
 */
public class GempackGem {

    private static final String GEMPACK_GEM = "gempackGem";
    public static String getGempackGem() { return GEMPACK_GEM; }



    ParseObject gemParseObject;
    String gemParseID;

    String productName;
    String productCode;
    Double productPrice;

    GempackPack rootPack;
    String packID;

    GempackUser gemOwner;
    String ownerID;

    private static final String PRODUCT_NAME = "productName";
    private static final String PRODUCT_CODE = "productCode";
    private static final String PRODUCT_PRICE = "productPrice";
    private static final String ROOT_PACK = "rootPack";
    private static final String PRODUCT_OWNER = "productOwner";

    public GempackGem(){
        gemParseObject = ParseObject.create(GEMPACK_GEM);
    }
    public GempackGem(ParseObject parseObject) {
        gemParseObject = parseObject;
        gemParseID = parseObject.getObjectId();

        gemOwner = GempackUser.constructGempackUser(parseObject.getParseUser(PRODUCT_OWNER));
        ownerID = gemOwner.getParseUserID();

        productName = parseObject.getString(PRODUCT_NAME);
        productCode = parseObject.getString(PRODUCT_CODE);
        productPrice = parseObject.getDouble(PRODUCT_PRICE);

        rootPack = GempackPack.constructGempackPack(parseObject.getParseObject(ROOT_PACK));
        packID = rootPack.getGempackPackID();

        gemOwner = new GempackUser(parseObject.getParseUser(PRODUCT_OWNER));
        ownerID = gemOwner.getParseUserID();
    }




    public interface SaveGemToParseCallback{
        void successfullySaved(GempackGem gem);
        void somethingWentWrong();
    }
    public void saveGemToParse(final Context context, final String name, final String code, final Double price, final GempackPack pack, final SaveGemToParseCallback callback){

        final Dialog loadingDialog = ProgressDialog.show(context, null, "Creating a Gem...", true);

        if(name!=null) gemParseObject.put(PRODUCT_NAME, name);
        if(code!=null) gemParseObject.put(PRODUCT_CODE, code);
        if(price!=null) gemParseObject.put(PRODUCT_PRICE, price);
        if(pack!=null) gemParseObject.put(ROOT_PACK, pack);
        if (!gemParseObject.has(PRODUCT_OWNER)) gemParseObject.put(PRODUCT_OWNER, ParseUser.getCurrentUser());

        gemParseObject.setACL(ParseACLHelper.setParseObjectPermissions());

        gemParseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                new ParseExceptionHandler(context).processPotentialParseExceptions(e, "creating a pack", new ParseExceptionHandler.ExceptionCallback() {
                    @Override
                    public void doFirst() {
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void retryLastStep() {
                        saveGemToParse(context, name, code, price, pack, callback);
                    }

                    @Override
                    public void abortLastStep() {
                        callback.somethingWentWrong();
                    }

                    @Override
                    public void ranSuccessfully() {

                        gemParseID = gemParseObject.getObjectId();
                        if(name!=null) productName = name;
                        if(code!=null) productCode = code;
                        if(pack!=null){
                            rootPack = pack;
                            packID = pack.getGempackPackID();
                        }
                        if(price!=null) {
                            rootPack.addCollectedAmountToParse(price);
                            productPrice = price;
                        }
                        gemOwner = GempackApplication.getMainGempackUser();
                        callback.successfullySaved(GempackGem.this);
                    }

                    @Override
                    public void finallyDo() {

                    }
                });
            }
        });

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

        ParseQuery<ParseObject> query = new ParseQuery<>(GEMPACK_GEM);
        query.getInBackground(gemParseObject.getObjectId(), new GetCallback<ParseObject>() {
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

                        gemParseObject = object;
                        gemParseID = object.getObjectId();
                        gemOwner = GempackUser.constructGempackUser(object.getParseUser(PRODUCT_OWNER));
                        ownerID = gemOwner.getParseUserID();

                        productName = object.getString(PRODUCT_NAME);
                        productCode = object.getString(PRODUCT_CODE);
                        productPrice = object.getDouble(PRODUCT_PRICE);

                        rootPack = GempackPack.constructGempackPack(object.getParseObject(ROOT_PACK));
                        packID = rootPack.getGempackPackID();

                        gemOwner = new GempackUser(object.getParseUser(PRODUCT_OWNER));
                        ownerID = gemOwner.getParseUserID();

                    }

                    @Override
                    public void finallyDo() {

                    }
                });
            }
        });

    }




    public ParseObject getGemParseObject() {
        return gemParseObject;
    }

    public String getGemParseID() {
        return gemParseID;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public GempackPack getRootPack() {
        return rootPack;
    }

    public String getPackID() {
        return packID;
    }

    public GempackUser getGemOwner() {
        return gemOwner;
    }

    public String getOwnerID() {
        return ownerID;
    }
}
