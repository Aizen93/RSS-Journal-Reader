package com.example.oussama_achraf.mplrss;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class WebVuePlayer extends AppCompatActivity {

    private WebView webpage = null;
    private String link = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_vue_player);

        webpage = findViewById(R.id.webPage);
        Intent intent = getIntent();
        link = intent.getStringExtra("link");
        webpage.loadUrl(link);
    }
}
