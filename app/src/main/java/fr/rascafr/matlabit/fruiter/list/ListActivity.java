package fr.rascafr.matlabit.fruiter.list;

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
import fr.rascafr.matlabit.async.AsyncListLoad;

/**
 * Created by root on 18/04/16.
 */
public class ListActivity extends AppCompatActivity {

    // Android
    private Resources resources;
    private Context context;

    // Layout
    private RecyclerView recyList;
    private ProgressBar progressList;

    // Adapter
    private ListAdapter mAdapter;

    // Model
    private ArrayList<ListItem> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruiter_list);

        // Init base
        context = this;
        resources = getResources();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get views
        recyList = (RecyclerView) findViewById(R.id.recyListFruiter);
        progressList = (ProgressBar) findViewById(R.id.progressList);

        // Init views
        progressList.setVisibility(View.INVISIBLE);

        // Init model
        listItems = new ArrayList<>();

        // Init adapter
        mAdapter = new ListAdapter(context, listItems);
        recyList.setAdapter(mAdapter);
        recyList.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyList.setLayoutManager(llm);

        // Fetch data from server
        new AsyncListLoad(context, DataStorage.getInstance().getSPL_API_GET_LIST(), listItems, mAdapter, progressList).execute();
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
