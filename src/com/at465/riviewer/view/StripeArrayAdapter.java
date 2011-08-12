package com.at465.riviewer.view;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class StripeArrayAdapter<T> extends ArrayAdapter<T> {

    public StripeArrayAdapter(Context context, int resource, int textViewResourceId, T[] objects) {
	super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	View v = super.getView(position, convertView, parent);
	TextView tv = (TextView) v.findViewById(android.R.id.text1);
	tv.setTextColor(Color.BLACK);
	
	if (position % 2 == 0) {
	    v.setBackgroundColor(Color.CYAN);
	} else {
	    v.setBackgroundColor(Color.LTGRAY);
	}
	return v;
    }

}
