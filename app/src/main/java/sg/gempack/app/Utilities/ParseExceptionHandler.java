package sg.gempack.app.Utilities;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.parse.ParseException;

/**
 * Handles Parse Exceptions
 *
 * @Author Wang Xien Dong
 */

public class ParseExceptionHandler {


        public ParseExceptionHandler (Context context){
            mContext = context;
        }

        Context mContext;

        public interface ExceptionCallback{
            void doFirst();
            void retryLastStep();
            void abortLastStep();
            void ranSuccessfully();
            void finallyDo();
        }

        public void processPotentialParseExceptions(ParseException e, String userAction, final ExceptionCallback callback) {

            // "userAction" --> Something went wrong while... INSERT TEXT HERE ...Please try again.
            callback.doFirst();

            if (e == null) {
                callback.ranSuccessfully();
            } else {
                Log.e("ParseException", "Error Code: " + e.getCode() + " Error Message: " + e.getMessage());
                e.printStackTrace();

                switch (e.getCode()) {

                    case (ParseException.CONNECTION_FAILED):

                        if (e.getMessage().equals("bad protocol")) {
                            callback.retryLastStep();
                            break;
                        }

                    case (ParseException.TIMEOUT):


                    case (ParseException.EXCEEDED_QUOTA):


                    default:
                        //TODO: REPORT THIS ERROR

                        new AlertDialog.Builder(mContext)
                                .setTitle("Something Went Wrong")
                                .setMessage("Something went wrong while " + userAction + " . Please try again.\n" + e.getMessage())
                                .setPositiveButton("TRY AGAIN", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        callback.retryLastStep();
                                    }
                                })
                                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        callback.abortLastStep();
                                    }
                                })
                                .setCancelable(false)
                                .create().show();

                        break;

                }
            }

            callback.finallyDo();

        }

}
