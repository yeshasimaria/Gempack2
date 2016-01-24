package sg.gempack.app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import sg.gempack.app.Cards.GempackPackCard;
import sg.gempack.app.Classes.GempackPack;
import sg.gempack.app.R;
import sg.gempack.app.PackDetailsActivity;

/**
 * Created by Yesha on 1/23/2016.
 */
public class GempackPackAdapter extends RecyclerView.Adapter<GempackPackAdapter.GempackPackViewHolder> implements View.OnClickListener{

        ArrayList<GempackPack> mGempackPackSet;
        Context context;

        public GempackPackAdapter(Context context,ArrayList<GempackPack>GempackPackset){
        this.context=context;
        mGempackPackSet=GempackPackset;
        }

        @Override
        public GempackPackViewHolder onCreateViewHolder(ViewGroup viewGroup,int i){
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_item_gempackpack,viewGroup,false);
        GempackPackViewHolder pvh=new GempackPackViewHolder(v,context);
        return pvh;
        }

        @Override
        public void onBindViewHolder(GempackPackViewHolder gempackPackViewHolder,int i){
        gempackPackViewHolder.cv.update(mGempackPackSet.get(i));
        }

        @Override
        public int getItemCount(){
        if(mGempackPackSet!=null)
        return mGempackPackSet.size();
        else return 0;
        }

public void updateList(ArrayList<GempackPack>newlist){
        mGempackPackSet.clear();
        mGempackPackSet.addAll(newlist);
        this.notifyDataSetChanged();
        }

@Override
public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
        }

@Override
public void onClick(View v){

        }

public static class GempackPackViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    GempackPackCard cv;
    Context ctx;

    GempackPackViewHolder(View itemView, Context context) {
        super(itemView);
        ctx = context;
        cv = (GempackPackCard) itemView.findViewById(R.id.gempackpack_card_item);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(ctx, PackDetailsActivity.class);
        intent.putExtra(GempackPack.getGempackPackCode(), cv.getPack());
        ctx.startActivity(intent);
    }
}
}

