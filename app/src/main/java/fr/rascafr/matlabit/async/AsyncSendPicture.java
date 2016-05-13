package fr.rascafr.matlabit.async;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import fr.rascafr.matlabit.R;
import fr.rascafr.matlabit.app.DataStorage;
import fr.rascafr.matlabit.profile.UserProfile;
import fr.rascafr.matlabit.utils.ConnexionUtils;
import fr.rascafr.matlabit.utils.EncryptUtils;
import fr.rascafr.matlabit.utils.Utilities;

/**
 * Created by root on 19/04/16.
 */
public class AsyncSendPicture extends AsyncTask<String,String,String> {

    private byte[] bImage;
    private Context context;
    private String url;
    private HashMap<String, String> pairs;
    private MaterialDialog md;
    private ImageView imgProfile;

    public AsyncSendPicture(String url, Context context, byte[] bImage, ImageView imgProfile) {
        this.bImage = bImage;
        this.pairs = new HashMap<>();
        this.url = url;
        this.context = context;
        this.imgProfile = imgProfile;

        UserProfile profile = DataStorage.getInstance().getUserProfile();

        pairs.put("client", profile.getLogin());
        pairs.put("password", profile.getPassword());
        pairs.put("hash", EncryptUtils.sha256(context.getResources().getString(R.string.auth_send_picture) + profile.getLogin() + profile.getPassword()));
    }

    @Override
    protected void onPreExecute() {
        md = new MaterialDialog.Builder(context)
                .title("Envoi en cours")
                .content("Merci de bien vouloir patienter un peu ...")
                .progress(true, 0)
                .progressIndeterminateStyle(false)
                .cancelable(false)
                .show();
    }

    @Override
    protected String doInBackground(String... params) {
        return ConnexionUtils.postRawServerData(url, pairs, bImage, context);
    }

    @Override
    protected void onPostExecute(String data) {
        md.dismiss();

        int success = 0;
        String cause = "Impossible de se connecter au serveur, veuillez vérifier votre connexion internet.";
        String imgPath = "";

        if (Utilities.isNetworkDataValid(data)) {
            try {
                JSONObject obj = new JSONObject(data);
                success = obj.getInt("status");
                cause = obj.getString("cause");
                imgPath = obj.getJSONObject("data").getString("imgPath");
            } catch (JSONException e) {
                cause = "Erreur serveur : réessayez plus tard ...";
            }
        }

        if (success == 1) {
            // ok send !
            DataStorage.getInstance().getUserProfile().setImgWebPath(imgPath);
            DataStorage.getInstance().getUserProfile().registerProfileInPrefs(context);
            Picasso.with(context).load(imgPath).into(imgProfile);
        } else {
            // fail
            md = new MaterialDialog.Builder(context)
                    .title("Erreur")
                    .content(cause)
                    .negativeText("Fermer")
                    .show();
        }
    }
}
