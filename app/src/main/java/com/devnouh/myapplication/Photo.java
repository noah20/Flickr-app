package com.devnouh.myapplication;

import androidx.annotation.NonNull;

import java.io.Serializable;

class Photo implements Serializable {
    private String title;
    private String link;
    private String author;
    private String ID;
    private String image;

    Photo(String title, String link, String author, String ID, String image) {
        this.title = title;
        this.link = link;
        this.author = author;
        this.ID = ID;
        this.image = image;
    }

    String getTitle() {
        return title;
    }


    String getLink() {
        return link;
    }


    String getAuthor() {
        return author;
    }


    String getID() {
        return ID;
    }


    String getImage() {
        return image;
    }


    @NonNull
    @Override
    public String toString() {
        return "title" + title;
    }
}
