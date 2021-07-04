package com.android.flickrimagebrowser.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.flickrimagebrowser.R;
import com.android.flickrimagebrowser.data.Photo;
import com.squareup.picasso.Picasso;

public class PhotoDetailActivity extends BaseActivity {

    private TextView photoTitle, photoAuthor, photoTags;
    private ImageView photoImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        activateToolbar(true);

        Intent intent = getIntent();
        Photo photo = (Photo) intent.getSerializableExtra(PHOTO_TRANSFER);

        if (photo != null) {
            photoAuthor = findViewById(R.id.photo_author);
            photoImage = findViewById(R.id.photo_image);
            photoTitle = findViewById(R.id.photo_title);
            photoTags = findViewById(R.id.photo_tags);
            setPhotoContent(photo);
        }
    }

    private void setPhotoContent(Photo photo) {
        photoAuthor.setText(photo.getAuthor());
        Picasso.get().load(photo.getImageLink())
                .error(R.drawable.place_holder)
                .placeholder(R.drawable.place_holder)
                .into(photoImage);
        photoTitle.setText(photo.getTitle());
        String tag = "Tags: " + photo.getTags();
        photoTags.setText(tag);
    }
}