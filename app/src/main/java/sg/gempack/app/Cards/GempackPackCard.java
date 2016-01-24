package sg.gempack.app.Cards;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import sg.gempack.app.Classes.GempackPack;
import sg.gempack.app.R;

/**
 * Created by Yesha on 1/23/2016.
 */
public class GempackPackCard extends CardView {
    TextView remainingAmount, endDate, vendor, collectionPoint;
    Context context;
    ImageView vendor_logo;
    GempackPack gempackPack;

    public GempackPackCard(Context context) {
        super(context);
        init(context);
    }

    public GempackPackCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GempackPackCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater.from(getContext()).inflate(R.layout.card_gempackpack, this, true);
        remainingAmount = (TextView) findViewById(R.id.remaining_amount);
        endDate = (TextView) findViewById(R.id.end_date);
        vendor = (TextView) findViewById(R.id.vendor_name);
        collectionPoint = (TextView) findViewById(R.id.collection_point);
        vendor_logo = (ImageView) findViewById(R.id.vendor_photo);
    }

    public void update(GempackPack gempackPack) {
        this.gempackPack = gempackPack;
        update();
    }

    private void update() {
        //TODO add photo
        int pic;
        switch (gempackPack.getVendorName()) {
            case "Amazon":
                pic = R.drawable.icon_amazon;
                break;
            case "Rakuten":
                pic = R.drawable.icon_rakuten;
                break;
            case "Ebay":
                pic = R.drawable.icon_ebay;
                break;
            case "qoo10":
                pic = R.drawable.icon_qoo10;
                break;
            case "Zalora":
                pic = R.drawable.icon_zalora;
                break;
            default:
                pic = R.drawable.icon_taobao;
                break;
        }
         // where myresource.png is the file
        // extension removed from the String
        //int imageResource = getResources().getIdentifier(uri, null, context.getPackageName());

        Drawable res = getResources().getDrawable(pic);
        vendor_logo.setImageDrawable(res);
        remainingAmount.setText(String.format("%.2f", gempackPack.getRemainingAmount()));
        endDate.setText(gempackPack.getStringForDate(gempackPack.getDeadlineTime()));
        vendor.setText(gempackPack.getVendorName());
        collectionPoint.setText(gempackPack.getCollectionPoint());
    }

    public GempackPack getPack() {
        return this.gempackPack;
    }


}