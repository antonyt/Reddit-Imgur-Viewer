package com.at465.riviewer.view;

import android.R;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.at465.riviewer.deserialise.Image;

public class ImageWebView extends WebView {
    private static final String BASE_URL = "http://api.imgur.com/%s%s";
    private static final String TEMPLATE_URL = "file:///android_asset/image_template.html";
    private static final String LOAD_IMAGE = "javascript: loadImage('%s', %s, %s);";
    private Image image;
    private float density;

    public ImageWebView(Context context) {
	super(context);
	setBackgroundColor(Color.BLACK);
	getSettings().setBuiltInZoomControls(true);
	getSettings().setJavaScriptEnabled(true);
	setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
	addJavascriptInterface(new JavascriptInterface(), "Loading");

	loadUrl(TEMPLATE_URL);

	DisplayMetrics dm = new DisplayMetrics();
	WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	windowManager.getDefaultDisplay().getMetrics(dm);
	this.density = dm.density;

    }

    @Override
    protected void onSizeChanged(int w, int h, int ow, int oh) {
	super.onSizeChanged(w, h, ow, oh);
	if (image != null && w > 0) {
	    loadImage(image);
	}
    }

    public void loadImage(Image image) {
	stopLoading();
	this.image = image;
	String url = String.format(BASE_URL, image.getHash(), image.getExt());
	float width = getWidth() / density;
	float height = getHeight() / density;
	String loadImageJS = String.format(LOAD_IMAGE, url, width, height);
	Log.d("AsyncImageFragment", "loadImage: " + loadImageJS);
	loadUrl(loadImageJS);
    }

    public class JavascriptInterface {
	private Dialog d;

	public void showLoadingDialog(boolean showDialog) {
	    if (showDialog) {
		if (d != null) {
		    d.dismiss();
		}
		Log.d("ImageWebView.JavascriptInterface", "SHOWLoadingDialog ");
		d = new Dialog(getContext(), R.style.Theme_Panel);
		d.setContentView(new ProgressBar(getContext()));
		d.show();
	    } else {
		Log.d("ImageWebView.JavascriptInterface", "HIDELoadingDialog ");
		if (d != null) {
		    d.dismiss();
		    d = null;
		}
	    }
	}
    }

}
