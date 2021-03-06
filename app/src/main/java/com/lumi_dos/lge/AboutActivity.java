package com.lumi_dos.lge;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;

import java.util.ArrayList;
import java.util.HashMap;


public class AboutActivity extends AppCompatActivity {

    String NAME = "LGE";
    String EMAIL = "secretariat@lge.lu";
    int PROFILE = R.drawable.profile;


    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    DrawerLayout Drawer;

    ActionBarDrawerToggle mDrawerToggle;

    public static int tapCounter = 0;

    public SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //Get a Tracker (should auto-report)
        ((LGE) getApplication()).getTracker(LGE.TrackerName.APP_TRACKER);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String TITLES[];
        int ICONS[];

        if(!sharedPreferences.getBoolean(Preferences.DEVELOPER_MODE, false)){
            TITLES = getResources().getStringArray(R.array.nav_drawer_titles);
            ICONS = LGE.ICONS;
        } else {
            TITLES = getResources().getStringArray(R.array.nav_drawer_titles_dev);
            ICONS = LGE.ICONS_DEV;
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MyAdapter(TITLES, ICONS, NAME, EMAIL, PROFILE, this);

        mRecyclerView.setAdapter(mAdapter);

        final GestureDetector mGestureDetector = new GestureDetector(AboutActivity.this, new GestureDetector.SimpleOnGestureListener() {

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

                    if(intent!=null) {
                        startActivity(intent);
                    }

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

        String versionName = BuildConfig.VERSION_NAME;
        String versionCode = Integer.toString(BuildConfig.VERSION_CODE);
        String versionDate = BuildConfig.versionDate;

        String[][] listData =
                {{getString(R.string.app_name), getString(R.string.app_banner)},
                        {getString(R.string.version), versionName},
                        {getString(R.string.build), versionCode},
                        {getString(R.string.date), versionDate},
                        {getString(R.string.developer_label), getString(R.string.app_autor)},
                        {getString(R.string.development_assistance), getString(R.string.development_assistance_name)},
                        {getString(R.string.design_assistance), getString(R.string.design_assistance_name)},
                        {getString(R.string.copyright), getString(R.string.credits_copyright)}
        };

        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();

        final ListView aboutView = (ListView) findViewById(R.id.aboutAppList);

        HashMap<String,String> item;
        for (String[] aListData : listData) {
            item = new HashMap<String, String>();
            item.put("line1", aListData[0]);
            item.put("line2", aListData[1]);
            list.add(item);
        }

        ListAdapter myListAdapter = new SimpleAdapter(this, list,
                android.R.layout.simple_list_item_2,
                new String[] { "line1","line2" },
                new int[] {android.R.id.text1, android.R.id.text2});
        aboutView.setAdapter(myListAdapter);

        aboutView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==2) {
                    if(!sharedPreferences.getBoolean(Preferences.DEVELOPER_MODE, false)) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Developer Mode activated!", Toast.LENGTH_SHORT);
                        toast.show();
                        sharedPreferences.edit().putBoolean(Preferences.DEVELOPER_MODE, true).apply();
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Developer Mode deactivated!", Toast.LENGTH_SHORT);
                        toast.show();
                        sharedPreferences.edit().putBoolean(Preferences.DEVELOPER_MODE, false).apply();
                    }
                }
                return false;
            }
        });
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
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_feedback) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", getString(R.string.feedback_address), null));

            String subject = getString(R.string.feedback_subject);
            String message = getString(R.string.feedback_message);

            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(Intent.createChooser(intent, getString(R.string.choose_email_client)));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}