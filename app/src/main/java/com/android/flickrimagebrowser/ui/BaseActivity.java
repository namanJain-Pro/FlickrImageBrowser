package com.android.flickrimagebrowser.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.flickrimagebrowser.R;

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BasicActivity";
    static final String FLICKR_QUERY = "FLICKR_QUERY";
    static final String PHOTO_TRANSFER = "PHOTO_TRANSFER";

    void activateToolbar(boolean enableHomeButton) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            Toolbar myToolbar = findViewById(R.id.myToolbar);

            if (myToolbar != null) {
                setSupportActionBar(myToolbar);
                actionBar = getSupportActionBar();
            }
        }

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(enableHomeButton);
        }
    }
}
