package com.at465.riviewer.deserialise;

import java.lang.ref.SoftReference;

import android.graphics.Bitmap;

public class Image {

    private String hash;
    private String title;
    private int views;
    private String bandwidth;
    private String author;
    private String datetime;
    private String ext;
    private int width, height;
    private int ups, downs, points;
    private String permalink;
    private String subreddit;
    private boolean nsfw;
    private String created;
    private int score;
    private String date;
    private SoftReference<Bitmap> bitmap = new SoftReference<Bitmap>(null);

    public void setHash(String hash) {
	this.hash = hash;
    }

    public String getHash() {
	return hash;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getTitle() {
	return title;
    }

    public void setBitmap(Bitmap bitmap) {
	this.bitmap = new SoftReference<Bitmap>(bitmap);
    }

    public Bitmap getBitmap() {
	return bitmap.get();
    }

    public void setExt(String ext) {
	this.ext = ext;
    }

    public String getExt() {
	return ext;
    }

    public void setPermalink(String permalink) {
	this.permalink = permalink;
    }

    public String getPermalink() {
	return permalink;
    }

}
