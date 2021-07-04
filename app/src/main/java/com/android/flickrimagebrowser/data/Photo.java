package com.android.flickrimagebrowser.data;

import java.io.Serializable;

public class Photo implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String mAuthor;
    private final String mAuthorId;
    private final String mTitle;
    private final String mTags;
    private final String mImageLink;

    public Photo(String author, String authorId, String title, String tags, String imageLink) {
        mAuthor = author;
        mAuthorId = authorId;
        mTitle = title;
        mTags = tags;
        mImageLink = imageLink;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getAuthorId() {
        return mAuthorId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getTags() {
        return mTags;
    }

    public String getImageLink() {
        return mImageLink;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "mAuthor='" + mAuthor + '\'' +
                ", mAuthorId='" + mAuthorId + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mDescription='" + mTags + '\'' +
                ", mImageLink='" + mImageLink + '\'' +
                '}';
    }
}
