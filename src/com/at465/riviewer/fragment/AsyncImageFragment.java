package com.at465.riviewer.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.at465.riviewer.R;
import com.at465.riviewer.deserialise.Image;
import com.at465.riviewer.view.WebViewSwitcher;
import com.at465.riviewer.view.WebViewSwitcher.AnimationMode;

public class AsyncImageFragment extends Fragment {
    private Image image;
    private WebViewSwitcher webView;
    private TextView title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	LinearLayout root = (LinearLayout) inflater.inflate(R.layout.image_fragment, null);
	title = (TextView) root.findViewById(R.id.title);
	FrameLayout imageContainer = (FrameLayout) root.findViewById(R.id.image_container);
	
	webView = new WebViewSwitcher(getActivity());
	imageContainer.addView(webView);
	return root;
    }

    
    public void loadImage(Image image, AnimationMode animMode) {
	title.setText(Html.fromHtml(image.getTitle()));
	webView.loadImage(image, animMode);
    }
    
    
}
