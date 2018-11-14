package edu.temple.webbrowser;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

public class WebFragment extends Fragment {

    View v;
    WebView webView;
    Context context;
    ImageButton prevPageButton;
    ImageButton nextPageButton;
    String URL;

    public static final String HOME_PAGE = "https://www.google.com";
    public static String ARG_PARAM1 = "web_key";
    public static String ARG_PARAM2 = "url_key";

    public WebFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            URL = bundle.getString(ARG_PARAM1);
        }
    }

    public static WebFragment newInstance(String URL) {
        WebFragment webFragment = new WebFragment();
        Bundle bundle = new Bundle();
        bundle.putString(WebFragment.ARG_PARAM1, URL);
        webFragment.setArguments(bundle);
        return webFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_web, container, false);

        prevPageButton = v.findViewById(R.id.prevPageButton);
        nextPageButton = v.findViewById(R.id.nextPageButton);

        webView = v.findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClientHandler());

        webView.getSettings().setJavaScriptEnabled(true);

        if (savedInstanceState != null) {
            URL = savedInstanceState.getString(ARG_PARAM2);
        }

        //loadURL(HOME_PAGE);
        getEditTextUrl();

        prevPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) webView.goBack();
            }
        });
        nextPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoForward()) webView.goForward();
            }
        });

        return v;
    }

    public void loadURL(String url) {
        URL = url;
        getEditTextUrl();
    }

    private void getEditTextUrl() {
        webView.loadUrl(URL);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putString(ARG_PARAM2, URL);
        super.onSaveInstanceState(bundle);
    }

    private class WebViewClientHandler extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            ((GetURL) context).getURL(url, view);
            URL = url;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    interface GetURL {
        void getURL(String URL, WebView view);
    }
}