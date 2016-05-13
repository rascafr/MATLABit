package fr.rascafr.matlabit.utils;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rascafr on 31/07/2015.
 * Use the class to connect to network (in AsyncTask)
 */
public class ConnexionUtils {

    private static final String LOG_KEY_ERROR = "ConnexionUtils.ERROR";

    // Send data with POST method, and returns the server's response
    // V2.0 : no more '\n' char
    // V3.0 : android 6.0 support
    public static String postServerData(String sUrl, HashMap<String, String> postDataParams, Context ctx) {

        String result = "";
        URL url = null;

        if (Utilities.isOnline(ctx)) {
            try {
                url = new URL(sUrl);
                try {

                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setReadTimeout(10000);
                    httpURLConnection.setConnectTimeout(15000);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);

                    if (postDataParams != null) {
                        OutputStream os = httpURLConnection.getOutputStream();
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                        writer.write(getPostDataString(postDataParams));

                        writer.flush();
                        writer.close();
                        os.close();
                    }
                    int responseCode = httpURLConnection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        String line;
                        BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                        while ((line = br.readLine()) != null) {
                            result += line;
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    private static String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    public static APIResponse getAPIResponse (String sUrl, HashMap<String, String> postDataParams, Context ctx) {
        String sData = postServerData(sUrl, postDataParams, ctx);
        APIResponse resp = new APIResponse();

        if (Utilities.isNetworkDataValid(sData)) {
            try {
                JSONObject jData = new JSONObject(sData);
                resp.setStatus(jData.getInt("status"));
                resp.setErrorMsg(jData.optString("cause"));
                resp.setJsonData(jData.optJSONObject("data"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return resp;
    }

    // Send data image with POST method, and returns the server's response
    // V2.0 : no more '\n' char
    // V3.0 : android 6.0 support
    public static String postRawServerData(String sUrl, HashMap<String, String> postDataParams, byte[] bImage, Context ctx) {

        String result = "";
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        URL url = null;
        String boundaryCst = "----------V2y2HZDDDFg03epSStjsqdbaKO0j1";

        if (Utilities.isOnline(ctx)) {
            try {
                url = new URL(sUrl);
                try {

                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.addRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundaryCst); // let contentType
                    httpURLConnection.setReadTimeout(20000);
                    httpURLConnection.setConnectTimeout(15000);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);

                    if (postDataParams != null) {

                        OutputStream os = httpURLConnection.getOutputStream();

                        // Début préparation envoi

                        // ajout des paramètres POST normaux (client, pass, hash)
                        for(Map.Entry<String, String> entry : postDataParams.entrySet()){
                            os.write((twoHyphens + boundaryCst + lineEnd).getBytes());
                            os.write(("Content-Disposition: form-data; name=\"" + URLEncoder.encode(entry.getKey(), "UTF-8") + "\"" + lineEnd + lineEnd).getBytes());
                            os.write((URLEncoder.encode(entry.getValue(), "UTF-8") + lineEnd).getBytes());
                        }

                        // ajout de l'image
                        os.write((twoHyphens + boundaryCst + lineEnd).getBytes());
                        os.write(("Content-Disposition: form-data; name=\"file\"; filename=\"image.jpg\"" + lineEnd).getBytes());
                        os.write(("Content-Type: image/jpeg" + lineEnd + lineEnd).getBytes());
                        os.write(bImage);
                        os.write(lineEnd.getBytes());

                        // on termine
                        os.write((twoHyphens + boundaryCst + twoHyphens + lineEnd).getBytes());

                        // on envoie la requete
                        os.close();
                    }
                    int responseCode = httpURLConnection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        String line;
                        BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                        while ((line = br.readLine()) != null) {
                            result += line;
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}
