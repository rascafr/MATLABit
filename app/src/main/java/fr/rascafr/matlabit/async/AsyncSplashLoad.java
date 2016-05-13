package fr.rascafr.matlabit.async;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;

import fr.rascafr.matlabit.Constants;
import fr.rascafr.matlabit.MainActivity;
import fr.rascafr.matlabit.app.DataStorage;
import fr.rascafr.matlabit.profile.UserProfile;
import fr.rascafr.matlabit.utils.APIAsyncTask;
import fr.rascafr.matlabit.utils.APIResponse;

/**
 * Created by root on 13/04/16.
 */
public class AsyncSplashLoad extends APIAsyncTask {

    private ProgressBar progress;
    private AppCompatActivity activity;
    private Bundle extras;

    public AsyncSplashLoad(Context context, String url, ProgressBar progress, AppCompatActivity activity, Bundle extras) {
        super(context, url);
        this.progress = progress;
        this.activity = activity;
        this.extras = extras;
    }

    @Override
    protected void onPreExecute() {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(APIResponse apiResponse) {
        progress.setVisibility(View.INVISIBLE);
        if (apiResponse.isValid()) { // HTTP_OK = 200
            try {

                // Fill urls
                DataStorage.getInstance().fillSplData(apiResponse.getJsonData(), context);

                // Load user profile from prefs
                DataStorage.getInstance().setUserProfile(UserProfile.readProfileFromPrefs(context));

                // Start GameFragment
                Intent i = new Intent(context, MainActivity.class);

                // If we've just received intent from push notification event, we handle it
                if (extras != null) {
                    i.putExtra(Constants.INTENT_KEY_TITLE, extras.getString(Constants.INTENT_KEY_TITLE));
                    i.putExtra(Constants.INTENT_KEY_MESSAGE, extras.getString(Constants.INTENT_KEY_MESSAGE));
                    i.putExtra(Constants.INTENT_KEY_DATA, extras.getInt(Constants.INTENT_KEY_DATA));
                    i.putExtra(Constants.INTENT_KEY_EXTRA, extras.getString(Constants.INTENT_KEY_EXTRA));
                }

                context.startActivity(i);

                // Close SplashScreen
                activity.finish();

            } catch (JSONException e) {
                Toast.makeText(context, "Erreur serveur. Veuillez réessayer plus tard ...", Toast.LENGTH_LONG).show();
            }
        } else {
            String errmsg = apiResponse.getErrorMsg().length() == 0 ? "Vous n'avez pas le droit d'accéder à ceci ... pour le moment !" : apiResponse.getErrorMsg();
            Toast.makeText(context, errmsg, Toast.LENGTH_LONG).show();
        }
    }
}
