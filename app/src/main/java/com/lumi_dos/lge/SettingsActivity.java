package com.lumi_dos.lge;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.CheckBox;

import com.google.android.gms.analytics.GoogleAnalytics;


public class SettingsActivity extends AppCompatActivity {

    String NAME = "LGE";
    String EMAIL = "secretariat@lge.lu";
    int PROFILE = R.drawable.profile;

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    DrawerLayout Drawer;

    ActionBarDrawerToggle mDrawerToggle;

    public SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if(sharedPreferences.getBoolean(Preferences.GENERAL_TOPIC_SUBSCRIBED, true)) {
            CheckBox generalTopicSubscribed = (CheckBox) findViewById(R.id.subscibeToGeneralTopic);
            generalTopicSubscribed.setChecked(true);
        }



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

        final GestureDetector mGestureDetector = new GestureDetector(SettingsActivity.this, new GestureDetector.SimpleOnGestureListener() {

            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });


        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    Drawer.closeDrawers();

                    int itemClicked = recyclerView.getChildPosition(child);

                    Intent intent = LGE.startActivityOnNavDrawerCAll(itemClicked, getApplicationContext(), getString(R.string.feedback_address), getString(R.string.feedback_subject), getString(R.string.feedback_subject), getString(R.string.choose_email_client));

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
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onCheckBoxClicked (View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()) {
            case R.id.subscibeToGeneralTopic:
                if (checked) {
                    sharedPreferences.edit().putBoolean(Preferences.GENERAL_TOPIC_SUBSCRIBED, true).apply();
                } else {
                    sharedPreferences.edit().putBoolean(Preferences.GENERAL_TOPIC_SUBSCRIBED, false).apply();
                }
                sharedPreferences.edit().putBoolean(Preferences.SUBSCRIBED_TOPICS_LIST_CHANGED, true).apply();

        }
    }

    public void restart(View view) {
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage( getBaseContext().getPackageName() );
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}
