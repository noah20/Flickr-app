package com.devnouh.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


class ShowDetailsDilaog {

    private Photo mPhoto;
    private Context mContext;

    ShowDetailsDilaog(Context context, Photo photo) {
        this.mPhoto = photo;
        this.mContext = context;
    }

    void show() {
        Resources r = mContext.getResources();

        @SuppressLint("InflateParams")
        View v = LayoutInflater.from(mContext).inflate(R.layout.show_dialog_details, null);
        TextView tv_title = v.findViewById(R.id.details_title);
        TextView tv_author = v.findViewById(R.id.details_author);
        TextView tv_author_id = v.findViewById(R.id.details_author_id);
        TextView tv_tags = v.findViewById(R.id.details_tags);
        tv_title.setText(r.getString(R.string.details_title, mPhoto.getTitle()));
        tv_author.setText(r.getString(R.string.details_author, mPhoto.getAuthor()));
        tv_author_id.setText(r.getString(R.string.details_author_id, mPhoto.getID()));
        //  tv_tags.setText(r.getString(R.string.details_tags,mPhoto.getTags()));

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setView(v).show();


    }


}
