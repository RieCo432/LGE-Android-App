package com.lumi_dos.lge;

import android.app.Activity;

import com.google.android.gms.gcm.GcmPubSub;
import com.lumi_dos.lge.LGE;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    String NAME = "LGE";
    String EMAIL = "secretariat@lge.lu";
    int PROFILE = R.drawable.profile;

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    DrawerLayout Drawer;

    ActionBarDrawerToggle mDrawerToggle;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private GoogleCloudMessaging gcm = null;
    private String SENDER_ID = "953167740477";
    public String regid;

    public SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        //Get a Tracker (should auto-report)
        ((LGE) getApplication()).getTracker(LGE.TrackerName.APP_TRACKER);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);






        String TITLES[] = getResources().getStringArray(R.array.nav_drawer_titles);
        int ICONS[] = LGE.ICONS;

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MyAdapter(TITLES, ICONS, NAME, EMAIL, PROFILE, this);

        mRecyclerView.setAdapter(mAdapter);

        final GestureDetector mGestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {

            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });


        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(),motionEvent.getY());

                if(child!=null && mGestureDetector.onTouchEvent(motionEvent)){
                    Drawer.closeDrawers();

                    int itemClicked = recyclerView.getChildPosition(child);

                    Intent intent = LGE.startActivityOnNavDrawerCAll(itemClicked,getApplicationContext(), getString(R.string.feedback_address), getString(R.string.feedback_subject), getString(R.string.feedback_subject), getString(R.string.choose_email_client));

                    startActivity(intent);

                    return true;

                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

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

        if(!prefs.getBoolean("com.lumi_dos.lge.gcm_registered", false)) {

            if (checkPlayServices(this)) {
                getRegistrationId(this);
                Log.i("GCM", "registered!");
            }
        }

        if (!prefs.getBoolean("com.lumi_dos.lge.gcm_general_topic_registered", false)) {
            String regToken = prefs.getString("com.lumi_dos.lge.gcm_id", null);
            Log.i("GCM", "regToken: "+regToken);
            subscribeToTopic("/topics/general");

        }
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

    public void startIntranet(View view) {
        Intent intent = new Intent(this, IntranetActivity.class);
        startActivity(intent);
    }

    public void startTimetable(View view) {
        Intent intent = new Intent(this, TimetablesActivity.class);
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

    public static boolean checkPlayServices(Context context) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity) context,PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("Developer", "This device is not supported.");
            }
            return false;
        }
        return true;
    }



    public void getRegistrationId(final Context context){
        new AsyncTask<Void, Void, Void>(){

            private String msg;

            @Override
            protected Void doInBackground(Void... arg0) {

                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }

                try {
                    Log.i("Sender", SENDER_ID);

                    regid = gcm.register(SENDER_ID);

                    msg = "" + regid;


                } catch (IOException e) {
                    e.printStackTrace();

                }

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {

                Log.i("Developer", msg);
                /*Toast.makeText(context, msg, Toast.LENGTH_LONG).show();*/
                prefs.edit().putString("com.lumi_dos.lge.gcm_id", msg).apply();
                prefs.edit().putBoolean("com.lumi_dos.lge.gcm_registered", true).apply();
            }
        }.execute();
    }


    //TODO: Get this fucking shit working
    public void subscribeToTopic(final String topic) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String regToken = prefs.getString("com.lumi_dos.lge.gcm_id", null);
                try {
                    assert regToken != null;
                    Log.i("GCM", "Subscribing");
                    GcmPubSub.getInstance(getApplicationContext()).subscribe(regToken, "/topics/general", null);
                    Log.i("GCM", "subscribed");
                    prefs.edit().putBoolean("com.lumi_dos.lge.gcm_general_topic_registered", true).apply();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}