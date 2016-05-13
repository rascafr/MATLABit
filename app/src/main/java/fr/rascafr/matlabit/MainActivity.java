package fr.rascafr.matlabit;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import fr.rascafr.matlabit.R;
import fr.rascafr.matlabit.app.DataStorage;
import fr.rascafr.matlabit.fruiter.FruiterFragment;
import fr.rascafr.matlabit.fruiter.best.BestActivity;
import fr.rascafr.matlabit.fruiter.list.ListActivity;
import fr.rascafr.matlabit.hackxor.HackXORFragment;
import fr.rascafr.matlabit.news.NewsFragment;
import fr.rascafr.matlabit.ninja.NinjaFragment;
import fr.rascafr.matlabit.ninja.game.ui.GameHostActivity;
import fr.rascafr.matlabit.profile.ProfileActivity;
import fr.rascafr.matlabit.profile.UserProfile;

public class MainActivity extends AppCompatActivity {

    // Tab definition
    private static final int TAB_NINJA = 0;
    private static final int TAB_FRUITER = 1;
    private static final int TAB_HACK = 2;
    private static final int TAB_NEWS = 3;

    // Custom design layout / manager
    private FrameLayout frameContainer;
    private int selectedTab = -1; // force redraw
    private ImageView imgNinja, imgFruiter, imgHack, imgNews, imgMatch;
    private TextView tvNinja, tvFruiter, tvHack, tvNews, tvMatch, tvMatchClose;
    private LinearLayout llNinja, llFruiter, llHack, llNews;
    private RelativeLayout llMatch;

