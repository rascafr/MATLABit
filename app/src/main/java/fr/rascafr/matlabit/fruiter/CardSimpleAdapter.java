package fr.rascafr.matlabit.fruiter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

import fr.rascafr.matlabit.R;

/**
 * Created by root on 18/04/16.
 */
public class CardSimpleAdapter extends BaseAdapter {

    private ArrayList<CardModel> cardModels;
    private Context context;
    private CropSquareTransformation transformation;

    public class CropSquareTransformation implements Transformation {
        @Override public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
            if (result != source) {
                source.recycle();
            }
            return result;
        }

        @Override public String key() { return "square()"; }
    }

    public CardSimpleAdapter(Context context, ArrayList<CardModel> cardModels) {
        this.context = context;
        this.cardModels = cardModels;
        this.transformation = new CropSquareTransformation();
    }

    @Override
    public int getCount() {
        return cardModels == null ? 0 : cardModels.size();
    }

    @Override
    public CardModel getItem(int position) {
        return cardModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        CardViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new CardViewHolder();

            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.std_card_inner, parent, false);

            viewHolder.tvName = (TextView) convertView.findViewById(R.id.title);
            viewHolder.imgUser = (ImageView) convertView.findViewById(R.id.image);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CardViewHolder) convertView.getTag();
        }

        CardModel cm = cardModels.get(position);

        viewHolder.tvName.setText(cm.getTitle());

        if (cm.getImgUri() == null || cm.getImgUri().length() == 0)
            viewHolder.imgUser.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_no_user));
        else
            Picasso.with(context).load(cm.getImgUri()).transform(transformation).into(viewHolder.imgUser);

        return convertView;
    }

    static class CardViewHolder {
        protected TextView tvName;
        protected ImageView imgUser;
    }

    public void add (CardModel cm) {
        this.cardModels.add(cm);
    }
}
