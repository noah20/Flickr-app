package com.devnouh.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.devnouh.myapplication.DownloadJson.lastJsonFile;

public class MainActivity extends AppCompatActivity implements JsonParser.OnDataAvailable, AdapterView.OnItemClickListener
        , AdapterView.OnItemLongClickListener {
    private static final String TAG = "MainActivity";

    GridView lv;
    SharedPreferences shared;
    ArrayList<Photo> listOfPhotos;
    ArrayList<Photo> subList;
    ListViewAdapter adapter;
    TextView page;
    int count = 1;

    String lastData;                //edit
    JsonParser parser;              //edit

    public static boolean running;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: start app");

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        page = findViewById(R.id.page_count);
        page.setText(String.valueOf(count));

        shared = PreferenceManager.getDefaultSharedPreferences(this);
        lastData = shared.getString("json", "not found");
        lv = findViewById(R.id.list_of_photo);

    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: begain");
        super.onResume();
        if (running)
            return;
        parser = new JsonParser(this);

        if (isNetworkAvailable()) {
            String query = shared.getString("TAGS", "car");
            Log.d(TAG, "onResume: query : " + query);
            parser.execute(query);

        } else {
            Toast.makeText(this, "no internet connection available..!", Toast.LENGTH_LONG).show();
            Log.d(TAG, "onResume: no internet connection ");
            parser.execute(lastData);
            Log.d(TAG, "onResume: last file : " + lastData);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.app_par_search) {
            Log.d(TAG, "onOptionsItemSelected: search clicked");
            Intent i = new Intent(this, SearchActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void _next(View v) {
        if (listOfPhotos == null || listOfPhotos.size() == 0)
            return;
        if (count != 5) {

            try {

                subList.clear();
                subList.addAll(listOfPhotos.subList((count) * 20, (++count) * 20));
                adapter.notifyDataSetChanged();
                Log.d(TAG, "_next: " + count);
                page.setText(String.valueOf(count));

            } catch (Exception e) {
                Log.d(TAG, "_next: error" + e.getMessage());
                subList.addAll(listOfPhotos.subList(listOfPhotos.size()-10, listOfPhotos.size()));
                adapter.notifyDataSetChanged();
                Log.d(TAG, "_next: " + count);
                page.setText(String.valueOf(count));
            }

        }
    }

    public void _previous(View v) {
        if (listOfPhotos == null || listOfPhotos.size() == 0)
            return;
        if (count != 1) {
            try {
                count--;
                subList.clear();
                subList.addAll(listOfPhotos.subList((count - 1) * 20, (count) * 20));
                adapter.notifyDataSetChanged();

                Log.d(TAG, "_next: " + count);
                page.setText(String.valueOf(count));

            } catch (Exception e) {
                Log.d(TAG, "_previous: error" + e.getMessage());
                subList.addAll(listOfPhotos.subList((count - 1) * 20, listOfPhotos.size()));
                adapter.notifyDataSetChanged();

                Log.d(TAG, "_next: " + count);
                page.setText(String.valueOf(count));
            }

        }
    }

    @Override
    public void onDataAvailable(ArrayList<Photo> photos) {
        Log.d(TAG, "onDataAvailable: data ready to use" + photos.size());

        if (photos.size() <20) {
            Toast.makeText(this, "network problem", Toast.LENGTH_SHORT).show();
            parser = new JsonParser(this);
            parser.execute(lastData);
            return;
        }
        page.setText(String.valueOf(count = 1));
        listOfPhotos = new ArrayList<>(photos);
        subList = new ArrayList<>(listOfPhotos.subList(0, 20));
        adapter = new ListViewAdapter(subList);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
        lv.setOnItemLongClickListener(this);

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onDestroy() {
        if (isNetworkAvailable() && listOfPhotos != null && listOfPhotos.size() >20) {
            shared.edit().putString("json", lastJsonFile).apply();
        }

        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(MainActivity.this, PhototDetails.class);
        //i.putExtra("photo",listOfPhotos.get(position));
        i.putExtra("photo", subList.get(position));
        startActivity(i);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        //ShowDetailsDilaog dialog = new ShowDetailsDilaog(MainActivity.this, listOfPhotos.get(position));
        ShowDetailsDilaog dialog = new ShowDetailsDilaog(MainActivity.this, subList.get(position));
        dialog.show();
        return true;
    }
}
