package com.lumi_dos.lge;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.analytics.GoogleAnalytics;


public class MainActivity extends ActionBarActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get a Tracker (should auto-report)
        //((LGE) getApplication()).getTracker(LGE.TrackerName.APP_TRACKER);
    }

    /*public void onStart() {
        super.onStart();
        //Get an Analytics tracker to report app starts & uncaught exceptions etc.
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }*/

    /*public void onStop() {
        super.onStop();
        //Stop the analytics tracking
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void startIntranet(View view) {
        Intent intent = new Intent(this, IntranetActivity.class);
        startActivity(intent);
    }

    public void startNews(View view) {
        Intent intent = new Intent(this, NewsActivity.class);
        startActivity(intent);
    }

    public void startContact(View view) {
        Intent intent = new Intent(this, ContactActivity.class);
        startActivity(intent);
    }

    public void startWebsite(View view) {
        Uri uri = Uri.parse("http://www.lge.lu");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}