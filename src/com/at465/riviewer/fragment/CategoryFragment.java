package com.at465.riviewer.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;

import com.at465.riviewer.deserialise.Category;
import com.at465.riviewer.deserialise.GsonResponseHandler;
import com.at465.riviewer.deserialise.Image;
import com.at465.riviewer.download.HttpLoader;

public class CategoryFragment extends Fragment implements LoaderCallbacks<Category> {
    private static final String BASE_URL = "http://imgur.com/r/%s/hot/page/%s.json";
    private List<Image> images = Collections.synchronizedList(new ArrayList<Image>());
    private int currentImageIndex = 0;
    private int currentPage = 0;
    private static final int LAST_PAGE = -1;
    private String subreddit = "pics";
    private boolean loadInProgress = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Category> onCreateLoader(int arg0, Bundle arg1) {
	HttpUriRequest request = new HttpGet(String.format(BASE_URL, subreddit, currentPage));
	ResponseHandler<Category> handler = new GsonResponseHandler<Category>(Category.class);
	return new HttpLoader<Category>(getActivity(), request, handler);
    }

    @Override
    public void onLoadFinished(Loader<Category> loader, Category data) {
	Log.d("ViewerActivity", "onLoadFinished ");
	List<Image> newImages = data.getGallery().getImages();
	Listener listener = (Listener) getActivity();

	boolean hasNewImages = newImages != null && newImages.size() > 0;
	boolean firstTime = images.size() == 0;

	if (hasNewImages) {
	    images.addAll(newImages);
	} else {
	    currentPage = LAST_PAGE;
	}

	if (firstTime) {
	    listener.initialLoadComplete(hasNewImages);
	}
	
	loadInProgress = false;

    }

    @Override
    public void onLoaderReset(Loader<Category> arg0) {
    }

    public Image getImage() {
	return images.get(currentImageIndex);
    }

    public Image getNextImage() {
	boolean nearTheEnd = (images.size() - currentImageIndex) < 10;
	boolean onLastPage = currentPage == LAST_PAGE;
	Log.d("CategoryFragment", "onLastPage =  " + onLastPage);
	Log.d("CategoryFragment", "nearTheEnd =  " + nearTheEnd);
	
	if (!loadInProgress && !onLastPage && nearTheEnd) {
	    loadInProgress = true;
	    currentPage++;
	    getLoaderManager().restartLoader(0, null, this);
	    Log.d("CategoryFragment", "LOADING ANOTHER PAGE!!");
	}

	currentImageIndex = Math.min(images.size() - 1, currentImageIndex + 1);
	return images.get(currentImageIndex);
    }

    public Image getPrevImage() {
	currentImageIndex = Math.max(0, currentImageIndex - 1);
	return images.get(currentImageIndex);
    }

    public static interface Listener {
	void initialLoadComplete(boolean hasImages);
    }
}
