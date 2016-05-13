package fr.rascafr.matlabit.ninja.game.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import fr.rascafr.matlabit.R;
import fr.rascafr.matlabit.app.DataStorage;

/**
 * Created by root on 24/04/16.
 */
public class GameHostActivity extends AppCompatActivity implements GameFragment.OnGameOver, ResultsFragment.OnButtonPressed {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_host);

        // Starfoulah
        DataStorage.getInstance().initMediaPlayer(this);
        DataStorage.getInstance().getMediaPlayer().start();
        onPlayButtonClicked();
    }

    public void onPlayButtonClicked() {
        GameFragment gameFragment = new GameFragment();

        FragmentTransaction transaction = (FragmentTransaction) getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, gameFragment, "Game");
        transaction.addToBackStack("Game");
        transaction.commit();
    }

    @Override
    public void onGameOver(int score) {
        ResultsFragment resultsFragment = new ResultsFragment();
        resultsFragment.setScore(score);

        FragmentTransaction transaction = (FragmentTransaction) getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, resultsFragment, "Results");
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        DataStorage.getInstance().getMediaPlayer().reset();
        finish();
    }

    @Override
    protected void onStop() {
        DataStorage.getInstance().getMediaPlayer().reset();
        super.onStop();
    }

    @Override
    public void onButtonPressed(int bpID) {
        if (bpID == R.id.imgRetry) {
            onPlayButtonClicked();
        } else if (bpID == R.id.imgQuit) {
            this.onBackPressed();
        }
    }
}
