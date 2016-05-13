package fr.rascafr.matlabit.ninja.game.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.rascafr.matlabit.R;
import fr.rascafr.matlabit.app.DataStorage;
import fr.rascafr.matlabit.async.AsyncScoreSend;

/**
 * Created by root on 24/04/16.
 */
public class ResultsFragment extends Fragment implements View.OnClickListener {

    private int score;

    private OnButtonPressed onButtonPressed;

    public interface OnButtonPressed {
        public void onButtonPressed(int bpID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_results, null);
        v.findViewById(R.id.imgRetry).setOnClickListener(this);
        v.findViewById(R.id.imgQuit).setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imgRetry) {
            onButtonPressed.onButtonPressed(R.id.imgRetry);
        } else if (view.getId() == R.id.imgQuit) {
            onButtonPressed.onButtonPressed(R.id.imgQuit);
        }
    }

    @Override
    public void onResume(){
        super.onResume();

        TextView view = (TextView) getView().findViewById(R.id.textview_results);
        view.setText("Score : " + score);

        new AsyncScoreSend(getActivity(), DataStorage.getInstance().getSPL_API_SEND_SCORE(), String.valueOf(score)).execute();
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            onButtonPressed = (OnButtonPressed) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnButtonPressed");
        }
    }
}