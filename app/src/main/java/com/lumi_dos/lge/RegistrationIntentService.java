/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lumi_dos.lge;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.ProgressBar;

import com.lumi_dos.lge.MainActivity;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

public class RegistrationIntentService extends IntentService {

    public String token;

    private static final String TAG = "GCM";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            synchronized (TAG) {

                InstanceID instanceID = InstanceID.getInstance(this);
                token = instanceID.getToken(getString(R.string.sender_id),
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                Log.i(TAG, "GCM Registration Token: " + token);

                sharedPreferences.edit().putString(Preferences.GCM_TOKEN, token).apply();

                sharedPreferences.edit().putBoolean(Preferences.DEVICE_REGISTERED, true).apply();
            }
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            sharedPreferences.edit().putBoolean(Preferences.DEVICE_REGISTERED, false).apply();
        }
        Intent registrationComplete = new Intent(Preferences.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);

        if(sharedPreferences.getBoolean(Preferences.SUBSCRIBED_TOPICS_LIST_CHANGED, true)) {
            if(sharedPreferences.getBoolean(Preferences.GENERAL_TOPIC_SUBSCRIBED, true)) {
                try {
                    subscribeTopics(token, getString(R.string.generalTopic));
                    Log.i("GCM", "Subscribed");
                    sharedPreferences.edit().putBoolean(Preferences.SUBSCRIBED_TOPICS_LIST_CHANGED, false).apply();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("GCM", "Error subscribing");
                }
            } else {
                try {
                    unsubscribeTopics(token, getString(R.string.generalTopic));
                    Log.i("GCM", "Unubscribed");
                    sharedPreferences.edit().putBoolean(Preferences.SUBSCRIBED_TOPICS_LIST_CHANGED, false);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("GCM", "Error unubscribing");
                }
            }
        }
    }



    public Context context = this;

    public void subscribeTopics(String token, String topic) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(context);
        pubSub.subscribe(token, topic, null);
        Log.i(TAG, "Topic " + topic + " subscibed");
    }

    public void unsubscribeTopics(String token, String topic) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(context);
        pubSub.unsubscribe(token, topic);
        Log.i(TAG, "Topic " + topic + " unsubscibed");
    }

}
