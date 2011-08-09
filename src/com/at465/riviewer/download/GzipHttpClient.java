package com.at465.riviewer.download;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;


public class GzipHttpClient extends DefaultHttpClient {
    public GzipHttpClient() {
	addRequestInterceptor(new HttpRequestInterceptor() {
	    @Override
	    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
		if (!request.containsHeader("Accept-Encoding")) {
		    request.addHeader("Accept-Encoding", "gzip");
		}

	    }
	});

	addResponseInterceptor(new HttpResponseInterceptor() {
	    @Override
	    public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
		Header encoding = response.getFirstHeader("Content-Encoding");
		if (encoding != null && "gzip".equalsIgnoreCase(encoding.getValue())) {
		    response.setEntity(new GzipEntity(response.getEntity()));
		    ;
		}
	    }
	});
    }
}
