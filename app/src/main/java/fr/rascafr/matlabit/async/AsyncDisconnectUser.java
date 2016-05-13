package fr.rascafr.matlabit.async;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import fr.rascafr.matlabit.Constants;
import fr.rascafr.matlabit.R;
import fr.rascafr.matlabit.profile.UserProfile;
import fr.rascafr.matlabit.utils.APIAsyncTask;
import fr.rascafr.matlabit.utils.APIResponse;
import fr.rascafr.matlabit.utils.EncryptUtils;

/**
 * Created by root on 14/04/16.
 */
public class AsyncDisconnectUser extends APIAsyncTask {

    private MaterialDialog materialDialog;

    public AsyncDisconnectUser(Context context, String url, UserProfile oldProfile) {
        super(context, url);

        // Set parameters
        this.pairs.put("client", oldProfile.getLogin());
        this.pairs.put("password", oldProfile.getPassword());
        this.pairs.put("token", oldProfile.getPushToken());
        this.pairs.put("os", Constants.APP_TYPE);
        this.pairs.put("hash", EncryptUtils.sha256(context.getResources().getString(R.string.auth_unregister) + oldProfile.getLogin() + oldProfile.getPassword() + Constants.APP_TYPE + oldProfile.getPushToken()));

        Log.d("DBG", "pairs = " + pairs.toString());
    }

    @Override
    protected void onPreExecute() {

        // Create dialog
        materialDialog = new MaterialDialog.Builder(context)
                .title("Déconnexion")
                .content("Veuillez patienter")
                .progress(true, 0)
                .progressIndeterminateStyle(false)
                .cancelable(false)
                .show();
    }

    @Override
    protected void onPostExecute(APIResponse apiResponse) {

        // Hide dialog
        materialDialog.hide();

        if (apiResponse.isValid()) {
            Toast.makeText(context, "Vous avez été déconnectés", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, apiResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
        }
    }
}
