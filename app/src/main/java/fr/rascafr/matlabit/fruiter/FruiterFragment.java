package fr.rascafr.matlabit.fruiter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import fr.rascafr.matlabit.R;
import fr.rascafr.matlabit.app.DataStorage;
import fr.rascafr.matlabit.async.AsyncFruiterMatches;
import fr.rascafr.matlabit.async.AsyncSendMatch;
import fr.rascafr.matlabit.profile.ProfileActivity;
import fr.rascafr.matlabit.profile.UserProfile;

/**
 * Created by root on 13/04/16.
 */
public class FruiterFragment extends Fragment {

    // Layout
    private SwipeFlingAdapterView flingContainer;
    private TextView tvMessage;
    private ProgressBar progressFruiter;
    private LinearLayout llNo, llSwipe;
    private TextView tvConnect, tvInfo;

    // Adapter
    private CardSimpleAdapter swipeAdapter;

    // Model
    private ArrayList<CardModel> matchItems;
    private boolean swipeOccured = true; // flag to prevent multiple loads

    // Android
    private Context context;
    private Resources resources;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fruiter, container, false);
        context = getActivity();
        resources = context.getResources();

        // Find views
        flingContainer = (SwipeFlingAdapterView) rootView.findViewById(R.id.swipeCard);
        tvMessage = (TextView) rootView.findViewById(R.id.tvFruiterMessage);
        progressFruiter = (ProgressBar) rootView.findViewById(R.id.progressFruiter);
        llNo = (LinearLayout) rootView.findViewById(R.id.llFruiterNo);
        llSwipe = (LinearLayout) rootView.findViewById(R.id.llSwipe);
        tvConnect = (TextView) rootView.findViewById(R.id.bpFruiterConnect);
        tvInfo = (TextView) rootView.findViewById(R.id.tvFruiterInfo);
        Picasso.with(context).load(DataStorage.getInstance().getSplLogo()).into((ImageView)rootView.findViewById(R.id.imgLogo));

        // Init views
        tvMessage.setVisibility(View.INVISIBLE);
        progressFruiter.setVisibility(View.INVISIBLE);

        // Init model
        matchItems = new ArrayList<>();

        // Init swipe adapter
        swipeAdapter = new CardSimpleAdapter(context, matchItems);

        // Set adapter, init listeners
        flingContainer.setAdapter(swipeAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                matchItems.remove(0);
                swipeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                // Dismiss card
                swipeOccured = true;
            }

            @Override
            public void onRightCardExit(Object dataObject) {

                // Send match
                new AsyncSendMatch(context, DataStorage.getInstance().getSPL_API_SEND_MATCH(), ((CardModel) dataObject).getLogin()).execute();
                swipeOccured = true;
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {

                // Start async task, ask for data
                if (swipeOccured) {
                    new AsyncFruiterMatches(context, DataStorage.getInstance().getSPL_API_GET_MATCHES(), swipeAdapter, tvMessage, progressFruiter).execute();
                    swipeOccured = false;
                }
            }

            @Override
            public void onScroll(float v) {

            }
        });

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

        UserProfile up = DataStorage.getInstance().getUserProfile();

        if (up.isProfileCreated()) {

            if (up.getImgWebPath() != null && up.getImgWebPath().length() > 0) {

                // Load data from server
                flingContainer.setVisibility(View.VISIBLE);
                llSwipe.setVisibility(View.VISIBLE);
                llNo.setVisibility(View.INVISIBLE);

            } else {

                tvConnect.setText("Ajouter ma photo");
                tvInfo.setText("Pour draguer les autres, il te faut ajouter ta photo de profil !\n(c'est plus convivial)");

                flingContainer.setVisibility(View.INVISIBLE);
                llSwipe.setVisibility(View.INVISIBLE);
                llNo.setVisibility(View.VISIBLE);
            }

        } else {

            tvConnect.setText("Me connecter");
            tvInfo.setText("On botte en touche pour trouver ton pseudo, joue grâce à ton login ESEO !");

            flingContainer.setVisibility(View.INVISIBLE);
            llSwipe.setVisibility(View.INVISIBLE);
            llNo.setVisibility(View.VISIBLE);
        }
    }

    public static FruiterFragment newInstance() {
        return new FruiterFragment();
    }
}
