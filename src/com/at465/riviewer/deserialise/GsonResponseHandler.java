package com.at465.riviewer.deserialise;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

import com.google.gson.Gson;

public class GsonResponseHandler<T> implements ResponseHandler<T> {

    private static final Gson gson = new Gson();
    private Type type;
    private Class<T> clazz;

    public GsonResponseHandler(Type type) {
	this.type = type;
    }

    public GsonResponseHandler(Class<T> clazz) {
	this.clazz = clazz;
    }

    @Override
    public T handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
	if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	    HttpEntity entity = response.getEntity();
	    Reader entityReader = new InputStreamReader(entity.getContent());
	    try {
		if (clazz != null) {
		    return gson.fromJson(entityReader, clazz);
		} else {
		    return gson.fromJson(entityReader, type);
		}
	    } finally {
		entity.consumeContent();
	    }
	}

	return null;
    }

}
