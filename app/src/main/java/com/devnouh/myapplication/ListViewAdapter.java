package com.devnouh.myapplication;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    private ArrayList<Photo> mPhotos;

    ListViewAdapter(ArrayList<Photo> photos) {
        mPhotos = photos;
    }

    @Override
    public int getCount() {
        return mPhotos.size();
    }

    @Override
    public Object getItem(int position) {
        return mPhotos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vr;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_photo, null);
            vr = new ViewHolder(convertView);
            convertView.setTag(vr);
        } else {
            vr = (ViewHolder) convertView.getTag();
        }
        // vr.title.setText(mPhotos.get(position).getTitle());

        Picasso.get().load(mPhotos.get(position).getLink())
//                .networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(R.drawable.placeholder)
                .into(vr.image);

        return convertView;
    }

    static class ViewHolder {
        //private TextView title;
        private ImageView image;

        ViewHolder(View v) {
            image = v.findViewById(R.id.listview_image);
        }
    }
}
