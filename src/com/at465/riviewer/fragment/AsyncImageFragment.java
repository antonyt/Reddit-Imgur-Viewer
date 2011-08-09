package com.at465.riviewer.fragment;

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
import com.at465.riviewer.download.CachedImageLoader;
import com.at465.riviewer.view.LoadingImageView;

public class AsyncImageFragment extends Fragment implements LoaderCallbacks<Bitmap> {
    private ImageView imageView;
    private Image image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	this.imageView = new LoadingImageView(getActivity());
	imageView.setScaleType(ScaleType.CENTER);
	return imageView;
    }

    public void loadImage(Image image) {
	getLoaderManager().destroyLoader(0);
	this.image = image;
	Bitmap bitmap = image.getBitmap();
	imageView.setImageBitmap(bitmap);
	if (bitmap == null) {
	    getLoaderManager().restartLoader(0, null, this);
	}
    }

    @Override
    public Loader<Bitmap> onCreateLoader(int arg0, Bundle arg1) {
	return new CachedImageLoader(getActivity(), image);
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
