package fr.rascafr.matlabit.fruiter.list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import fr.rascafr.matlabit.R;
import fr.rascafr.matlabit.app.DataStorage;
import fr.rascafr.matlabit.async.AsyncListDelete;

/**
 * Created by root on 14/04/16.
 */
public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_EMPTY = 2;

    // Objects
    private Context context;
    private ArrayList<ListItem> listItems;

    public ListAdapter(Context context, ArrayList<ListItem> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public int getItemViewType(int position) {
        return listItems.get(position).isHeader() ? TYPE_HEADER : listItems.get(position).isEmpty() ? TYPE_EMPTY : TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER)
            return new ListHeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.header_list_match, parent, false));
        else if (viewType == TYPE_EMPTY)
            return new ListHeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_match_empty, parent, false));
        else
            return new ListItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_match, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final ListItem li = listItems.get(position);

        if (getItemViewType(position) == TYPE_HEADER || getItemViewType(position) == TYPE_EMPTY) {

            ListHeaderViewHolder lhvh = (ListHeaderViewHolder) holder;

            lhvh.tvName.setText(li.getName());

        } else {

            ListItemViewHolder livh = (ListItemViewHolder) holder;

            String title = li.getName() + (li.isMutual() ? "  \uD83D\uDC95" : "");
            livh.tvName.setText(title);
            String details = "";
            if (li.isYours()) {
                if (li.getOthers() == 0) {
                    details = "Personne à part toi ne l'a ajouté ... " + "\uD83D\uDE0F";
                } else if (li.getOthers() == 1) {
                    details = "Une autre personne l'a dans sa liste";
                } else {
                    details = li.getOthers() + " personnes l'ont dans leur liste";
                }
            } else {
                if (li.getOthers() == 0) {
                    details = "T'es tout seul dans sa liste ... " + "\uD83D\uDE0F";
                } else if (li.getOthers() == 1) {
                    details = "T'es dans sa liste avec une autre personne";
                } else {
                    details = "T'es dans sa liste avec " + li.getOthers()  + " autres personnes";
                }
            }

            livh.tvMutualInfo.setText(details);

            if (li.getImgUrl() == null || li.getImgUrl().length() == 0)
                livh.imgUser.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_no_user));
            else
                Picasso.with(context).load(li.getImgUrl()).into(livh.imgUser);

            livh.vwDivider.setVisibility(position == listItems.size() - 1 ? View.INVISIBLE : View.VISIBLE);

            // Listener : tap -> delete ?
            if (li.isYours()) livh.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MaterialDialog.Builder(context)
                            .title("Supprimer la touche ?")
                            .content("Il n'y aura plus aucune affinité entre vous et " + li.getName() + " ...")
                            .negativeText("Annuler")
                            .positiveText("L'amour c'est vache")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    new AsyncListDelete(context, DataStorage.getInstance().getSPL_API_DEL_MATCH(), li.getLogin()).execute();
                                    listItems.remove(position);
                                    notifyItemRemoved(position);
                                }
                            })
                            .show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listItems == null ? 0 : listItems.size();
    }

    // Classic view holder for list item
    private class ListItemViewHolder extends RecyclerView.ViewHolder {

        protected LinearLayout ll;
        protected TextView tvName, tvMutualInfo;
        protected CircleImageView imgUser;
        protected View vwDivider;

        public ListItemViewHolder(View v) {
            super(v);

            ll = (LinearLayout) v.findViewById(R.id.llList);
            tvName = (TextView) v.findViewById(R.id.tvListName);
            tvMutualInfo = (TextView) v.findViewById(R.id.tvListDesc);
            imgUser = (CircleImageView) v.findViewById(R.id.imgListUser);
            vwDivider = v.findViewById(R.id.vwDivider);
        }
    }

    // Classic view holder for header item
    private class ListHeaderViewHolder extends RecyclerView.ViewHolder {

        protected TextView tvName;

        public ListHeaderViewHolder(View v) {
            super(v);

            tvName = (TextView) v.findViewById(R.id.tvListName);
        }
    }
}
