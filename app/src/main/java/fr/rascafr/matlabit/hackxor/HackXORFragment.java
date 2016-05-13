package fr.rascafr.matlabit.hackxor;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import fr.rascafr.matlabit.R;
import fr.rascafr.matlabit.utils.RegexUtils;

/**
 * Created by root on 13/04/16.
 */
public class HackXORFragment extends Fragment {

    // Layout
    private EditText etKey, etMessage;
    private TextView tvResult;
    private TextView bpEncode, bpDecode;

    // Vibrator
    private Vibrator vibrator;

    // Android
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_hackxor, container, false);
        context = getActivity();

        // Find views
        etKey = (EditText) rootView.findViewById(R.id.etHackXORKey);
        etMessage = (EditText) rootView.findViewById(R.id.etHackXORMessage);
        tvResult = (TextView) rootView.findViewById(R.id.tvHackXORResult);
        bpEncode = (TextView) rootView.findViewById(R.id.bpHackXOREncode);
        bpDecode = (TextView) rootView.findViewById(R.id.bpHackXORDecode);

        // Create vibrator object
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        // Encode action
        bpEncode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String toEnc = etMessage.getText().toString().trim();
                String key = etKey.getText().toString();

                if (toEnc.length() != 0 && key.length() != 0) {
                    tvResult.setText(encodeXOR(toEnc, key));
                } else {
                    Toast.makeText(context, "Un des champ n'est pas rempli, le message ne peut pas être encrypté", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Decode action
        bpDecode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String toDec = etMessage.getText().toString().trim();
                String key = etKey.getText().toString();

                if (toDec.length() != 0 && key.length() != 0 && RegexUtils.regexValidation(toDec, RegexUtils.REGEX_HEXA_16)) {
                    tvResult.setText(decodeXOR(toDec, etKey.getText().toString()).trim());
                } else {
                    Toast.makeText(context, "Un des champ n'est pas rempli, le message ne peut pas être décrypté", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Share action
        tvResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = tvResult.getText().toString();

                if (result.length() != 0) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, result);
                    startActivity(Intent.createChooser(intent, "Partager ..."));
                } else {
                    Toast.makeText(context, "T'es saoul ? Impossible de partager un message vide !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Copy action
        tvResult.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                // Vibrate
                vibrator.vibrate(35);

                // Notify the user the text has been copied
                Toast.makeText(context, "Message copié", Toast.LENGTH_SHORT).show();

                // Copy text
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(
                        "MATLABit-crypt",
                        tvResult.getText().toString()
                );
                clipboard.setPrimaryClip(clip);

                return true;
            }
        });

        return rootView;
    }

    public static HackXORFragment newInstance() {
        return new HackXORFragment();
    }

    private String encodeXOR(String message, String key) {
        String result = "";

        int j = 0;

        for (int i = 0; i < message.length(); i++) {
            result += String.format("%04X", message.charAt(i) ^ key.charAt(j));
            j++;

            if (j == key.length()) j = 0;
        }

        return result;
    }

    private String decodeXOR(String data, String key) {
        String result = "";

        int j = 0;

        for (int i = 0; i < data.length(); i += 4) {
            result += Character.toString((char) (((char) Integer.parseInt(data.substring(i, i + 4), 16)) ^ key.charAt(j)));
            j++;

            if (j == key.length()) j = 0;
        }

        return result;
    }
}
