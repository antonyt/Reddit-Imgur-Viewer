package com.at465.riviewer.view;

import android.content.Context;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.at465.riviewer.EditPreferencesActivity;
import com.at465.riviewer.R;
import com.at465.riviewer.deserialise.Image;

public class ImageWebView extends RelativeLayout {
    private static final String BASE_URL = "http://api.imgur.com/%s%s%s";
    private static final String TEMPLATE_URL = "file:///android_asset/image_template.html";
    private static final String LOAD_IMAGE = "javascript: loadImage('%s', %s, %s);";
    private static final String JAVASCRIPT_INTERFACE = "Loading";
    private Image image;
    private float density;
    private WebView webview;
    private ProgressBar loading;

    public ImageWebView(Context context) {
	super(context);
	LayoutInflater.from(context).inflate(R.layout.image_web_view, this, true);
	loading = (ProgressBar) findViewById(R.id.progress);

	webview = (WebView) findViewById(R.id.webview);
	webview.setBackgroundColor(Color.BLACK);
	webview.getSettings().setBuiltInZoomControls(true);
	webview.getSettings().setJavaScriptEnabled(true);
	webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
	webview.addJavascriptInterface(new JavascriptInterface(), JAVASCRIPT_INTERFACE);

	webview.loadUrl(TEMPLATE_URL);

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
	webview.stopLoading();
	this.image = image;

	if (image == null) {
	    webview.clearView();
	} else {
	    String imageSize = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(
		    EditPreferencesActivity.IMAGE_SIZE_KEY, "");
	    String url = String.format(BASE_URL, image.getHash(), imageSize, image.getExt());
	    float width = getWidth() / density;
	    float height = getHeight() / density;
	    String loadImageJS = String.format(LOAD_IMAGE, url, width, height);
	    Log.d("ImageWebView", "loadImage: " + loadImageJS);
	    webview.loadUrl(loadImageJS);
	}
    }

    public class JavascriptInterface {
	public void showLoadingDialog(boolean showDialog) {
	    if (showDialog) {
		post(showDialogRunnable);
	    } else {
		post(hideDialogRunnable);
	    }
	}
    };

    private Runnable showDialogRunnable = new Runnable() {
	@Override
	public void run() {
	    webview.setVisibility(INVISIBLE);
	    loading.setVisibility(VISIBLE);
	}
    };

    private Runnable hideDialogRunnable = new Runnable() {
	@Override
	public void run() {
	    webview.setVisibility(VISIBLE);
	    loading.setVisibility(INVISIBLE);
	}
    };

}
