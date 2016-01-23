package sg.gempack.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class LogInScreenActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_log_in_screen);


    }



    public static void openLogInActivity(Context context){
        Intent startLogInScreenIntent = new Intent(context, LogInScreenActivity.class);
        context.startActivity(startLogInScreenIntent);
        ((Activity)context).finish();
    }
}
