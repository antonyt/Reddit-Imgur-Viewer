package com.at465.riviewer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

public class ViewerActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);
	Fragment navigator = getSupportFragmentManager().findFragmentById(R.id.navigator);
	Fragment image = getSupportFragmentManager().findFragmentById(R.id.image);
	navigator.setTargetFragment(image, 0);
    }


}