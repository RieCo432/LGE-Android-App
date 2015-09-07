package com.lumi_dos.lge;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by Colin on 23/08/2015.
 */

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    
    protected Bitmap doInBackground(String... url) {
        return loadImageFromNetwork(url[0]);
    }


    private Bitmap loadImageFromNetwork(String url) {
        try {
            return BitmapFactory
                    .decodeStream((InputStream) new URL(url)
                            .getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPreExecute() {
        IntranetActivity.progressBar.setVisibility(View.VISIBLE);
    }

    protected void onPostExecute(Bitmap result) {
        IntranetActivity.slideView.setImageBitmap(result);
        IntranetActivity.progressBar.setVisibility(View.INVISIBLE);
    }

    /*protected Void doInBackground(Void... params) {
        for (IntranetActivity.loadingSlideNumber=1; IntranetActivity.loadingSlideNumber < lastSlide; IntranetActivity.loadingSlideNumber++) {
            //if(IntranetActivity.slides[IntranetActivity.loadingSlideNumber]==null) {
                IntranetActivity.slides[IntranetActivity.loadingSlideNumber] = loadImageFromNetwork("http://www.lge.lu/lgeapp/intranet/2006/1996/Slide"+IntranetActivity.loadingSlideNumber+".JPG");

        }
        return null;
    }

    private Bitmap loadImageFromNetwork(String url) {
        try {
            return BitmapFactory
                    .decodeStream((InputStream) new URL(url)
                            .getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/

}
