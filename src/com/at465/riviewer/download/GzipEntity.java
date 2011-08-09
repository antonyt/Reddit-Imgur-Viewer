package com.at465.riviewer.download;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;

public class GzipEntity extends HttpEntityWrapper {

	public GzipEntity(HttpEntity wrapped) {
		super(wrapped);
	}

	@Override
	public InputStream getContent() throws IOException {
		return new GZIPInputStream(super.getContent());
	}

}
