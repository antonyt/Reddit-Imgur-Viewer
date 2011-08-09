package com.at465.riviewer.deserialise;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageResponseHandler implements ResponseHandler<Bitmap> {

    @Override
    public Bitmap handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
	if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	    HttpEntity entity = response.getEntity();
	    byte[] data = EntityUtils.toByteArray(entity);
	    return BitmapFactory.decodeByteArray(data, 0, data.length);
	}

	return null;

    }

}
