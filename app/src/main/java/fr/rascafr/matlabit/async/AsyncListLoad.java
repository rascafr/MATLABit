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
import fr.rascafr.matlabit.fruiter.list.ListAdapter;
import fr.rascafr.matlabit.fruiter.list.ListItem;
import fr.rascafr.matlabit.profile.UserProfile;
import fr.rascafr.matlabit.utils.APIAsyncTask;
import fr.rascafr.matlabit.utils.APIResponse;
import fr.rascafr.matlabit.utils.EncryptUtils;

/**
 * Created by root on 14/04/16.
 */
public class AsyncListLoad extends APIAsyncTask {

    private ArrayList<ListItem> listItems;
    private ListAdapter adapter;
    private ProgressBar progressList;

    public AsyncListLoad(Context context, String url, ArrayList<ListItem> listItems, ListAdapter adapter, ProgressBar progressList) {
        super(context, url);
        this.listItems = listItems;
        this.adapter = adapter;
        this.progressList = progressList;

        // Get profile
        UserProfile profile = DataStorage.getInstance().getUserProfile();

        // Set parameters
        this.pairs.put("client", profile.getLogin());
        this.pairs.put("password", profile.getPassword());
        this.pairs.put("hash", EncryptUtils.sha256(context.getResources().getString(R.string.auth_list_match) + profile.getLogin() + profile.getPassword()));
    }

    @Override
    protected void onPreExecute() {
        listItems.clear();
        progressList.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(APIResponse apiResponse) {

        progressList.setVisibility(View.INVISIBLE);

        if (apiResponse.isValid()) {
            try {
                JSONObject obj = apiResponse.getJsonData();
                JSONArray listMe = obj.getJSONArray("moi");
                JSONArray listThem = obj.getJSONArray("eux");

                // Me / my list
                listItems.add(new ListItem("Ta liste"));
                if (listMe.length() == 0) {
                    listItems.add(new ListItem("Tu n'as encore ajouté personne à ta liste ! Timide ?", true));
                } else {
                    for (int i=0;i<listMe.length();i++) {
                        listItems.add(new ListItem(listMe.getJSONObject(i), true));
                    }
                }

                // Others
                listItems.add(new ListItem("Ils te veulent !"));

                if (listThem.length() == 0) {
                    listItems.add(new ListItem("Personne ne t'a encore matché ! En même temps vu ta tête ...", true));
                } else {
                    for (int i=0;i<listThem.length();i++) {
                        listItems.add(new ListItem(listThem.getJSONObject(i), false));
                    }
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
