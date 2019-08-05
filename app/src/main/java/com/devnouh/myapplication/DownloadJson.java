package com.devnouh.myapplication;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class DownloadJson extends AsyncTask<String, Void, String> {
    private static final String TAG = "DownloadJson";
    static String lastJsonFile;
    private static final String url = "https://api.flickr.com/services/rest?method=flickr.photos.getrecent&api_key=5d884488db539502d0b2107f197f80d8&extras=url_s";

    interface OnDownloadComplete {
        void onDownloadComplete(String data);
    }

    private OnDownloadComplete mDownloadComplete;

    DownloadJson(OnDownloadComplete downloadComplete) {
        mDownloadComplete = downloadComplete;
    }

    @Override
    protected String doInBackground(String... strings) {
        URL url;
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            url = new URL(getUrl(strings[0]));
            Log.d(TAG, "doInBackground: URL : " + getUrl(strings[0]));
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int respondCode = connection.getResponseCode();
            Log.d(TAG, "doInBackground: connect with respondcode " + respondCode);
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                stringBuilder.append(line).append("\n");
            }

            return stringBuilder.toString();

        } catch (Exception e) {
            Log.d(TAG, "doInBackground: " + e.getMessage());
        } finally {
            if (connection != null)
                connection.disconnect();
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    Log.d(TAG, "doInBackground: reader " + e.getMessage());
                }
            }
        }
        return null;
    }

    void doInSameThread(String url) {
        onPostExecute(doInBackground(url));
    }

    @Override
    protected void onPostExecute(String s) {
        mDownloadComplete.onDownloadComplete(s);
        lastJsonFile = s;
    }

    private String getUrl(String search) {
        return Uri.parse(url).buildUpon()
                .appendQueryParameter("tag", search)
                .appendQueryParameter("content_type", "7")
                .appendQueryParameter("format", "json")
                .appendQueryParameter("nojsoncallback", "1").toString();
    }

}
