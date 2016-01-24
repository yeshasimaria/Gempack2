package sg.gempack.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class AddGemActivity extends AppCompatActivity {

    private EditText productCode, amt, productName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gem);

        amt = (EditText) findViewById(R.id.gemAmt);
        productCode = (EditText) findViewById(R.id.gemCode);
        productName= (EditText) findViewById(R.id.gemName);
    }
}
