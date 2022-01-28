package com.example.cms.Leeds;

import static com.example.cms.HelperClass.PostRequest.cookies;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sanction_letter_display);

        id = getIntent().getStringExtra("userid");
        System.out.println("user id is --- "+id);
        url = "http://192.168.40.7:8080/cms/view-lease?id="+id;

        StringBuilder stringBuilder = new StringBuilder();
        for (String str : cookies){
            stringBuilder.append(str);
            stringBuilder.append(",");
        }
        String str = stringBuilder.toString();

        System.out.println("str value is --- "+str);

        webView = findViewById(R.id.letterWebview);
        final WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        webView.setWebViewClient(new WebViewClient());

        //send cookies with url
        CookieSyncManager.createInstance(SanctionLetterDisplay.this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeSessionCookie();
        String cookieString = str;
        cookieManager.setCookie(url,cookieString);
        CookieSyncManager.getInstance().sync();

        Map<String,String> abc = new HashMap<String,String>();
        abc.put("Cookie",cookieString);

        webView.loadUrl(url,abc);


    }
}