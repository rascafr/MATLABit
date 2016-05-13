package fr.rascafr.matlabit.profile;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import fr.rascafr.matlabit.R;
import fr.rascafr.matlabit.app.DataStorage;
import fr.rascafr.matlabit.async.AsyncConnectUser;
import fr.rascafr.matlabit.interfaces.ContactInterface;
import fr.rascafr.matlabit.interfaces.OnProfileChange;

/**
 * Created by root on 14/04/16.
 */
public class FragmentProfileCreate extends Fragment {

    // Android
    private Context context;

    // Layout
    private EditText etLogin, etPassword;
    private Button bpLogin;

    // Interfaces
    private OnProfileChange onProfileChange;
    private ContactInterface dialogInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile_create, container, false);
        context = getActivity();

        // Find views
        etLogin = (EditText) rootView.findViewById(R.id.etLoginIdentifier);
        etPassword = (EditText) rootView.findViewById(R.id.etLoginPassword);
        bpLogin = (Button) rootView.findViewById(R.id.bpLoginAction);

        // Connect button listener
        bpLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // get / clean fields
                String login = etLogin.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                etLogin.setText(login);
                etPassword.setText(password);

                // Send request, start Async Task
                new AsyncConnectUser(context, DataStorage.getInstance().getSPL_API_CONNECT(), login, password, dialogInterface, getActivity()).execute();
            }
        });

        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        onProfileChange = (OnProfileChange) getActivity();
        dialogInterface = (ContactInterface) getActivity();
    }


    public static FragmentProfileCreate newInstance() {
        return new FragmentProfileCreate();
    }
}
