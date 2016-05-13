package fr.rascafr.matlabit.async;

import android.content.Context;
import android.widget.Toast;

import fr.rascafr.matlabit.R;
import fr.rascafr.matlabit.profile.UserProfile;
import fr.rascafr.matlabit.utils.APIAsyncTask;
import fr.rascafr.matlabit.utils.APIResponse;
import fr.rascafr.matlabit.utils.EncryptUtils;

/**
 * Created by root on 23/04/16.
 */
public class AsyncSendMatch extends APIAsyncTask {

    public AsyncSendMatch(Context context, String url, String coeur) {
        super(context, url);

        UserProfile profile = UserProfile.readProfileFromPrefs(context);

        // Set parameters
        this.pairs.put("coeur", coeur);
        this.pairs.put("client", profile.getLogin());
        this.pairs.put("password", profile.getPassword());
        this.pairs.put("hash", EncryptUtils.sha256(context.getResources().getString(R.string.auth_send_match) + profile.getLogin() + coeur + profile.getPassword()));
    }

    @Override
    protected void onPreExecute() {
        // nop
    }

    @Override
    protected void onPostExecute(APIResponse apiResponse) {

        if (!apiResponse.isValid()) {
            Toast.makeText(context, apiResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
        }
    }
}
