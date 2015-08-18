package com.lumi_dos.lge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;


public class TimetablesActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    String NAME = "LGE";
    String EMAIL = "secretariat@lge.lu";
    int PROFILE = R.drawable.profile;

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    DrawerLayout Drawer;

    ActionBarDrawerToggle mDrawerToggle;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetables);

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

        final GestureDetector mGestureDetector = new GestureDetector(TimetablesActivity.this, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
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

        Spinner class_selector = (Spinner) findViewById(R.id.class_selector);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.classes, R.layout.spinner_item);

        adapter.setDropDownViewResource(R.layout.spinner_item);

        class_selector.setAdapter(adapter);

        class_selector.setOnItemSelectedListener(this);

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(position != 0) {

            String position_string = Integer.toString(position);

            if(position_string.length() < 2) {
                position_string = "0" + position_string;
            }

            String timetable_url = getString(R.string.timetable_address_prefix) + position_string + getString(R.string.timetable_address_suffix);

            WebView timetable_webview = (WebView) findViewById(R.id.timetable_webview);
            WebSettings timetable_settings = timetable_webview.getSettings();
            timetable_settings.setSupportZoom(true);
            timetable_settings.setBuiltInZoomControls(true);
            timetable_webview.setInitialScale(150);

            timetable_webview.loadUrl(timetable_url);

        } else {
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.select_class), Toast.LENGTH_LONG);
            toast.show();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

        Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.select_class), Toast.LENGTH_SHORT);
        toast.show();
    }
}