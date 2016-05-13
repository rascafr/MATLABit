package fr.rascafr.matlabit.async;

import android.content.Context;
import android.widget.Toast;

import fr.rascafr.matlabit.R;
import fr.rascafr.matlabit.app.DataStorage;
import fr.rascafr.matlabit.profile.UserProfile;
import fr.rascafr.matlabit.utils.APIAsyncTask;
import fr.rascafr.matlabit.utils.APIResponse;
import fr.rascafr.matlabit.utils.EncryptUtils;

/**
 * Created by root on 14/04/16.
 */
public class AsyncListDelete extends APIAsyncTask {

    public AsyncListDelete(Context context, String url, String coeur) {
        super(context, url);

        // Get profile
        UserProfile profile = DataStorage.getInstance().getUserProfile();

        // Set parameters
        this.pairs.put("client", profile.getLogin());
        this.pairs.put("coeur", coeur);
        this.pairs.put("password", profile.getPassword());
        this.pairs.put("hash", EncryptUtils.sha256(context.getResources().getString(R.string.auth_delete_match) + profile.getLogin() + coeur + profile.getPassword()));
    }

    @Override
    protected void onPreExecute() {
        // nope
    }

    @Override
    protected void onPostExecute(APIResponse apiResponse) {

        if (!apiResponse.isValid()) {
            Toast.makeText(context, apiResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
        }
    }
}
