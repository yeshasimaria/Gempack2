package sg.gempack.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

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

    }

}