    // Android
    private Resources resources;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_basic);

        // Init base
        context = this;
        resources = getResources();

        // Find views
        frameContainer = (FrameLayout) findViewById(R.id.frame_container);
        tvMatch = (TextView) findViewById(R.id.tvMatch);
        imgMatch = (ImageView) findViewById(R.id.imgMatch);
        llMatch = (RelativeLayout) findViewById(R.id.rlMatch);
        tvMatchClose = (TextView) findViewById(R.id.tvMatchClose);

        // Init views
        llMatch.setVisibility(View.INVISIBLE);

        // Containers
        llNinja = (LinearLayout) findViewById(R.id.llNinja);
        llFruiter = (LinearLayout) findViewById(R.id.llFruiter);
        llHack = (LinearLayout) findViewById(R.id.llHack);
        llNews = (LinearLayout) findViewById(R.id.llNews);

        // Icons
        imgNinja = (ImageView) findViewById(R.id.barIcoNinja);
        imgFruiter = (ImageView) findViewById(R.id.barIcoFruiter);
        imgHack = (ImageView) findViewById(R.id.barIcoHack);
        imgNews = (ImageView) findViewById(R.id.barIcoNews);

        // Text views
        tvNinja = (TextView) findViewById(R.id.barTvNinja);
        tvFruiter = (TextView) findViewById(R.id.barTvFruiter);
        tvHack = (TextView) findViewById(R.id.barTvHack);
        tvNews = (TextView) findViewById(R.id.barTvNews);

        // Receive Intent from notification
        Bundle extras = getIntent().getExtras();
        String message, title, extra;
        int intendID = 0;
        int fragToShow = TAB_NINJA;

        // If we've just received intent from push notification event, we handle it
        if (extras != null) {
            title = extras.getString(Constants.INTENT_KEY_TITLE);
            message = extras.getString(Constants.INTENT_KEY_MESSAGE);
            intendID = extras.getInt(Constants.INTENT_KEY_DATA);
            extra = extras.getString(Constants.INTENT_KEY_EXTRA);
            if (intendID == 0) {
                if (title != null && title.length() > 0 && message != null && message.length() > 0) {
                    new MaterialDialog.Builder(context)
                            .title(title)
                            .content(message)
                            .negativeText("Fermer")
                            .show();
                }
            } else {
                // not a message, we have to show the tinder fragment
                try {
                    JSONObject obj = new JSONObject(extra);
                    String jName = obj.getString("name");
                    String jImg = obj.getString("img");
                    tvMatch.setText(jName + " te veut dans sa liste BDE");
                    Picasso.with(context).load(jImg).into(imgMatch);
                    fragToShow = TAB_FRUITER;
                    llMatch.setVisibility(View.VISIBLE);
                    tvMatchClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            llMatch.setVisibility(View.INVISIBLE);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        // Default init
        barLayoutManager(fragToShow);

        // Set listeners
        llNinja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barLayoutManager(TAB_NINJA);
            }
        });

        llFruiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barLayoutManager(TAB_FRUITER);
            }
        });

        llHack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barLayoutManager(TAB_HACK);
            }
        });

        llNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barLayoutManager(TAB_NEWS);
            }
        });
    }

    /**
     * Bottom bar's touch events action manager
     */
    private void barLayoutManager(int sTab) {

        Fragment fragment = null;
        String title = "";

        if (selectedTab != sTab) {
            selectedTab = sTab;
            barDisableAll();

            switch (sTab) {

                case TAB_NINJA:
                    fragment = NinjaFragment.newInstance();
                    title = resources.getString(R.string.tab_ninja);
                    imgNinja.setImageDrawable(resources.getDrawable(R.drawable.ic_banana_sel));
                    tvNinja.setTextColor(resources.getColor(R.color.barTextEnabled));
                    break;

                case TAB_FRUITER:
                    fragment = FruiterFragment.newInstance();
                    title = resources.getString(R.string.tab_fruiter);
                    imgFruiter.setImageDrawable(resources.getDrawable(R.drawable.ic_like_sel));
                    tvFruiter.setTextColor(resources.getColor(R.color.barTextEnabled));
                    break;

                case TAB_HACK:
                    fragment = HackXORFragment.newInstance();
                    title = resources.getString(R.string.tab_hack);
                    imgHack.setImageDrawable(resources.getDrawable(R.drawable.ic_lock_sel));
                    tvHack.setTextColor(resources.getColor(R.color.barTextEnabled));
                    break;

                case TAB_NEWS:
                    fragment = NewsFragment.newInstance();
                    title = resources.getString(R.string.tab_news);
                    imgNews.setImageDrawable(resources.getDrawable(R.drawable.ic_news_sel));
                    tvNews.setTextColor(resources.getColor(R.color.barTextEnabled));
                    break;

                default:
                    break;
            }

            getSupportActionBar().setTitle(title);

            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_container, fragment, "frag" + sTab).commit();
                supportInvalidateOptionsMenu();
            }
        }
    }

    /**
     * Disable all icons and text views
     */
    private void barDisableAll() {
        imgNinja.setImageDrawable(resources.getDrawable(R.drawable.ic_banana_dis));
        imgFruiter.setImageDrawable(resources.getDrawable(R.drawable.ic_like_dis));
        imgHack.setImageDrawable(resources.getDrawable(R.drawable.ic_lock_dis));
        imgNews.setImageDrawable(resources.getDrawable(R.drawable.ic_news_dis));
        tvNinja.setTextColor(resources.getColor(R.color.barTextDisabled));
        tvFruiter.setTextColor(resources.getColor(R.color.barTextDisabled));
        tvHack.setTextColor(resources.getColor(R.color.barTextDisabled));
        tvNews.setTextColor(resources.getColor(R.color.barTextDisabled));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        UserProfile up = DataStorage.getInstance().getUserProfile();
        if (selectedTab == TAB_FRUITER) {
            if (up.isProfileCreated()) {
                getMenuInflater().inflate(R.menu.menu_fruiter, menu);
            } else {
                getMenuInflater().inflate(R.menu.menu_main, menu);
            }
        } else if (selectedTab == TAB_NINJA) {
            if (up.isProfileCreated()) {
                getMenuInflater().inflate(R.menu.menu_ninja, menu);
            } else {
                getMenuInflater().inflate(R.menu.menu_main, menu);
            }
        }
        else getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar actions click
        switch (item.getItemId()) {

            // Profile : manage or create it (let the ProfileActivity decides)
            case R.id.action_profile:
                Intent iProf = new Intent(context, ProfileActivity.class);
                startActivity(iProf);
                return true;

            // Best : show people with big sex-appeal
            case R.id.action_best:
                Intent iBest = new Intent(context, BestActivity.class);
                startActivity(iBest);
                return true;

            // List show people we love
            case R.id.action_my_list:
                Intent iList = new Intent(context, ListActivity.class);
                startActivity(iList);
                return true;

            // Play ! start GameFragment and have fun :)
            case R.id.action_play:
                Intent iPlay = new Intent(context, GameHostActivity.class);
                startActivity(iPlay);
                return true;

            // No default action
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }
}
