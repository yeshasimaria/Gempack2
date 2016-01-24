package sg.gempack.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import sg.gempack.app.Classes.GempackPack;

public class NewGemRequestActivity extends AppCompatActivity {
    Context context;
    private EditText meetingPt, amt, benefits, description;
    private DatePicker datePicker;
    private String meet, amount, benefitString, descript, vendor;
    private Spinner spinner;
    private TimePicker timePicker;
    private DateTime date;
    private Button next;

    private GempackPack GMPP;
    //TODO: remove method

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_gem_request);
        context = this.getApplicationContext();
        // Spinner element
         spinner = (Spinner) findViewById(R.id.spinner);
        meetingPt = (EditText) findViewById(R.id.editmeeting);
        amt = (EditText) findViewById(R.id.editamount);
        description = (EditText) findViewById(R.id.editdescription);
        benefits= (EditText) findViewById(R.id.editbenefits);
        datePicker =  (DatePicker) findViewById(R.id.datePicker);

        date = new DateTime();
        //date = new DateTime(1995,01, 01, 23,59);
        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                vendor = parent.getItemAtPosition(position).toString();

                // Showing selected spinner item
                Toast.makeText(parent.getContext(), "Selected: " + vendor, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        // Spinner click listener
        spinner.setOnItemSelectedListener(listener);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Amazon");
        categories.add("Rakuten");
        categories.add("Ebay");
        categories.add("qoo10");
        categories.add("Zalora");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        vendor = "Amazon";
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = new DateTime(datePicker.getYear(),datePicker.getMonth(), datePicker.getDayOfMonth(), 0,0);
            }
        });

        next = (Button) findViewById(R.id.submitBtn);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GMPP = new GempackPack();
                Log.d("NewGem1", meetingPt.getText().toString());
                Log.d("NewGem2", benefits.getText().toString());
                Log.d("NewGem3", amt.getText().toString());
                double price = Double.parseDouble(amt.getText().toString());

                meet = meetingPt.getText().toString();
                benefitString = benefits.getText().toString();
                descript = description.getText().toString();
                GMPP.savePackDetailsParse(NewGemRequestActivity.this, meet, date, price, benefitString, descript, vendor,new GempackPack.OnPackCreatedCallback() {
                    @Override
                    public void createdSuccessfully(GempackPack gempackPack) {
                        AddGemActivity.openAddGemActivity(NewGemRequestActivity.this, GMPP);
                    }

                    @Override
                    public void somethingWentWrong() {
                        //do nothing
                    }
                });
                Intent myIntent = new Intent(context, GempackPack.class);
            }
        });



    }

    public static void openNewGemActivity(Context context){
        Intent startNewGemScreenIntent = new Intent(context, NewGemRequestActivity.class);

        context.startActivity(startNewGemScreenIntent);
        ((Activity)context).finish();
    }

}
