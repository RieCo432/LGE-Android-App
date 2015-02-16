package com.lumi_dos.lge;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.google.android.gms.analytics.GoogleAnalytics;


public class NewsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        final WebView newsView = (WebView) findViewById(R.id.news_view);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        newsView.setWebViewClient(new WebViewClient());
        newsView.clearCache(true);
        newsView.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.INVISIBLE);
                newsView.setVisibility(View.VISIBLE);
            }
        });
        WebSettings newsViewSettings = newsView.getSettings();
        newsViewSettings.setJavaScriptEnabled(true);
        newsViewSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        newsViewSettings.setAppCacheEnabled(false);
        newsView.loadUrl(getString(R.string.base_calendar_url));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.global, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
