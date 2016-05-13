package fr.rascafr.matlabit.fruiter.best;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;

import fr.rascafr.matlabit.R;
import fr.rascafr.matlabit.app.DataStorage;
import fr.rascafr.matlabit.async.AsyncBestLoad;

/**
 * Created by root on 23/04/16.
 */
public class BestActivity extends AppCompatActivity {

    // Android
    private Resources resources;
    private Context context;

    // Layout
    private RecyclerView recyList;
    private ProgressBar progressBest;

    // Adapter
    private BestAdapter mAdapter;

    // Model
    private ArrayList<BestItem> bestItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruiter_best);

        // Init base
        context = this;
        resources = getResources();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get views
        recyList = (RecyclerView) findViewById(R.id.recyListBest);
        progressBest = (ProgressBar) findViewById(R.id.progressBest);

        // Init views
        progressBest.setVisibility(View.INVISIBLE);

        // Init model
        bestItems = new ArrayList<>();

        // Init adapter
        mAdapter = new BestAdapter(context, bestItems);
        recyList.setAdapter(mAdapter);
        recyList.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyList.setLayoutManager(llm);

        // Fetch data from server
        new AsyncBestLoad (context, DataStorage.getInstance().getSPL_API_GET_BEST(), bestItems, mAdapter, progressBest).execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar actions click
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
