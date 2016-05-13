package fr.rascafr.matlabit.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import fr.rascafr.matlabit.Constants;

/**
 * Created by root on 14/04/16.
 */
public class UserProfile {

    private String name, login, password, imgWebPath, pushToken;

    public UserProfile(String name, String login, String password, String imgWebPath, String pushToken) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.imgWebPath = imgWebPath;
        this.pushToken = pushToken;
    }

    public UserProfile(JSONObject obj, String login, String password) throws JSONException {
        this(obj.getString("username"), login, password, obj.getString("img"), "");
    }

    public static UserProfile readProfileFromPrefs(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return new UserProfile(
                prefs.getString(Constants.PREFS_KEY_PROFILE_NAME, ""),
                prefs.getString(Constants.PREFS_KEY_PROFILE_LOGIN, ""),
                prefs.getString(Constants.PREFS_KEY_PROFILE_PASSWORD, ""),
                prefs.getString(Constants.PREFS_KEY_PROFILE_WEBPATH, ""),
                prefs.getString(Constants.PREFS_KEY_PROFILE_PUSHTOKEN, "")
        );
    }

    public static UserProfile getEmptyProfile () {
        return new UserProfile("", "", "", "", "");
    }

    public void registerProfileInPrefs(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();

        edit.putString(Constants.PREFS_KEY_PROFILE_NAME, name);
        edit.putString(Constants.PREFS_KEY_PROFILE_LOGIN, login);
        edit.putString(Constants.PREFS_KEY_PROFILE_PASSWORD, password);
        edit.putString(Constants.PREFS_KEY_PROFILE_WEBPATH, imgWebPath);
        edit.putString(Constants.PREFS_KEY_PROFILE_PUSHTOKEN, pushToken);
        edit.apply();
    }

    public boolean isProfileCreated () {
        return login != null && login.length() > 0;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getPushToken() {
        return pushToken;
    }

    public String getImgWebPath() {
        return imgWebPath;
    }

    public void setImgWebPath(String imgWebPath) {
        this.imgWebPath = imgWebPath;
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }
}
