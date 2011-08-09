package com.at465.riviewer.download;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CacheRequest;
import java.net.CacheResponse;
import java.net.ResponseCache;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.at465.riviewer.R.string;
import com.at465.riviewer.deserialise.Image;

public class CachedImageLoader extends AsyncTaskLoader<Bitmap> {
    private static final String BASE_URL = "http://api.imgur.com/%sl%s";
    private Image image;

    public CachedImageLoader(Context context, Image image) {
	super(context);
	this.image = image;
	try {
	    init();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void init() throws IOException {
	ResponseCache.setDefault(new ResponseCache() {
	    final String cacheDir = getContext().getCacheDir().getCanonicalPath();
	    private Map<String, List<String>> headers;

	    @Override
	    public CacheRequest put(URI uri, URLConnection conn) throws IOException {
		final File file = new File(cacheDir, escape(conn.getURL().getPath()));
		return new CacheRequest() {
		    @Override
		    public OutputStream getBody() throws IOException {
			Log.d("CacheRequest", "getBody " + file.getAbsolutePath());
			return new FileOutputStream(file);
		    }
		    
		    @Override
		    public void abort() {
			file.delete();
		    }
		};
	    }

	    @Override
	    public CacheResponse get(URI uri, String rqstMethod, final Map<String, List<String>> rqstHeaders)
		    throws IOException {
		this.headers = rqstHeaders;
		
		final File file = new File(cacheDir, escape(uri.getPath()));
		if (file.exists()) {
		    return new CacheResponse() {
			@Override
			public Map<String, List<String>> getHeaders() throws IOException {
			    return rqstHeaders;
			}

			@Override
			public InputStream getBody() throws IOException {
			    Log.d("CacheResponse", "getBody " + file.getAbsolutePath());
			    FileInputStream fis = new FileInputStream(file);
			    return fis;
			}
		    };
		} else {
		    return null;
		}
	    }

	    private String escape(String url) {
		return url.replace("/", "=").replace(".", "-");
	    }
	});

    }

    @Override
    public Bitmap loadInBackground() {
	String strUrl = String.format(BASE_URL, image.getHash(), image.getExt());

	try {
	    URL url = new URL(strUrl);
	    URLConnection connection = url.openConnection();
	    connection.setUseCaches(true);
	    Object response = connection.getContent();
	    Log.d("CachedImageLoader", "loadInBackground response = " + response);
	    
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
