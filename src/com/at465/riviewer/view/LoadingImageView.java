package com.at465.riviewer.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.at465.riviewer.R;

public class LoadingImageView extends ImageView {

    private final Animation rotateAnim;

    public LoadingImageView(Context context) {
	super(context);
	rotateAnim = AnimationUtils.loadAnimation(context, R.anim.rotate);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
	if (bm == null) {
	    setImageResource(android.R.drawable.ic_menu_info_details);
	    startAnimation(rotateAnim);
	} else {
	    clearAnimation();
	    super.setImageBitmap(bm);
	}
    }

}
