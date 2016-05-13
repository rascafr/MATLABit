package fr.rascafr.matlabit.news;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import fr.rascafr.matlabit.R;
import fr.rascafr.matlabit.app.DataStorage;

/**
 * Created by root on 13/04/16.
 */
public class NewsFragment extends Fragment {

    // Layout
    private WebView webView;

    // Android
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_webview, container, false);
        context = getActivity();

        // Find views
        webView = (WebView) rootView.findViewById(R.id.webViewNews);

        // Open url
        webView.loadUrl(DataStorage.getInstance().getSplfb());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        return rootView;
    }

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }
}
