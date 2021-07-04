 package com.android.flickrimagebrowser.data;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.android.flickrimagebrowser.DownloadStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ParseFlickrJsonData extends AsyncTask<String, Void, List<Photo>> implements GetRawData.OnDownloadComplete {
    private static final String TAG = "ParseFlickrJsonData";
    private List<Photo> mPhotoList = null;
    private String mBaseURL;
    private boolean mMatchAll;

    private final OnDataAvailable mCallBack;

    public interface OnDataAvailable {
        void onDataAvailable(List<Photo> data, DownloadStatus downloadStatus);
    }

    public ParseFlickrJsonData(OnDataAvailable callBack, String baseURL, boolean matchAll) {
        mCallBack = callBack;
        mBaseURL = baseURL;
        mMatchAll = matchAll;
    }

    String createUri (String searchCriteria) {
        return Uri.parse(mBaseURL).buildUpon()
                .appendQueryParameter("tags", searchCriteria)
                .appendQueryParameter("tagmode",mMatchAll?"ALL":"ANY")
                .appendQueryParameter("format","json")
                .appendQueryParameter("nojsoncallback","1")
                .build().toString();
    }

    @Override
    protected void onPostExecute(List<Photo> photos) {
        if (mCallBack != null) {
            mCallBack.onDataAvailable(mPhotoList, DownloadStatus.OK);
        }
    }

    @Override
    protected List<Photo> doInBackground(String... strings) {
        String uri = createUri(strings[0]);
        Log.d(TAG, "executeOnSameThread: URL = " + uri);

        GetRawData getRawData = new GetRawData(this);
        getRawData.runInSameThread(uri);
        return mPhotoList;
    }

    @Override
    public void onDownloadComplete(String rawJson, DownloadStatus downloadStatus) {
        if(downloadStatus == DownloadStatus.OK) {
            mPhotoList = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(rawJson);
                JSONArray photoArray = jsonObject.getJSONArray("items");

                for (int i = 0; i < photoArray.length(); i++) {
                    JSONObject photoObject = photoArray.getJSONObject(i);
                    String author = photoObject.getString("author");
                    String authorID = photoObject.getString("author_id");
                    String title = photoObject.getString("title");
                    String tags = photoObject.getString("tags");

                    JSONObject imageObject = photoObject.getJSONObject("media");
                    String imageLink = imageObject.getString("m").replace("_m.", "_b.");

                    Photo photo = new Photo(author, authorID, title, tags, imageLink);
                    mPhotoList.add(photo);
                }
            } catch (JSONException e) {
                Log.e(TAG, "onDownloadComplete: Error in Parsing JSON " + e.getMessage() + " status : " + downloadStatus);
                downloadStatus = DownloadStatus.FAILED_OR_EMPTY;
            }
        }
    }
}
