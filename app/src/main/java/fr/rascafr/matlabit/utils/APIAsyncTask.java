package fr.rascafr.matlabit.utils;

import android.content.Context;
import android.os.AsyncTask;

import java.util.HashMap;

/**
 * Created by root on 13/04/16.
 */
public class APIAsyncTask extends AsyncTask<String,String,APIResponse>  {

    protected Context context;
    protected String url;
    protected HashMap<String, String> pairs;

    public APIAsyncTask (Context context, String url) {
        this.context = context;
        this.url = url;
        this.pairs = new HashMap<>();
    }

    @Override
    protected APIResponse doInBackground(String... more) {
        return ConnexionUtils.getAPIResponse(url, pairs, context);
    }
}
