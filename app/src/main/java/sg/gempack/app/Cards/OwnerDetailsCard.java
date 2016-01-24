package sg.gempack.app.Cards;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import sg.gempack.app.Classes.GempackUser;
import sg.gempack.app.R;

/**
 * Created by Yesha on 1/24/2016.
 */
public class OwnerDetailsCard extends CardView {
    TextView owner_name, owner_email;
    Context context;
    GempackUser gempackUser;

    public OwnerDetailsCard(Context context) {
        super(context);
        init(context);
    }

    public OwnerDetailsCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public OwnerDetailsCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater.from(getContext()).inflate(R.layout.card_ownerdetails, this, true);
        owner_name = (TextView) findViewById(R.id.owner_name);
        owner_email = (TextView) findViewById(R.id.owner_email);
    }

    public void update(GempackUser gempackUser) {
        this.gempackUser = gempackUser;
        update();
    }

    private void update() {
        owner_name.setText("Alex Wong");
        owner_email.setText("alexwonglovezalora@gmail.com");

    }

    public GempackUser getPack() {
        return this.gempackUser;
    }
}
