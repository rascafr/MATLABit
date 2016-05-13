package fr.rascafr.matlabit.profile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import fr.rascafr.matlabit.R;
import fr.rascafr.matlabit.app.DataStorage;
import fr.rascafr.matlabit.async.AsyncDisconnectUser;
import fr.rascafr.matlabit.gcmpush.QuickstartPreferences;
import fr.rascafr.matlabit.interfaces.ContactInterface;
import fr.rascafr.matlabit.interfaces.OnProfileChange;

/**
 * Created by root on 14/04/16.
 */
public class ProfileActivity extends AppCompatActivity implements OnProfileChange, ContactInterface {

    private static final int INTENT_GALLERY_ID = 42;
    private static final int MAX_PROFILE_SIZE = 250;

    // Android
    private Context context;

    // Layout
    private FrameLayout frameContainer;

    // Fragment gestion
    private FragmentManager fragmentManager;
    private Fragment fragToLoad;

    // User profile
    private UserProfile userProfile;

    // GCM
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    // Dialog (gcm progress)
    private MaterialDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Init main objects
        context = this;
        fragmentManager = getSupportFragmentManager();
        userProfile = DataStorage.getInstance().getUserProfile();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get views
        frameContainer = (FrameLayout) findViewById(R.id.profile_frame_container);

        // Decide fragment to show
        loadFragment();

        // GCM Receiver
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("NOTIF", "onReceive !");
                dialog.dismiss();
                dialog.hide();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences.getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);

                if (sentToken) {
                    DataStorage.getInstance().setUserProfile(UserProfile.readProfileFromPrefs(context));
                    OnProfileChange(DataStorage.getInstance().getUserProfile());
                } else {
                    Toast.makeText(ProfileActivity.this, "Erreur d'enregistrement", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    public void OnProfileChange(UserProfile userProfile) {
        this.userProfile = userProfile;
        loadFragment(); // change fragment
        invalidateOptionsMenu();
    }

    /**
     * Decide which fragment to load
     */
    private void loadFragment() {

        if (userProfile.isProfileCreated()) {
            fragToLoad = FragmentProfileView.newInstance();
        } else {
            fragToLoad = FragmentProfileCreate.newInstance();
        }
        fragmentManager.beginTransaction().replace(R.id.profile_frame_container, fragToLoad).commit();
    }

    @Override
    public void OnDialogChange(boolean createDialog, boolean hideDialog, String title, String message) {
        if (createDialog) {
            dialog = new MaterialDialog.Builder(context)
                    .title(title)
                    .content(message)
                    .cancelable(false)
                    .progressIndeterminateStyle(false)
                    .progress(true, 0)
                    .show();
        } else if (hideDialog) {
            if (dialog != null) dialog.dismiss();
        } else {
            dialog.setTitle(title);
            dialog.setContent(message);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (userProfile.isProfileCreated()) getMenuInflater().inflate(R.menu.menu_disconnect, menu);
        else getMenuInflater().inflate(R.menu.menu_empty, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar actions click
        switch (item.getItemId()) {

            // Profile : disconnect it
            case R.id.action_disconnect:

                new MaterialDialog.Builder(context)
                        .title("Déconnexion")
                        .content("Êtes-vous sûr de vouloir continuer ?\nVous ne pourrez plus jouer à Oasis Ninja ni à Pineapple")
                        .positiveText("J'en peux plus de vous")
                        .negativeText("Non, Je me touche")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                // Async disconnect
                                new AsyncDisconnectUser(context, DataStorage.getInstance().getSPL_API_DEL_PUSH(), userProfile).execute();

                                UserProfile prof = UserProfile.getEmptyProfile();
                                DataStorage.getInstance().setUserProfile(prof);
                                prof.registerProfileInPrefs(context);
                                OnProfileChange(prof);
                            }
                        })
                        .show();

                return true;

            case android.R.id.home:
                this.onBackPressed();
                return true;


            // No default action
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Checks if the app has permission to write to device storage
     * <p/>
     * If the app does not has permission then the user will be prompted to grant permissions
     */
    /*public void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission_read = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED || permission_read != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    PERMISSION_REQUEST_CODE
            );
            return;
        }
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, INTENT_GALLERY_ID);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        Log.d("DBG", "Perm : " + requestCode);
        switch (requestCode) {

            case PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay!
                    Toast.makeText(context, "Permission Storage accordée ! :)", Toast.LENGTH_SHORT).show();

                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, INTENT_GALLERY_ID);

                } else {

                    // permission denied, boo!
                    new MaterialDialog.Builder(context)
                            .title("Faites-nous confiance")
                            .content("MATLABit ne vend pas vos photos, ne les publie pas sur internet.\nVous devez accepter la demande de permission si vous voulez pouvoir définir une photo de profil ...")
                            .cancelable(false)
                            .negativeText("Refuser")
                            .positiveText("Réessayer")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                                    verifyStoragePermissions(ProfileActivity.this);
                                }
                            })
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                                    Toast.makeText(ProfileActivity.this, "C'est pas cool de votre part. Timide ?", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .show();
                }
            }

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }*/
}
