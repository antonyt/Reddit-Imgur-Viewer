package com.at465.riviewer.deserialise;

import java.util.List;

public class Category {
    private List<Image> gallery;

    public void setGallery(List<Image> gallery) {
	this.gallery = gallery;
    }

    public List<Image> getGallery() {
	return gallery;
    }
}
