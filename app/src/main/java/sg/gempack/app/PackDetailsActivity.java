package sg.gempack.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import sg.gempack.app.Cards.OwnerDetailsCard;
import sg.gempack.app.Cards.PackDetailsCard;
import sg.gempack.app.Classes.GempackPack;

public class PackDetailsActivity extends AppCompatActivity {
    GempackPack gempackPack;
    PackDetailsCard packDetailsCard;
    OwnerDetailsCard ownerDetailsCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pack_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.gempack_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pack Details");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        gempackPack = getIntent().getParcelableExtra(GempackPack.getGempackPackCode());
        packDetailsCard = (PackDetailsCard) findViewById(R.id.pack_details_card);
        ownerDetailsCard = (OwnerDetailsCard) findViewById(R.id.owner_details_card);
        packDetailsCard.update(gempackPack);
        ownerDetailsCard.update(gempackPack.getPackOwner());

    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_details, menu);

        return super.onPrepareOptionsMenu(menu);
    }
    public static void openPackDetailsActivity(Context context){
        Intent intent = new Intent(context, PackDetailsActivity.class);
        context.startActivity(intent);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == android.R.id.home) {
            finish();
        }
        if (id == R.id.action_refresh) {
            //TODO: Refresh
        }


        return super.onOptionsItemSelected(item);
    }

    public void addGem (View v) {
        Intent i = new Intent(this, AddGemActivity.class);
        startActivity(i);
    }
}
