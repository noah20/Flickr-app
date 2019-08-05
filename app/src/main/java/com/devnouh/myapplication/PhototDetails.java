package com.devnouh.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhototDetails extends AppCompatActivity {

    TextView title;
    TextView tags;
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photot_details);
        getSupportActionBar().setTitle(R.string.photo_details_act);
        title = findViewById(R.id.title_details_activity);
        tags = findViewById(R.id.tags_details_activity);
        iv = findViewById(R.id.image_details_activity);
        Intent i = getIntent();
        Photo mPhoto = (Photo) i.getSerializableExtra("photo");
        if (mPhoto != null) {
            Resources r = getResources();
            title.setText(r.getString(R.string.details_title, mPhoto.getTitle()));
            // tags.setText(r.getString(R.string.details_tags,mPhoto.getTags()));
            Picasso.get().load(mPhoto.getImage())
                    .placeholder(R.drawable.placeholder)
                    //.networkPolicy(NetworkPolicy.OFFLINE)
                    .into(iv);
        }

        com.devnouh.myapplication.MainActivity.running = true;

    }
}
