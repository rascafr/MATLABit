package fr.rascafr.matlabit;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import fr.rascafr.matlabit.R;
import fr.rascafr.matlabit.async.AsyncSplashLoad;

/**
 * Created by root on 13/04/16.
 */
public class SplashActivity extends AppCompatActivity {

    // Layout
    private ProgressBar progressSplash;

    // Android
    private Resources resources;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Init base
        context = this;
        resources = getResources();

        // Find views
        progressSplash = (ProgressBar) findViewById(R.id.progressSplash);
        progressSplash.getIndeterminateDrawable().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);

        Bundle extras = getIntent().getExtras();

        // Start asyncTask
        AsyncSplashLoad splashLoad = new AsyncSplashLoad(context, Constants.URL_API_GENERAL, progressSplash, SplashActivity.this, extras);
        splashLoad.execute();
    }
}
