package com.devnouh.myapplication;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonParser extends AsyncTask<String, Void, ArrayList<Photo>> implements DownloadJson.OnDownloadComplete {
    private static final String TAG = "JsonParser";

    interface OnDataAvailable {
        void onDataAvailable(ArrayList<Photo> photos);
    }

    private OnDataAvailable mDataAvailable;
    private ArrayList<Photo> mPhotos;


    JsonParser(OnDataAvailable dataAvailable) {
        mDataAvailable = dataAvailable;
        mPhotos = new ArrayList<>();
    }

    @Override
    protected ArrayList<Photo> doInBackground(String... strings) {
        if (strings[0].length() > 150) {
            onDownloadComplete(strings[0]);
            return mPhotos;

        } else {
            DownloadJson json = new DownloadJson(this);
            json.doInSameThread(strings[0]);
        }
        Log.d(TAG, "doInBackground:  size <******> "+mPhotos.size());
        return mPhotos;
    }

    @Override
    protected void onPostExecute(ArrayList<Photo> photos) {
        Log.d(TAG, "onPostExecute: size " + photos.size());
        mDataAvailable.onDataAvailable(photos);
    }

    @Override
    public void onDownloadComplete(String data) {
        Log.d(TAG, "onDownloadComplete: " + data);
        Photo mPhoto;
        try {
            JSONObject json = new JSONObject(data);
            JSONObject object = json.getJSONObject("photos");
            JSONArray arrObject = object.getJSONArray("photo");

            for (int i = 0; i < arrObject.length(); i++) {

                JSONObject photoDetailes = arrObject.getJSONObject(i);


                String title = photoDetailes.getString("title");
                String author = photoDetailes.getString("owner");
                String author_ID = photoDetailes.getString("id");
                //String tags = photoDetailes.getString("tags");

                String link = photoDetailes.getString("url_s");
                String image = link.replace("_m", "_b");

                mPhoto = new Photo(title, link, author, author_ID, image);
                mPhotos.add(mPhoto);

            }
          //  mDataAvailable.onDataAvailable(mPhotos);
            Log.d(TAG, "onDownloadComplete: size -----> " + mPhotos.size());
        } catch (Exception e) {
            Log.d(TAG, "onDownloadComplete: " + e.getMessage());
        }

    }
}


