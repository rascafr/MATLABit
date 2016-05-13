package fr.rascafr.matlabit.fruiter.best;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import fr.rascafr.matlabit.R;

/**
 * Created by root on 14/04/16.
 */
public class BestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Objects
    private Context context;
    private ArrayList<BestItem> bestItems;

    public BestAdapter(Context context, ArrayList<BestItem> bestItems) {
        this.context = context;
        this.bestItems = bestItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BestItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_best_match, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        BestItemViewHolder bivh = (BestItemViewHolder) holder;
        BestItem bi = bestItems.get(position);

        bivh.tvName.setText(bi.getName());
        bivh.tvHowMany.setText("dans " + bi.getHowMany() + (bi.getHowMany() > 1 ? " listes" : " liste"));

        if (bi.getImgUrl() == null || bi.getImgUrl().length() == 0)
            bivh.imgUser.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_no_user));
        else
            Picasso.with(context).load(bi.getImgUrl()).into(bivh.imgUser);

        bivh.vwDivider.setVisibility(position == bestItems.size() - 1 ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return bestItems == null ? 0 : bestItems.size();
    }

    // Classic view holder for best item
    private class BestItemViewHolder extends RecyclerView.ViewHolder {

        protected TextView tvName, tvHowMany;
        protected CircleImageView imgUser;
        protected View vwDivider;

        public BestItemViewHolder(View v) {
            super(v);

            tvName = (TextView) v.findViewById(R.id.tvBestName);
            tvHowMany = (TextView) v.findViewById(R.id.tvBestHowMany);
            imgUser = (CircleImageView) v.findViewById(R.id.imgBestUser);
            vwDivider = v.findViewById(R.id.vwDivider);
        }
    }
}
