package sg.gempack.app;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import sg.gempack.app.Cards.PackDetailsCard;
import sg.gempack.app.Classes.GempackPack;

public class PackDetailsActivity extends AppCompatActivity {
    GempackPack gempackPack;
    PackDetailsCard packDetailsCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pack_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        gempackPack = getIntent().getParcelableExtra(GempackPack.getGempackPackCode());
        packDetailsCard = (PackDetailsCard) findViewById(R.id.pack_details_card);
        packDetailsCard.update(gempackPack);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
