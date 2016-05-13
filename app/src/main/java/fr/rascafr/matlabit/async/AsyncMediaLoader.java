package fr.rascafr.matlabit.async;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import fr.rascafr.matlabit.Constants;

/**
 * Created by root on 23/04/16.
 */
public class AsyncMediaLoader extends AsyncTask<String,String,String> {

    private SharedPreferences prefs;
    private String mp3url;
    private File mp3cache;

    public AsyncMediaLoader(String mp3url, SharedPreferences prefs, File mp3cache) {
        this.prefs = prefs;
        this.mp3url = mp3url;
        this.mp3cache = mp3cache;
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            URL mediaURL = new URL(mp3url);
            URLConnection connection = mediaURL.openConnection();
            InputStream inputStream = new BufferedInputStream(mediaURL.openStream(), 10240);
            FileOutputStream outputStream = null;
            outputStream = new FileOutputStream(mp3cache);
            byte buffer[] = new byte[1024];
            int dataSize;
            while ((dataSize = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, dataSize);
            }
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {

        // Save that we have downloaded file
        prefs.edit().putString(Constants.PREFS_KEY_MP3_URL, mp3url);
        prefs.edit().apply();
    }
}
