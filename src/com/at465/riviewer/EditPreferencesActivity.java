package com.at465.riviewer;

import com.at465.riviewer.fragment.CategoryDataFragment;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;

public class EditPreferencesActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {
    public static final String IMAGE_SIZE_KEY = "image_size";
    public static final String NSFW_KEY = "nsfw";
    private SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	addPreferencesFromResource(R.xml.pref);
	prefs = getPreferenceManager().getSharedPreferences();
	prefs.registerOnSharedPreferenceChangeListener(this);

	// initial values
	onSharedPreferenceChanged(prefs, IMAGE_SIZE_KEY);
	onSharedPreferenceChanged(prefs, NSFW_KEY);
	setResult(CategoryDataFragment.RESULT_NO_CHANGE);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
	if (NSFW_KEY.equals(key)) {
	    CheckBoxPreference nsfwCheckBox = (CheckBoxPreference) findPreference(key);
	    int messageId = nsfwCheckBox.isChecked() ? R.string.show_nsfw : R.string.dont_show_nsfw;
	    nsfwCheckBox.setSummary(messageId);
	    setResult(CategoryDataFragment.RESULT_NSFW_CHANGE);
	} else if (IMAGE_SIZE_KEY.equals(key)) {
	    ListPreference imageSizeList = (ListPreference) findPreference(key);
	    String imageSize = imageSizeList.getEntry().toString().toLowerCase();
	    String message = String.format(getString(R.string.image_size_message), imageSize);
	    imageSizeList.setSummary(message);
	    setResult(CategoryDataFragment.RESULT_IMAGE_SIZE_CHANGE);
	}
    }
}