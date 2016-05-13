package fr.rascafr.matlabit.async;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.rascafr.matlabit.ninja.NinjaAdapter;
import fr.rascafr.matlabit.ninja.ScoreItem;
import fr.rascafr.matlabit.utils.APIAsyncTask;
import fr.rascafr.matlabit.utils.APIResponse;

/**
 * Created by root on 14/04/16.
 */
public class AsyncScoresLoad extends APIAsyncTask {

    private ArrayList<ScoreItem> scoreItems;
    private NinjaAdapter adapter;
    private ProgressBar progressNinja;

    public AsyncScoresLoad(Context context, String url, ArrayList<ScoreItem> scoreItems, NinjaAdapter adapter, ProgressBar progressNinja) {
        super(context, url);
        this.scoreItems = scoreItems;
        this.adapter = adapter;
        this.progressNinja = progressNinja;
    }

    @Override
    protected void onPreExecute() {
        scoreItems.clear();
        progressNinja.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(APIResponse apiResponse) {

        progressNinja.setVisibility(View.INVISIBLE);

        if (apiResponse.isValid()) {
            try {
                JSONObject obj = apiResponse.getJsonData();
                JSONArray scores = obj.getJSONArray("scores");

                scoreItems.add(new ScoreItem("Aide MATLABit à voler les bananes de JB et de son équipe !\nTape sur Jouer et récolte tous les fruits pour nous, c'est cool, merci, le 1er gagne sa fierté", false));

                for (int i=0;i<scores.length();i++) {

                    if (i==0) scoreItems.add(new ScoreItem("Podium", true));
                    if (i==3) scoreItems.add(new ScoreItem("Ceux à la traîne", true));

                    scoreItems.add(new ScoreItem(scores.getJSONObject(i)));
                }

                adapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, apiResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
        }
    }
}
