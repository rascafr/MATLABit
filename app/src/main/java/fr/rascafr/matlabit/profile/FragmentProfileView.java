package fr.rascafr.matlabit.profile;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import fr.rascafr.matlabit.R;
import fr.rascafr.matlabit.app.DataStorage;
import fr.rascafr.matlabit.async.AsyncPictureDelete;
import fr.rascafr.matlabit.async.AsyncSendPicture;
import fr.rascafr.matlabit.interfaces.ContactInterface;
import fr.rascafr.matlabit.utils.ImageUtils;

/**
 * Created by root on 14/04/16.
 */
public class FragmentProfileView extends Fragment {

    private static final int REQUEST_CODE_SOME_FEATURES_PERMISSIONS = 69;
    private static final int INTENT_GALLERY_ID = 42; // quarantdeuuux t'as vu
    private static final int MAX_PROFILE_SIZE = 280;

    // Android
    private Context context;

    // Layout
    private TextView tvWelcome, tvChoose;
    private CircleImageView imgProfile;

    // Interfaces
    private ContactInterface dialogInterface;

    // User
    private UserProfile userProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile_view, container, false);
        context = getActivity();
        userProfile = DataStorage.getInstance().getUserProfile();

        // Get views
        tvWelcome = (TextView) rootView.findViewById(R.id.tvProfileWelcome);
        tvChoose = (TextView) rootView.findViewById(R.id.tvProfileChoosePict);
        imgProfile = (CircleImageView) rootView.findViewById(R.id.imgProfilePicture);

        // Set views
        tvWelcome.setText("Salut " + userProfile.getName() + " !");
        if (userProfile.getImgWebPath().length() > 0)
            Picasso.with(context).load(userProfile.getImgWebPath()).into(imgProfile);
        else
            imgProfile.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_no_user));
        tvChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new MaterialDialog.Builder(context)
                        .title("Photomaton")
                        .content("Que souhaitez-vous faire ?")
                        .positiveText("Supprimer la photo")
                        .neutralText("Choisir une photo")
                        .negativeText("Annuler")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                new AsyncPictureDelete(context, DataStorage.getInstance().getSPL_API_DEL_PICT(), imgProfile).execute();
                            }
                        })
                        .onNeutral(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                // Check for permissions
                                int readStoragePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
                                int writeStoragePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                                List<String> permissions = new ArrayList<>();
                                if (readStoragePermission != PackageManager.PERMISSION_GRANTED) {
                                    permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                                    Log.d("Permissions", "Ask for READ");
                                }

                                if (!permissions.isEmpty()) {
                                    Log.d("Permissions", "Ask for " + permissions.size() + " permissions");
                                    requestPermissions(permissions.toArray(new String[permissions.size()]), REQUEST_CODE_SOME_FEATURES_PERMISSIONS);
                                } else {
                                    Log.d("Permissions", "All permissions granted");
                                    sendIntentToGallery();
                                }
                            }
                        })
                        .show();
            }
        });

        return rootView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_SOME_FEATURES_PERMISSIONS: {
                boolean allPermGranted = true;
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Log.d("Permissions", "Permission Granted: " + permissions[i]);
                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        Log.d("Permissions", "Permission Denied: " + permissions[i]);
                        allPermGranted = false;
                    }
                }

                if (allPermGranted) {
                    sendIntentToGallery();
                }
            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    private void sendIntentToGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, INTENT_GALLERY_ID);
    }

    public static FragmentProfileView newInstance() {
        return new FragmentProfileView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == INTENT_GALLERY_ID && resultCode == Activity.RESULT_OK && data != null) {

            Uri profPicture = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(profPicture, filePathColumn, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                Bitmap bmp = ImageUtils.getResizedBitmap(BitmapFactory.decodeFile(picturePath), MAX_PROFILE_SIZE);

                if (bmp != null) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                    byte[] b = baos.toByteArray();
                    new AsyncSendPicture(DataStorage.getInstance().getSPL_API_ADD_PICT(), context, b, imgProfile).execute();
                } else {
                    Toast.makeText(context, "Vous n'avez pas autorisé l'accès aux images.", Toast.LENGTH_SHORT).show();
                    new MaterialDialog.Builder(context)
                            .title("Oups ...")
                            .content("On dirait que vous n'avez pas autorisé l'accès aux images.\nSi c'est le cas, mais que vous êtes sur Android 6.0, veuillez redémarrer l'app.")
                            .negativeText("Fermer")
                            .show();
                }

            } else {
                Toast.makeText(context, "Erreur lors du traitement de l'image.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        dialogInterface = (ContactInterface) getActivity();
    }
}
