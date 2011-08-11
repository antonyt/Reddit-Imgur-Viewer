package com.at465.riviewer.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.at465.riviewer.R;

public class ChooseSubredditFragment extends DialogFragment {
    private EditText subredditFilter;
    private ListView subredditList;
    private ArrayAdapter<String> adapter;
    private static final String[] SUBREDDITS = new String[] { "pics", "food", "worldnews", "politics", "comics",
	    "itookapicture", "photography", "gaming", "funny", "aww", "AdviceAnimals", "ragenovels", "gifs", "hot",
	    "nsfw" };

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setStyle(STYLE_NO_FRAME, android.R.style.Theme_NoTitleBar_Fullscreen);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	View root = inflater.inflate(R.layout.choose_subreddit, container, false);
	root.findViewById(R.id.ok_button).setOnClickListener(okListener);

	subredditFilter = (EditText) root.findViewById(R.id.subreddit);
	subredditFilter.addTextChangedListener(watcher);
	subredditFilter.setSaveEnabled(false);

	subredditList = (ListView) root.findViewById(R.id.subreddit_list);
	subredditList.setSaveEnabled(false);

	adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1,
		SUBREDDITS);
	subredditList.setAdapter(adapter);
	subredditList.setOnItemClickListener(listClickListener);
	return root;
    }

    @Override
    public void onDestroyView() {
	Listener listener = (Listener) getTargetFragment();
	String selected = subredditFilter.getText().toString();
	if (selected.length() > 0) {
	    listener.subredditSelected(selected);
	}
	super.onDestroyView();
    }

    public static interface Listener {
	void subredditSelected(String subreddit);
    }

    private OnItemClickListener listClickListener = new OnItemClickListener() {
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	    String selected = adapter.getItem(position);
	    subredditFilter.setText(selected);
	    dismiss();
	}
    };

    private TextWatcher watcher = new TextWatcher() {
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	@Override
	public void afterTextChanged(Editable s) {
	    adapter.getFilter().filter(s);
	}
    };

    private OnClickListener okListener = new OnClickListener() {
	@Override
	public void onClick(View v) {
	    dismiss();
	}
    };
}
