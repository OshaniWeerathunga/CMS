package com.example.cms.Leeds;

import static com.example.cms.HelperClass.PostRequest.cookies;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.cms.R;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SanctionLetterDisplay extends AppCompatActivity {

    WebView webView;
    String id;
    String url;
    String stringcookie;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sanction_letter_display);

        //get user ID
        id = getIntent().getStringExtra("userid");

        //create url by adding ID
        url = "http://cms.fintrex.lk/view-lease?id=" + id;
        //url = "http://192.168.40.7:8080/cms/cusfile/cus_35_3_file";


/*
        //set cookies as a string value
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : cookies) {
            stringBuilder.append(str);
            stringBuilder.append(",");
        }
        String str = stringBuilder.toString();


        //send cookies with url
        CookieSyncManager.createInstance(SanctionLetterDisplay.this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeSessionCookie();
        String cookieString = str;
        cookieManager.setCookie(url, cookieString);
        CookieSyncManager.getInstance().sync();

        Map<String, String> abc = new HashMap<String, String>();
        abc.put("Cookie", cookieString);


        //web view set
        webView = findViewById(R.id.letterWebview);
        final WebSettings settings = webView.getSettings();
        settings.setSupportMultipleWindows(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        webView.setWebViewClient(new MyBrowser());

        webView.loadUrl(url, abc);

 */

        //set cookies as a string value
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : cookies) {
            stringBuilder.append(str);
            stringBuilder.append(",");
        }
        String str = stringBuilder.toString();


        //send cookies with url
        CookieSyncManager.createInstance(SanctionLetterDisplay.this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeSessionCookie();
        String cookieString = str;
        cookieManager.setCookie(url, cookieString);
        CookieSyncManager.getInstance().sync();

        Map<String, String> abc = new HashMap<String, String>();
        abc.put("Cookie", cookieString);


        webView = findViewById(R.id.letterWebview);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url,abc);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    private class MyBrowser extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}