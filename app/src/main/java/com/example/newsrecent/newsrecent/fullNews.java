package com.example.newsrecent.newsrecent;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URI;
import java.net.URISyntaxException;

public class fullNews extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_news);

        WebView browser = (WebView) findViewById(R.id.webview);
        browser.setWebViewClient(new WebViewClient());
        String uri = getIntent().getStringExtra("CLickedNewsText");
        try {
            URI myUri = new URI(uri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        browser.loadUrl(uri);
    }
}
