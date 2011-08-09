package com.at465.riviewer.download;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.AsyncTaskLoader;

import com.at465.riviewer.deserialise.Image;

public class CachedImageLoader extends AsyncTaskLoader<Bitmap> {
    private static final String BASE_URL = "http://api.imgur.com/%sl%s";
    private Image image;

    public CachedImageLoader(Context context, Image image) {
	super(context);
	this.image = image;
    }

    @Override
    public Bitmap loadInBackground() {
	String strUrl = String.format(BASE_URL, image.getHash(), image.getExt());

	try {
	    URL url = new URL(strUrl);
	    URLConnection connection = url.openConnection();
	    connection.setUseCaches(true);
	    Object response = connection.getContent();
	    if (response instanceof InputStream) {
		InputStream stream = (InputStream) response;
		return BitmapFactory.decodeStream(stream);
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}

	return null;
    }

    @Override
    protected void onStartLoading() {
	forceLoad();
	super.onStartLoading();
    }
}
