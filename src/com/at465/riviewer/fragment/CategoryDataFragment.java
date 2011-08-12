package com.at465.riviewer.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.at465.riviewer.R;
import com.at465.riviewer.deserialise.Category;
import com.at465.riviewer.deserialise.GsonResponseHandler;
import com.at465.riviewer.deserialise.Image;
import com.at465.riviewer.download.HttpLoader;

public class CategoryDataFragment extends Fragment implements LoaderCallbacks<Category>,
	ChooseSubredditFragment.Listener {
    private static final String BASE_URL = "http://imgur.com/r/%s/hot/page/%s.json";
    private List<Image> images = Collections.synchronizedList(new ArrayList<Image>());
    private int currentImageIndex = 0;
    private int currentPage = 0;
    private static final int LAST_PAGE = -1;
    private String subreddit = "pics";
    private boolean loadInProgress = true;
    private ChooseSubredditFragment subredditSelector;
    private ProgressDialog loadingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	getLoaderManager().initLoader(0, null, this);
	setHasOptionsMenu(true);
	subredditSelector = new ChooseSubredditFragment();
	subredditSelector.setTargetFragment(this, 0);
    }

    @Override
    public Loader<Category> onCreateLoader(int arg0, Bundle arg1) {
	if (images.size() == 0) {
	    loadingDialog = ProgressDialog.show(getActivity(), "", getActivity().getString(R.string.loading), true,
		    false);
	}
	HttpUriRequest request = new HttpGet(String.format(BASE_URL, subreddit, currentPage));
	ResponseHandler<Category> handler = new GsonResponseHandler<Category>(Category.class);
	return new HttpLoader<Category>(getActivity(), request, handler);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	super.onCreateOptionsMenu(menu, inflater);
	inflater.inflate(R.menu.navigator, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case R.id.subreddit:
	    subredditSelector.show(getFragmentManager(), "dialog");
	case R.id.reddit:
	    Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.reddit.com" + getImage().getPermalink()));
	    startActivity(viewIntent);
	    return true;
	default:
	    return super.onOptionsItemSelected(item);
	}
    }

    @Override
    public void onLoadFinished(Loader<Category> loader, Category data) {
	Log.d("ViewerActivity", "onLoadFinished ");
	if (images.size() == 0) {
	    loadingDialog.dismiss();
	}
	Listener listener = (Listener) getTargetFragment();

	if (data == null) {
	    Log.d("CategoryDataFragment", "failed to load index!");
	    listener.initialLoadComplete(false);
	    return;
	}

	List<Image> newImages = data.getGallery().getImages();

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

    @Override
    public void subredditSelected(String subreddit) {
	if (this.subreddit.equals(subreddit)) {
	    return;
	}

	this.subreddit = subreddit;
	images.clear();
	currentImageIndex = 0;
	currentPage = 0;
	getLoaderManager().restartLoader(0, null, this);
    }
}
