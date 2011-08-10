package com.at465.riviewer.view;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewSwitcher;

import com.at465.riviewer.R;
import com.at465.riviewer.deserialise.Image;

public class WebViewSwitcher extends ViewSwitcher {

    private Animation backwardOutAnim, backwardInAnim;
    private Animation forwardInAnim, forwardOutAnim;

    public WebViewSwitcher(Context context) {
	super(context);
	addView(new ImageWebView(context));
	addView(new ImageWebView(context));

	backwardInAnim = AnimationUtils.loadAnimation(context, R.anim.slide_in_left);
	backwardOutAnim = AnimationUtils.loadAnimation(context, R.anim.slide_out_right);
	
	forwardInAnim = AnimationUtils.loadAnimation(context, R.anim.slide_in_right);
	forwardOutAnim = AnimationUtils.loadAnimation(context, R.anim.slide_out_left);
    }

    public void loadImage(Image image, AnimationMode animMode) {
	setAnimation(animMode);
	ImageWebView nextView = (ImageWebView) getNextView();
	nextView.loadImage(image);
	showNext();
    }

    private void setAnimation(AnimationMode animMode) {
	switch (animMode) {
	case FORWARD:
	    setInAnimation(forwardInAnim);
	    setOutAnimation(forwardOutAnim);
	    break;
	case BACKWARD:
	    setInAnimation(backwardInAnim);
	    setOutAnimation(backwardOutAnim);
	    break;
	case NONE:
	    setInAnimation(null);
	    setOutAnimation(null);
	    break;
	}
    }

    public static enum AnimationMode {
	FORWARD, BACKWARD, NONE
    }

}
