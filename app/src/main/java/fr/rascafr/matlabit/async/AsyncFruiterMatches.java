package fr.rascafr.matlabit.async;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.rascafr.matlabit.R;
import fr.rascafr.matlabit.fruiter.CardModel;
import fr.rascafr.matlabit.app.DataStorage;
import fr.rascafr.matlabit.fruiter.CardSimpleAdapter;
import fr.rascafr.matlabit.profile.UserProfile;
import fr.rascafr.matlabit.utils.APIAsyncTask;
import fr.rascafr.matlabit.utils.APIResponse;
import fr.rascafr.matlabit.utils.EncryptUtils;

/**
 * Created by root on 18/04/16.
 */
public class AsyncFruiterMatches extends APIAsyncTask {

    private CardSimpleAdapter adapter;
    private TextView tvMessage;
    private ProgressBar progressFruiter;

    public AsyncFruiterMatches(Context context, String url, CardSimpleAdapter adapter, TextView tvMessage, ProgressBar progressFruiter) {
        super(context, url);
        this.adapter = adapter;
        this.tvMessage = tvMessage;
        this.progressFruiter = progressFruiter;

        // Get profile
        UserProfile profile = DataStorage.getInstance().getUserProfile();

        // Set parameters
        this.pairs.put("client", profile.getLogin());
        this.pairs.put("password", profile.getPassword());
        this.pairs.put("hash", EncryptUtils.sha256(context.getResources().getString(R.string.auth_fruiter_list) + profile.getLogin() + profile.getPassword()));
    }

    @Override
    protected void onPreExecute() {
        progressFruiter.setVisibility(View.VISIBLE);
        tvMessage.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onPostExecute(APIResponse apiResponse) {
        progressFruiter.setVisibility(View.INVISIBLE);

        if (apiResponse.isValid()) {
            try {
                JSONObject obj = apiResponse.getJsonData();
                JSONArray people = obj.getJSONArray("people");

                for (int i = 0; i < people.length(); i++) {

                    // Create card
                    CardModel cm = new CardModel(people.getJSONObject(i));

                    // Add card to stack
                    adapter.add(cm);
                }

                adapter.notifyDataSetChanged();

                if (people.length() == 0) {
                    tvMessage.setVisibility(View.VISIBLE);
                    tvMessage.setText("Plus personne à matcher !\nSi avec ça tu n'as pas une touche ...\nVa vite voir ta liste BDE et tes demandes !");
                }

            } catch (JSONException e) {
                Toast.makeText(context, "Erreur serveur. Veuillez réessayer plus tard ...", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context, apiResponse.getErrorMsg(), Toast.LENGTH_LONG).show();
        }
    }
}
