package com.at465.riviewer.fragment;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.at465.riviewer.deserialise.Image;
import com.at465.riviewer.deserialise.ImageResponseHandler;
import com.at465.riviewer.download.HttpLoader;
import com.at465.riviewer.view.LoadingImageView;

public class AsyncImageFragment extends Fragment implements LoaderCallbacks<Bitmap> {
    private static final String BASE_URL = "http://api.imgur.com/%sl%s";
    private ImageView imageView;
    private Image image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	this.imageView = new LoadingImageView(getActivity());
	imageView.setScaleType(ScaleType.CENTER);
	return imageView;
    }

    public void loadImage(Image image) {
	this.image = image;
	Bitmap bitmap = image.getBitmap();
	imageView.setImageBitmap(bitmap);
	if (bitmap == null) {
	    getLoaderManager().restartLoader(0, null, this);
	}
    }

    @Override
    public Loader<Bitmap> onCreateLoader(int arg0, Bundle arg1) {
	HttpUriRequest request = new HttpGet(String.format(BASE_URL, image.getHash(), image.getExt()));
	ResponseHandler<Bitmap> handler = new ImageResponseHandler();
	return new HttpLoader<Bitmap>(getActivity(), request, handler);
    }

    @Override
    public void onLoadFinished(Loader<Bitmap> arg0, Bitmap bitmap) {
	image.setBitmap(bitmap);
	imageView.setImageBitmap(bitmap);
    }

    @Override
    public void onLoaderReset(Loader<Bitmap> arg0) {
    }
}
