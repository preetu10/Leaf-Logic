package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class AboutActivity extends AppCompatActivity {

    private WebView webview;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        webview=(WebView) findViewById(R.id.webvideo);

        String htmlContent = "<html><head><link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_res/raw/styles.css\" /></head><body><iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/yp58geKmdYE?si=fhdLEU91ZtjKNJ4v\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></body></html>";
       // String video="<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/yp58geKmdYE?si=fhdLEU91ZtjKNJ4v\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
        webview.loadDataWithBaseURL(null,htmlContent,"text/html","utf-8",null);

        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebChromeClient(new WebChromeClient());


        Fragment fragment= new Map_Fragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.location,fragment).commit();
    }
}