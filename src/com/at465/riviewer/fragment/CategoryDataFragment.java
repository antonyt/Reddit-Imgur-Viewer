package com.at465.riviewer.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.at465.riviewer.EditPreferencesActivity;
import com.at465.riviewer.R;
import com.at465.riviewer.deserialise.Category;
import com.at465.riviewer.deserialise.GsonResponseHandler;
import com.at465.riviewer.deserialise.Image;
import com.at465.riviewer.download.HttpLoader;

public class CategoryDataFragment extends Fragment implements LoaderCallbacks<Category>,
	ChooseSubredditFragment.Listener {
    private static final String BASE_URL = "http://imgur.com/r/%s/hot/page/%s.json";

    public static final int RESULT_NO_CHANGE = 0;
    public static final int RESULT_IMAGE_SIZE_CHANGE = 1;
    public static final int RESULT_NSFW_CHANGE = 2;

    private List<Image> images = Collections.synchronizedList(new ArrayList<Image>());
    private List<Image> imagesNoNsfw = Collections.synchronizedList(new ArrayList<Image>());
    private List<Image> imagesCurrent;

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

	boolean showNsfw = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(
		EditPreferencesActivity.NSFW_KEY, true);
	imagesCurrent = showNsfw ? images : imagesNoNsfw;
	getLoaderManager().initLoader(0, null, this);

	setHasOptionsMenu(true);
	subredditSelector = new ChooseSubredditFragment();
	subredditSelector.setTargetFragment(this, 0);
    }

    @Override
    public Loader<Category> onCreateLoader(int arg0, Bundle arg1) {
	if (imagesCurrent.size() == 0) {
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
	    return true;
	case R.id.reddit:
	    if (currentImageIndex < images.size()) {
		Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.reddit.com"
			+ getImage().getPermalink()));
		startActivity(viewIntent);
	    }
	    return true;
	case R.id.preferences:
	    Intent preferencesIntent = new Intent(getActivity(), EditPreferencesActivity.class);
	    startActivityForResult(preferencesIntent, 0);
	    return true;
	default:
	    return super.onOptionsItemSelected(item);
	}
    }

    private Listener getListener() {
	return (Listener) getTargetFragment();
    }

    @Override
    public void onLoadFinished(Loader<Category> loader, Category data) {
	Log.d("ViewerActivity", "onLoadFinished ");
	boolean firstTime = imagesCurrent.size() == 0;
	if (firstTime) {
	    loadingDialog.dismiss();
	}

	if (data == null) {
	    Log.d("CategoryDataFragment", "failed to load index!");
	    getListener().initialLoadComplete(false);
	    return;
	}

	List<Image> newImages = data.getGallery();
	boolean hasNewNsfwImages = newImages.size() > 0;
	if (hasNewNsfwImages) {
	    images.addAll(newImages);
	} else {
	    currentPage = LAST_PAGE;
	}

	pruneNsfw(newImages);
	boolean hasNewImages = newImages.size() > 0;
	if (hasNewImages) {
	    imagesNoNsfw.addAll(newImages);
	} else {
	    currentPage = LAST_PAGE;
	}

	boolean showNsfw = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(
		EditPreferencesActivity.NSFW_KEY, true);
	if (firstTime) {
	    getListener().initialLoadComplete(showNsfw ? hasNewNsfwImages : hasNewImages);
	}

	loadInProgress = false;

    }

    @Override
    public void onLoaderReset(Loader<Category> arg0) {
    }

    public Image getImage() {
	getListener().firstImage(currentImageIndex == 0);
	getListener().lastImage(currentImageIndex == imagesCurrent.size() - 1);
	return imagesCurrent.get(currentImageIndex);
    }

    public Image getNextImage() {
	boolean nearTheEnd = (imagesCurrent.size() - currentImageIndex) < 10;
	boolean onLastPage = currentPage == LAST_PAGE;

	if (!loadInProgress && !onLastPage && nearTheEnd) {
	    loadInProgress = true;
	    currentPage++;
	    getLoaderManager().restartLoader(0, null, this);
	    Log.d("CategoryFragment", "LOADING ANOTHER PAGE!!");
	}

	currentImageIndex++;
	getListener().firstImage(currentImageIndex == 0);
	getListener().lastImage(currentImageIndex == imagesCurrent.size() - 1);

	return imagesCurrent.get(currentImageIndex);
    }

    public Image getPrevImage() {
	currentImageIndex--;
	getListener().firstImage(currentImageIndex == 0);
	getListener().lastImage(currentImageIndex == imagesCurrent.size() - 1);
	return imagesCurrent.get(currentImageIndex);
    }

    public static interface Listener {
	void initialLoadComplete(boolean hasImages);

	void firstImage(boolean isFirst);

	void lastImage(boolean isLast);
    }

    @Override
    public void subredditSelected(String subreddit) {
	if (this.subreddit.equals(subreddit)) {
	    return;
	}

	this.subreddit = subreddit;
	images.clear();
	imagesNoNsfw.clear();
	currentImageIndex = 0;
	currentPage = 0;
	getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	switch (resultCode) {
	case RESULT_NO_CHANGE:
	    // no change - no action
	    break;
	case RESULT_IMAGE_SIZE_CHANGE:
	    // image size changed
	    if (currentImageIndex < imagesCurrent.size()) {
		getListener().initialLoadComplete(true);
	    }
	    break;
	case RESULT_NSFW_CHANGE:
	    // nsfw changed
	    boolean showNsfw = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(
		    EditPreferencesActivity.NSFW_KEY, true);

	    Image currentImage = null;
	    if (imagesCurrent.size() > 0) {
		currentImage = imagesCurrent.get(currentImageIndex);
	    }
	    imagesCurrent = showNsfw ? images : imagesNoNsfw;
	    currentImageIndex = imagesCurrent.indexOf(currentImage) == -1 ? 0 : imagesCurrent.indexOf(currentImage);

	    boolean hasImages = imagesCurrent.size() > 0;
	    getListener().initialLoadComplete(hasImages);
	    break;
	}
    }

    private static void pruneNsfw(List<Image> newImages) {
	Iterator<Image> i = newImages.iterator();
	while (i.hasNext()) {
	    Image image = i.next();
	    if (image.isNsfw()) {
		i.remove();
	    }
	}
    }

}
