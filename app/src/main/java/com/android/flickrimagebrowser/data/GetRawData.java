package com.android.flickrimagebrowser.data;

import android.os.AsyncTask;
import android.util.Log;

import com.android.flickrimagebrowser.DownloadStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;



public class GetRawData extends AsyncTask<String, Void, String> {
    private static final String TAG = "GetRawData";
    private final OnDownloadComplete mCallback;
    private DownloadStatus mDownloadStatus;

    public GetRawData(OnDownloadComplete callback) {
        this.mDownloadStatus = DownloadStatus.IDLE;
        mCallback = callback;
    }

    interface OnDownloadComplete {
        void onDownloadComplete(String rawJson, DownloadStatus downloadStatus);
    }

    @Override
    protected void onPostExecute(String s) {
        mCallback.onDownloadComplete(s, mDownloadStatus);
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String line;
        if (strings == null) {
            this.mDownloadStatus = DownloadStatus.NOT_INITIALISED;
            return null;
        }

        try {
            mDownloadStatus = DownloadStatus.PROCESSING;
            URL url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            Log.d(TAG, "doInBackground Response Code : " + responseCode);
            connection.connect();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuilder rawData = new StringBuilder();
            while (null != (line = reader.readLine())) {
                rawData.append(line);
                rawData.append("\n");
            }

            mDownloadStatus = DownloadStatus.OK;
            return rawData.toString();
        } catch (MalformedURLException e) {
            Log.e(TAG, "doInBackground: Invalid URL, " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: Error in reading data, " + e.getMessage());
        } catch (SecurityException e) {
            Log.e(TAG, "doInBackground: Security exception, need permission : " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground: Error in closing stream" + e.getMessage());
                }
            }
        }

        mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
        return null;
    }

    void runInSameThread(String s) {
        onPostExecute(doInBackground(s));
    }
}
