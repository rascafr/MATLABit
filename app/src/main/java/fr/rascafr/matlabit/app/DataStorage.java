package fr.rascafr.matlabit.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import fr.rascafr.matlabit.Constants;
import fr.rascafr.matlabit.async.AsyncMediaLoader;
import fr.rascafr.matlabit.profile.UserProfile;

/**
 * Created by root on 14/04/16.
 */
public class DataStorage {

    private static DataStorage instance;

    public static DataStorage getInstance () {
        if (instance == null)
            instance = new DataStorage();
        return instance;
    }

    private String splLogo, splLogoEux, splfb, splSon;
    private double harder;
    private MediaPlayer mediaPlayer;

    private String
            SPL_API_CONNECT, SPL_API_ADD_PICT, SPL_API_DEL_PICT,
            SPL_API_GET_SCORES, SPL_API_SEND_SCORE,
            SPL_API_GET_MATCHES, SPL_API_SEND_MATCH, SPL_API_DEL_MATCH, SPL_API_GET_LIST, SPL_API_GET_BEST,
            SPL_API_DEL_PUSH;

    public void fillSplData (JSONObject obj, Context context) throws JSONException {
        splLogo = obj.getString("logo");
        splLogoEux = obj.getString("logoEux");
        splfb = obj.getString("fb");
        splSon = obj.getString("son");
        harder = obj.getDouble("harderAndroid");
        JSONObject phpObj = obj.getJSONObject("php");
        SPL_API_CONNECT = phpObj.getString("connect");
        SPL_API_ADD_PICT = phpObj.getString("addPic");
        SPL_API_DEL_PICT = phpObj.getString("delPic");
        SPL_API_DEL_PUSH = phpObj.getString("delPush");
        SPL_API_GET_SCORES = phpObj.getString("getScores");
        SPL_API_SEND_SCORE = phpObj.getString("sendScore");
        SPL_API_GET_MATCHES = phpObj.getString("getMatches");
        SPL_API_SEND_MATCH = phpObj.getString("sendMatch");
        SPL_API_DEL_MATCH = phpObj.getString("delMatch");
        SPL_API_GET_LIST = phpObj.getString("getList");
        SPL_API_GET_BEST = phpObj.getString("getBest");

        mediaPlayer = new MediaPlayer();

        // Get mp3 cache uri
        File mp3cache = new File(context.getCacheDir() + "/fuit.mp3");

        // Start asynctask if not existing
        // or if URL has changed
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        if (!mp3cache.exists() || prefs.getString(Constants.PREFS_KEY_MP3_URL, "").equalsIgnoreCase(splSon)) {

            // We have to download new mp3 file
            new AsyncMediaLoader(splSon, prefs, mp3cache).execute();
        }
    }

    public void initMediaPlayer (Context context) {

        // Get mp3 cache uri
        File mp3cache = new File(context.getCacheDir() + "/fuit.mp3");

        // Get stream
        try {
            FileInputStream stream = new FileInputStream(mp3cache);
            mediaPlayer.setDataSource(stream.getFD());
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public String getSplLogo() {
        return splLogo;
    }

    public String getSplfb() {
        return splfb;
    }

    public double getHarder() {
        return harder;
    }

    public String getSPL_API_CONNECT() {
        return SPL_API_CONNECT;
    }

    public String getSPL_API_ADD_PICT() {
        return SPL_API_ADD_PICT;
    }

    public String getSPL_API_DEL_PICT() {
        return SPL_API_DEL_PICT;
    }

    public String getSPL_API_GET_SCORES() {
        return SPL_API_GET_SCORES;
    }

    public String getSPL_API_SEND_SCORE() {
        return SPL_API_SEND_SCORE;
    }

    public String getSPL_API_GET_MATCHES() {
        return SPL_API_GET_MATCHES;
    }

    public String getSPL_API_SEND_MATCH() {
        return SPL_API_SEND_MATCH;
    }

    public String getSPL_API_DEL_MATCH() {
        return SPL_API_DEL_MATCH;
    }

    public String getSPL_API_GET_LIST() {
        return SPL_API_GET_LIST;
    }

    public String getSPL_API_DEL_PUSH() {
        return SPL_API_DEL_PUSH;
    }

    public String getSplLogoEux() {
        return splLogoEux;
    }

    public String getSplSon() {
        return splSon;
    }

    public String getSPL_API_GET_BEST() {
        return SPL_API_GET_BEST;
    }

    private UserProfile userProfile;

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void test () {

        try {
            JSONObject obj = new JSONObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
