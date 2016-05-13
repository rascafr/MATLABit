package fr.rascafr.matlabit.async;

import android.content.Context;
import android.widget.ImageView;
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
public class AsyncPictureDelete extends APIAsyncTask {

    private ImageView imgProfile;

    public AsyncPictureDelete(Context context, String url, ImageView imgProfile) {
        super(context, url);
        this.imgProfile = imgProfile;

        // Get profile
        UserProfile profile = DataStorage.getInstance().getUserProfile();

        // Set parameters
        this.pairs.put("client", profile.getLogin());
        this.pairs.put("password", profile.getPassword());
        this.pairs.put("hash", EncryptUtils.sha256(context.getResources().getString(R.string.auth_delete_picture) + profile.getLogin() + profile.getPassword()));
    }

    @Override
    protected void onPreExecute() {
        // nope
    }

    @Override
    protected void onPostExecute(APIResponse apiResponse) {

        if (!apiResponse.isValid()) {
            Toast.makeText(context, apiResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
        } else {
            // ok deleted !
            DataStorage.getInstance().getUserProfile().setImgWebPath("");
            DataStorage.getInstance().getUserProfile().registerProfileInPrefs(context);
            imgProfile.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_no_user));
            Toast.makeText(context, "Votre photo de profil a bien été supprimée", Toast.LENGTH_SHORT).show();
        }
    }
}
