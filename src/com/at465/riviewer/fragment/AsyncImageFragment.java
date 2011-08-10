package com.at465.riviewer.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.at465.riviewer.deserialise.Image;

public class AsyncImageFragment extends Fragment {
    private static final String BASE_URL = "http://api.imgur.com/%s%s";
    private static final String TEMPLATE_URL = "file:///android_asset/image_template.html";
    private static final String LOAD_IMAGE = "javascript: loadImage('%s', %s, %s);";
    private Image image;
    private WebView webView;
    private float density;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	DisplayMetrics dm = new DisplayMetrics();
	getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
	this.density = dm.density;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	webView = new WebView(getActivity()) {
	    @Override
	    protected void onSizeChanged(int w, int h, int ow, int oh) {
		super.onSizeChanged(w, h, ow, oh);
		if (image != null && w > 0) {
		    loadImage(image);
		}
	    };
	};
	webView.setBackgroundColor(Color.BLACK);
	webView.getSettings().setBuiltInZoomControls(true);
	webView.getSettings().setJavaScriptEnabled(true);
	webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
	webView.loadUrl(TEMPLATE_URL);
	return webView;
    }

    
    public void loadImage(Image image) {
	webView.stopLoading();
	this.image = image;
	String url = String.format(BASE_URL, image.getHash(), image.getExt());
	float width = webView.getWidth() / density;
	float height = webView.getHeight() / density;
	String loadImageJS = String.format(LOAD_IMAGE, url, width, height);
	Log.d("AsyncImageFragment", "loadImage: " + loadImageJS);
	webView.loadUrl(loadImageJS);
    }
    
    
}
