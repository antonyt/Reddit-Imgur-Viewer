package com.at465.riviewer.download;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class HttpLoader<D> extends AsyncTaskLoader<D> {
    
    private static final HttpClient httpClient = new GzipHttpClient();
    private ResponseHandler<D> handler;
    private HttpUriRequest request;
    
    
    public HttpLoader(Context context, HttpUriRequest request, ResponseHandler<D> handler) {
	super(context);
	this.request = request;
	this.handler = handler;
    }

    @Override
    public D loadInBackground() {
	try {
	    return httpClient.execute(request, handler);
	} catch (ClientProtocolException e) {
	    e.printStackTrace();
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
