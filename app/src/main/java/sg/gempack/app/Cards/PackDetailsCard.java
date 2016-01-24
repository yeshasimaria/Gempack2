package sg.gempack.app.Cards;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ProgressBar;
import android.widget.TextView;

import sg.gempack.app.Classes.GempackPack;
import sg.gempack.app.R;

/**
 * Created by Yesha on 1/24/2016.
 */
public class PackDetailsCard extends CardView {

    TextView remainingAmount, endDate, vendor, collectionPoint, description, benefits;
    ProgressBar progressBar;
    Context context;
    GempackPack gempackPack;

    public PackDetailsCard(Context context) {
        super(context);
        init(context);
    }

    public PackDetailsCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PackDetailsCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater.from(getContext()).inflate(R.layout.card_packdetails, this, true);
        remainingAmount = (TextView) findViewById(R.id.remaining_amount);
        endDate = (TextView) findViewById(R.id.end_date);
        vendor = (TextView) findViewById(R.id.vendor_name);
        collectionPoint = (TextView) findViewById(R.id.collection_point);
        description = (TextView) findViewById(R.id.description);
        benefits = (TextView) findViewById(R.id.benefits);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
    }

    public void update(GempackPack gempackPack) {
        this.gempackPack = gempackPack;
        update();
    }

    private void update() {
        //TODO add photo
        vendor.setText(gempackPack.getVendorName());
        remainingAmount.setText(String.format("%.2f", gempackPack.getRemainingAmount()));
        endDate.setText(gempackPack.getStringForDate(gempackPack.getDeadlineTime()));
        collectionPoint.setText(gempackPack.getCollectionPoint());
        description.setText(gempackPack.getDescriptionText());
        benefits.setText(gempackPack.getBenefitsText());
        Log.d("PROGRESS", String.valueOf(Math.round(gempackPack.getCollectedAmount()/gempackPack.getRequiredAmount()) * 100));
        progressBar.setProgress((int) (Math.round(gempackPack.getCollectedAmount()/gempackPack.getRequiredAmount() * 100)));
    }

    public GempackPack getPack() {
        return this.gempackPack;
    }
}
