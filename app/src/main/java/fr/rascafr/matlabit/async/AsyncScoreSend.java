package fr.rascafr.matlabit.async;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;

import fr.rascafr.matlabit.R;
import fr.rascafr.matlabit.app.DataStorage;
import fr.rascafr.matlabit.profile.UserProfile;
import fr.rascafr.matlabit.utils.APIAsyncTask;
import fr.rascafr.matlabit.utils.APIResponse;
import fr.rascafr.matlabit.utils.EncryptUtils;

/**
 * Created by root on 14/04/16.
 */
public class AsyncScoreSend extends APIAsyncTask {

    public AsyncScoreSend(Context context, String url, String score) {
        super(context, url);

        // Get profile
        UserProfile profile = DataStorage.getInstance().getUserProfile();

        // Set parameters
        this.pairs.put("client", profile.getLogin());
        this.pairs.put("score", score);
        this.pairs.put("os", "Android");
        this.pairs.put("password", profile.getPassword());
        this.pairs.put("hash", EncryptUtils.sha256(context.getResources().getString(R.string.auth_ninja) + profile.getLogin() + score + profile.getPassword()));
    }

    @Override
    protected void onPreExecute() {
        // nope
    }

    @Override
    protected void onPostExecute(APIResponse apiResponse) {

        if (!apiResponse.isValid()) {
            new MaterialDialog.Builder(context)
                    .title("Erreur lors de l'envoi des scores ...")
                    .content(apiResponse.getErrorMsg())
                    .negativeText("Fermer")
                    .show();
        }
    }
}
