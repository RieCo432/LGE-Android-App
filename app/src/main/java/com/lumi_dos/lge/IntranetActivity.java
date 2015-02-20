package com.lumi_dos.lge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.google.android.gms.analytics.GoogleAnalytics;


public class IntranetActivity extends ActionBarActivity {

    int ICONS[] = {R.drawable.home, R.drawable.intranet, R.drawable.news, R.drawable.contact, R.drawable.about, R.drawable.home};

    String NAME = "LGE";
    String EMAIL = "secretariat@lge.lu";
    int PROFILE = R.drawable.ic_launcher;

    private Toolbar toolbar;

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    DrawerLayout Drawer;

    ActionBarDrawerToggle mDrawerToggle;

    public int currentSlideNumber = 1;
    public String slide_url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intranet);

        //Get a Tracker (should auto-report)
        ((LGE) getApplication()).getTracker(LGE.TrackerName.APP_TRACKER);

        toolbar =(Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        String WELCOME = getString(R.string.welcome);
        String INTRANET = getString(R.string.intranet);
        String NEWS = getString(R.string.news);
        String CONTACT = getString(R.string.contact);
        String ABOUT = getString(R.string.action_about);
        String OFFICIAL_WEBSITE = getString(R.string.official_website);

        String TITLES[] = {WELCOME, INTRANET, NEWS, CONTACT, ABOUT, OFFICIAL_WEBSITE};

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MyAdapter(TITLES, ICONS, NAME, EMAIL, PROFILE);

        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //code here will be executed when drawer gets opened
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //code here gets executed  when drawer gets closed
            }
        };

        Drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        final WebView slideView = (WebView) findViewById(R.id.slideView);
        slideView.setVisibility(View.INVISIBLE);
        slideView.clearCache(true);
        slideView.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.INVISIBLE);
                slideView.setVisibility(View.VISIBLE);
            }
        });
        WebSettings webSettings = slideView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //Keep for later, maybe it may be necessary to disable caching
        /*webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setAppCacheEnabled(false);*/

        slide_url = constructURL(currentSlideNumber);
        slideView.loadUrl(slide_url);
    }

    public void onStart() {
        super.onStart();
        //Get an Analytics tracker to report app starts & uncaught exceptions etc.
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    public void onStop() {
        super.onStop();
        //Stop the analytics tracking
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
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

    public String constructURL(int slideNumber) {
        return getString(R.string.base_intranet_url) + slideNumber;
    }

    public void slideBack(View view) {
        if(currentSlideNumber > 1) {
            currentSlideNumber--;
            initializeLoadingSequence(view);
            slide_url = constructURL(currentSlideNumber);
            WebView slideView = (WebView) findViewById(R.id.slideView);
            slideView.loadUrl(slide_url);
        }
    }

    public void slideForward(View view) {
        currentSlideNumber++;
        initializeLoadingSequence(view);
        slide_url = constructURL(currentSlideNumber);
        WebView slideView = (WebView) findViewById(R.id.slideView);
        slideView.loadUrl(slide_url);
    }

    public void initializeLoadingSequence(View view) {
        WebView slideView = (WebView) findViewById(R.id.slideView);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        slideView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }
}