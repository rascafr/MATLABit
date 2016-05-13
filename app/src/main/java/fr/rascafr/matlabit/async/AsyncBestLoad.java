package fr.rascafr.matlabit.async;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.rascafr.matlabit.R;
import fr.rascafr.matlabit.app.DataStorage;
import fr.rascafr.matlabit.fruiter.best.BestAdapter;
import fr.rascafr.matlabit.fruiter.best.BestItem;
import fr.rascafr.matlabit.profile.UserProfile;
import fr.rascafr.matlabit.utils.APIAsyncTask;
import fr.rascafr.matlabit.utils.APIResponse;
import fr.rascafr.matlabit.utils.EncryptUtils;

/**
 * Created by root on 14/04/16.
 */
public class AsyncBestLoad extends APIAsyncTask {

    private ArrayList<BestItem> bestItems;
    private BestAdapter adapter;
    private ProgressBar progressBest;

    public AsyncBestLoad(Context context, String url, ArrayList<BestItem> bestItems, BestAdapter adapter, ProgressBar progressBest) {
        super(context, url);
        this.bestItems = bestItems;
        this.adapter = adapter;
        this.progressBest = progressBest;

        // Get profile
        UserProfile profile = DataStorage.getInstance().getUserProfile();

        // Set parameters
        this.pairs.put("client", profile.getLogin());
        this.pairs.put("password", profile.getPassword());
        this.pairs.put("hash", EncryptUtils.sha256(context.getResources().getString(R.string.auth_best_match) + profile.getLogin() + profile.getPassword()));
    }

    @Override
    protected void onPreExecute() {
        bestItems.clear();
        progressBest.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(APIResponse apiResponse) {

        progressBest.setVisibility(View.INVISIBLE);

        if (apiResponse.isValid()) {
            try {
                JSONObject obj = apiResponse.getJsonData();
                JSONArray peoples = obj.getJSONArray("people");

                for (int i=0;i<peoples.length();i++) {
                    bestItems.add(new BestItem(peoples.getJSONObject(i)));
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
