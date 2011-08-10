package com.at465.riviewer.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.at465.riviewer.R;

public class NavigatorFragment extends Fragment implements CategoryDataFragment.Listener {
    private Button prevButton;
    private Button nextButton;
    private CategoryDataFragment categoryDataFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	categoryDataFragment = new CategoryDataFragment();
	categoryDataFragment.setTargetFragment(this, 0);
	getFragmentManager().beginTransaction().add(categoryDataFragment, "category").commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	View v = inflater.inflate(R.layout.navigator_fragment, null);

	nextButton = (Button) v.findViewById(R.id.next);
	nextButton.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		getImageFragment().loadImage(categoryDataFragment.getNextImage());
	    }
	});

	prevButton = (Button) v.findViewById(R.id.prev);
	prevButton.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		getImageFragment().loadImage(categoryDataFragment.getPrevImage());
	    }
	});

	return v;
    }

    private AsyncImageFragment getImageFragment() {
	return (AsyncImageFragment) getTargetFragment();
    }

    @Override
    public void initialLoadComplete(boolean hasImages) {
	if (hasImages) {
	    getImageFragment().loadImage(categoryDataFragment.getImage());
	} else {
	    new AlertDialog.Builder(getActivity()).setTitle("No images to show!").show();
	}
    }

}
