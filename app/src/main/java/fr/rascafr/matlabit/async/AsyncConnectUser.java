package fr.rascafr.matlabit.async;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import fr.rascafr.matlabit.R;
import fr.rascafr.matlabit.app.DataStorage;
import fr.rascafr.matlabit.gcmpush.RegistrationIntentService;
import fr.rascafr.matlabit.interfaces.ContactInterface;
import fr.rascafr.matlabit.interfaces.OnProfileChange;
import fr.rascafr.matlabit.profile.UserProfile;
import fr.rascafr.matlabit.utils.APIAsyncTask;
import fr.rascafr.matlabit.utils.APIResponse;
import fr.rascafr.matlabit.utils.EncryptUtils;
import fr.rascafr.matlabit.utils.Utilities;

/**
 * Created by root on 14/04/16.
 */
public class AsyncConnectUser extends APIAsyncTask {

    private OnProfileChange changeInterface;
    private String login, password, b64password, hashPassword;
    private Activity activity;
    private ContactInterface dialogInterface;

    public AsyncConnectUser(Context context, String url, String login, String password, ContactInterface dialogInterface, Activity activity) {
        super(context, url);
        this.activity = activity;

        // Get values / objects
        this.dialogInterface = dialogInterface;
        this.login = login;
        this.password = password;
        this.hashPassword = EncryptUtils.sha256(context.getResources().getString(R.string.auth_password) + password);
        this.b64password = EncryptUtils.passBase64(password);

        // Set parameters
        this.pairs.put("username", login);
        this.pairs.put("password", b64password);
        this.pairs.put("hash", EncryptUtils.sha256(login + b64password + context.getResources().getString(R.string.auth_login)));
    }

    @Override
    protected void onPreExecute() {
        // Create dialog
        dialogInterface.OnDialogChange(true, false, "Connexion", "Veuillez patienter");
    }

    @Override
    protected void onPostExecute(APIResponse apiResponse) {

        if (apiResponse.isValid()) {

            JSONObject obj = apiResponse.getJsonData();

            try {
                UserProfile resultProfile = new UserProfile(obj, login, hashPassword);
                DataStorage.getInstance().setUserProfile(resultProfile);
                resultProfile.registerProfileInPrefs(context);
                //changeInterface.OnProfileChange(resultProfile);

                // Check Services, then start Registration class
                if (Utilities.checkPlayServices(activity)) {

                    dialogInterface.OnDialogChange(false, false, "Connexion", "Enregistrement de l'appareil");

                    // Start IntentService to register this application with GCM.
                    Intent intent = new Intent(context, RegistrationIntentService.class);
                    context.startService(intent);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                dialogInterface.OnDialogChange(false, true, "", "");
            }

        } else {
            dialogInterface.OnDialogChange(false, true, "", "");
            Toast.makeText(context, apiResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
        }
    }
}
