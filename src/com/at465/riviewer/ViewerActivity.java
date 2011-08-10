package com.at465.riviewer;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;

import com.at465.riviewer.deserialise.Image;
import com.at465.riviewer.fragment.AsyncImageFragment;
import com.at465.riviewer.fragment.CategoryFragment;

public class ViewerActivity extends FragmentActivity implements CategoryFragment.Listener{

    private AsyncImageFragment imageFragment;
    private CategoryFragment categoryFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);

	imageFragment = (AsyncImageFragment) getSupportFragmentManager().findFragmentById(R.id.image);

	findViewById(R.id.next).setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		Image image = categoryFragment.getNextImage();
		imageFragment.loadImage(image);
	    }
	});

	findViewById(R.id.prev).setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		Image image = categoryFragment.getPrevImage();
		imageFragment.loadImage(image);
	    }
	});
	
	categoryFragment = new CategoryFragment();
	getSupportFragmentManager().beginTransaction().add(categoryFragment, "category").commit();
    }

    @Override
    public void initialLoadComplete(boolean hasImages) {
	if (hasImages) {
	    Image image = categoryFragment.getImage();
	    imageFragment.loadImage(image);
	} else {
	    new AlertDialog.Builder(this).setTitle("No images to show!").show();
	}
    }


}