package com.at465.riviewer;

import java.util.List;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.at465.riviewer.deserialise.Category;
import com.at465.riviewer.deserialise.GsonResponseHandler;
import com.at465.riviewer.deserialise.Image;
import com.at465.riviewer.download.HttpLoader;
import com.at465.riviewer.fragment.AsyncImageFragment;

public class ViewerActivity extends FragmentActivity {
    private TextView title;

    private List<Image> images;
    private int i;

    private AsyncImageFragment imageFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	this.requestWindowFeature(Window.FEATURE_NO_TITLE);

	setContentView(R.layout.main);
	
	if (savedInstanceState != null) {
	    i = savedInstanceState.getInt("imageIndex");
	}
	
	
	title = (TextView) findViewById(R.id.title);
	imageFragment = (AsyncImageFragment) getSupportFragmentManager().findFragmentById(R.id.image);

	findViewById(R.id.next).setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		i = Math.min(images.size()-1, i+1);
		Image currentImage = images.get(i);
		title.setText(currentImage.getTitle());
		imageFragment.loadImage(currentImage);
	    }
	});

	findViewById(R.id.prev).setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		i = Math.max(0, i-1);
		Image currentImage = images.get(i);
		title.setText(currentImage.getTitle());
		imageFragment.loadImage(currentImage);
	    }
	});

	getSupportLoaderManager().initLoader(0, null, categoryCallback);
    }

    LoaderCallbacks<Category> categoryCallback = new LoaderCallbacks<Category>() {
	@Override
	public Loader<Category> onCreateLoader(int arg0, Bundle arg1) {
	    final String BASE_URL = "http://imgur.com/r/%s.json";
	    HttpUriRequest request = new HttpGet(String.format(BASE_URL, "pics"));
	    ResponseHandler<Category> handler = new GsonResponseHandler<Category>(Category.class);
	    return new HttpLoader<Category>(ViewerActivity.this, request, handler);
	}

	@Override
	public void onLoadFinished(Loader<Category> loader, Category data) {
	    Log.d("ViewerActivity", "onLoadFinished ");
	    images = data.getGallery().getImages();
	    title.setText(images.get(i).getTitle());
	    imageFragment.loadImage(images.get(i));
	    
	}

	@Override
	public void onLoaderReset(Loader<Category> arg0) {
	}
    };
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
	outState.putInt("imageIndex", i);
	super.onSaveInstanceState(outState);
    };
    
}