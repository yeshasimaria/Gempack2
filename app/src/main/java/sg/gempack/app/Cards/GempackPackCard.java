package sg.gempack.app.Cards;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import sg.gempack.app.Classes.GempackPack;
import sg.gempack.app.R;

/**
 * Created by Yesha on 1/23/2016.
 */
public class GempackPackCard extends CardView {

    Context context;
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
    }

    public void update(GempackPack gempackPack) {
        this.gempackPack = gempackPack;
        update();
    }

    private void update() {

    }


}