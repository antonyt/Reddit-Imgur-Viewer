package com.at465.riviewer.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.at465.riviewer.deserialise.Image;

public class AsyncImageFragment extends Fragment {
    private static final String BASE_URL = "http://api.imgur.com/%sl%s";
    private Image image;
    private WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	webView = new WebView(getActivity());
	webView.setBackgroundColor(0);
	webView.getSettings().setBuiltInZoomControls(true);
	webView.getSettings().setJavaScriptEnabled(true);
	webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
	return webView;
    }

    
    public void loadImage(Image image) {	
	this.image = image;
	webView.stopLoading();
	String url = String.format(BASE_URL, image.getHash(), image.getExt());
	float width = webView.getWidth() / 1.5f;
	float height = webView.getHeight() / 1.5f;
	String style = "<head><style>* {margin:0;padding:0;}</style></head>";
	String html = style + "<body><img id='image' src='" + url + "' style='position:absolute; top:0; bottom:0; left:0; right:0; margin:auto; max-width:" + width + "px; max-height:" + height + "'/></body>";
	Log.d("AsyncImageFragment", "loadImage " + html);
	webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
    }
}
