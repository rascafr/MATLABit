package fr.rascafr.matlabit.ninja;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import fr.rascafr.matlabit.R;
import fr.rascafr.matlabit.app.DataStorage;
import fr.rascafr.matlabit.async.AsyncScoresLoad;
import fr.rascafr.matlabit.profile.ProfileActivity;

/**
 * Created by root on 14/04/16.
 */
public class NinjaFragment extends Fragment {

    // Layout
    private RecyclerView recyList;
    private LinearLayout llNo;
    private TextView tvConnect;
    private ProgressBar progressNinja;

    // Adapter
    private NinjaAdapter mAdapter;

    // Model
    private ArrayList<ScoreItem> scoreItems;

    // Android
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ninja_scores, container, false);
        context = getActivity();

        // Find views
        recyList = (RecyclerView) rootView.findViewById(R.id.recyListScores);
        llNo = (LinearLayout) rootView.findViewById(R.id.llNinjaNo);
        tvConnect = (TextView) rootView.findViewById(R.id.bpNinjaConnect);
        progressNinja = (ProgressBar) rootView.findViewById(R.id.progressNinja);
        Picasso.with(context).load(DataStorage.getInstance().getSplLogo()).into((ImageView)rootView.findViewById(R.id.imgLogo));

        // Init views
        llNo.setVisibility(View.INVISIBLE);
        recyList.setVisibility(View.INVISIBLE);
        progressNinja.setVisibility(View.INVISIBLE);

        // Init model
        scoreItems = new ArrayList<>();

        // Init adapter
        mAdapter = new NinjaAdapter(context, scoreItems);
        recyList.setAdapter(mAdapter);
        recyList.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyList.setLayoutManager(llm);

        // Connect listener
        tvConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iProf = new Intent(context, ProfileActivity.class);
                startActivity(iProf);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (DataStorage.getInstance().getUserProfile().isProfileCreated()) {

            // Load data from server
            recyList.setVisibility(View.VISIBLE);
            llNo.setVisibility(View.INVISIBLE);
            AsyncScoresLoad scoresLoad = new AsyncScoresLoad(context, DataStorage.getInstance().getSPL_API_GET_SCORES(), scoreItems, mAdapter, progressNinja);
            scoresLoad.execute();
        } else {
            recyList.setVisibility(View.INVISIBLE);
            llNo.setVisibility(View.VISIBLE);
        }
    }

    public static NinjaFragment newInstance() {
        return new NinjaFragment();
    }
}
