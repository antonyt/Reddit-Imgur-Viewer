package com.at465.riviewer.deserialise;

import java.util.List;

public class Gallery {
    private int count;
    private List<Image> images;

    public void setCount(int count) {
	this.count = count;
    }

    public int getCount() {
	return count;
    }

    public void setImages(List<Image> images) {
	this.images = images;
    }

    public List<Image> getImages() {
	return images;
    }
}
