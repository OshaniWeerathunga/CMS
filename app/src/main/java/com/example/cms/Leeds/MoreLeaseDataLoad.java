package com.example.cms.Leeds;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.cms.R;

public class MoreLeaseDataLoad extends AppCompatActivity {

    WebView webView;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more__lease_data_load);

        id = getIntent().getStringExtra("id");

        webView = findViewById(R.id.moredataWebview);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://cms.fintrex.lk/leasing?id="+id);

    }
}