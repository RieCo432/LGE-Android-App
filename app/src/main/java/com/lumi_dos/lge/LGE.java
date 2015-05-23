package com.lumi_dos.lge;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;

public class LGE extends Application {

    public static int ICONS[] = {R.drawable.home, R.drawable.intranet, R.drawable.news, R.drawable.contact, R.drawable.about, R.drawable.website, R.drawable.feedback};

    public static Intent startActivityOnNavDrawerCAll(int childNumber, Context context, String feedback_address, String feedback_subject, String feedback_message, String choose_email_client) {
        if(childNumber == 1){
            return new Intent(context, MainActivity.class);
        } else if(childNumber == 2) {
            return new Intent(context, IntranetActivity.class);
        } else if(childNumber == 3) {
            return new Intent(context, NewsActivity.class);
        } else if(childNumber == 4) {
            return new Intent(context, ContactActivity.class);
        } else if(childNumber == 5) {
            return new Intent(context, AboutActivity.class);
        } else if(childNumber == 6) {
            Uri uri = Uri.parse("http://www.lge.lu");
            return new Intent(Intent.ACTION_VIEW, uri);
        } else if(childNumber == 7) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", feedback_address, null));

            intent.putExtra(Intent.EXTRA_SUBJECT, feedback_subject);
            intent.putExtra(Intent.EXTRA_TEXT, feedback_message);
            Intent.createChooser(intent, choose_email_client);

            return intent;
        }

        return null;
    }

    // The following line should be changed to include the correct property id.
    private static final String PROPERTY_ID = "UA-47100626-5";

    //Logging TAG
    private static final String TAG = "LGE";

    public static int GENERAL_TRACKER = 0;

    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
        ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
    }

    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    public LGE() {
        super();
    }

    synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(R.xml.app_tracker)
                    : (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(PROPERTY_ID)
                    : analytics.newTracker(R.xml.ecommerce_tracker);
            mTrackers.put(trackerId, t);

        }
        return mTrackers.get(trackerId);
    }
}