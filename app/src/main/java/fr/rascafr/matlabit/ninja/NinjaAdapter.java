package fr.rascafr.matlabit.ninja;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import fr.rascafr.matlabit.R;

/**
 * Created by root on 14/04/16.
 */
public class NinjaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_TOP = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_HEADER = 2;
    private static final int TYPE_LEGEND = 3;

    // Objects
    private Context context;
    private ArrayList<ScoreItem> scoreItems;

    public NinjaAdapter(Context context, ArrayList<ScoreItem> scoreItems) {
        this.context = context;
        this.scoreItems = scoreItems;
    }

    @Override
    public int getItemViewType(int position) {
        ScoreItem si = scoreItems.get(position);
        if (si.isHeader())
            return TYPE_HEADER;
        else if (si.isLegend())
            return TYPE_LEGEND;
        else if (si.getRank() >= 1 && si.getRank() <= 3)
            return TYPE_TOP;
        else
            return TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_TOP)
            return new ScoreItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scores_prez, parent, false));
        else if (viewType == TYPE_HEADER)
            return new SimpleItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.header_legend_empty, parent, false));
        else if (viewType == TYPE_LEGEND)
            return new SimpleItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_legend_empty, parent, false));
        else
            return new ScoreItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scores, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ScoreItem si = scoreItems.get(position);

        if (getItemViewType(position) == TYPE_ITEM || getItemViewType(position) == TYPE_TOP) {
            ScoreItemViewHolder sivh = (ScoreItemViewHolder) holder;
            sivh.tvRank.setText(String.valueOf(si.getRank()));
            sivh.tvLogin.setText(si.getLogin());
            sivh.tvScore.setText(String.valueOf(si.getScore()) + " point" + (si.getScore() > 1 ? "s" : "") + " - " + si.getOs());

            switch (si.getRank()) {
                case 1:
                    sivh.vwSymb.setBackgroundResource(R.drawable.star_rank_1);
                    break;
                case 2:
                    sivh.vwSymb.setBackgroundResource(R.drawable.star_rank_2);
                    break;
                case 3:
                    sivh.vwSymb.setBackgroundResource(R.drawable.star_rank_3);
                    break;
                default:
                    break;
            }
        } else {
            SimpleItemViewHolder sivh = (SimpleItemViewHolder) holder;
            sivh.tvName.setText(si.getLogin());
        }
    }

    @Override
    public int getItemCount() {
        return scoreItems == null ? 0 : scoreItems.size();
    }

    // Classic view holder for history item
    private class ScoreItemViewHolder extends RecyclerView.ViewHolder {

        protected TextView tvLogin, tvScore, tvRank;
        protected View vwSymb;

        public ScoreItemViewHolder(View v) {
            super(v);

            tvLogin = (TextView) v.findViewById(R.id.tvScoreLogin);
            tvScore = (TextView) v.findViewById(R.id.tvScoreValue);
            tvRank = (TextView) v.findViewById(R.id.tvScoreRank);
            vwSymb = v.findViewById(R.id.vwScoreSymb);
        }
    }

    // Classic view holder for simple item (header / legend)
    private class SimpleItemViewHolder extends RecyclerView.ViewHolder {

        protected TextView tvName;

        public SimpleItemViewHolder(View v) {
            super(v);
            this.tvName = (TextView) v.findViewById(R.id.tvSimpleName);
        }
    }
}
