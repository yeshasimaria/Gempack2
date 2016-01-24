package sg.gempack.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import sg.gempack.app.Classes.GempackGem;
import sg.gempack.app.Classes.GempackPack;

public class AddGemActivity extends AppCompatActivity {

    private EditText productCode, amt, productName;
    private String productCodeS, amtS, productNameS;
    private Button next;
    private GempackGem GMPG;
    private GempackPack pack;

    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gem);
        toolbar = (Toolbar) findViewById(R.id.gempack_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Add Gem");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.gempack_brown_dark));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.gempack_brown_dark));
        }

        Intent intent = getIntent();
        if (intent.hasExtra(GempackPack.getGempackPackCode())) pack = intent.getParcelableExtra(GempackPack.getGempackPackCode());
        else{
            Toast.makeText(this, "Pack information missing.", Toast.LENGTH_LONG).show();
        }
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        amt = (EditText) findViewById(R.id.gemAmt);
        productCode = (EditText) findViewById(R.id.gemCode);
        productName = (EditText) findViewById(R.id.gemName);
        next = (Button) findViewById(R.id.submitBtn);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GMPG = new GempackGem();
                productNameS = productName.getText().toString();
                productCodeS = productCode.getText().toString();
                double price = Double.parseDouble(amt.getText().toString());
                Log.d("NewGem1", amt.getText().toString());
                GMPG.saveGemToParse(AddGemActivity.this, productNameS, productCodeS, price,
                        pack, new GempackGem.SaveGemToParseCallback() {
                            @Override
                            public void successfullySaved(GempackGem gem) {
                                GempackFeedActivity.openGempackFeedActivity(AddGemActivity.this);
                            }

                            @Override
                            public void somethingWentWrong() {
                        //do nothing
                    }
                });
            }
        });
    }
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_gem_activity, menu);

        return super.onPrepareOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    public static void openAddGemActivity(Context context, GempackPack pack){
        Intent startNewGemScreenIntent = new Intent(context, AddGemActivity.class);
        startNewGemScreenIntent.putExtra(GempackPack.getGempackPackCode(), pack);
        context.startActivity(startNewGemScreenIntent);
    }

}
