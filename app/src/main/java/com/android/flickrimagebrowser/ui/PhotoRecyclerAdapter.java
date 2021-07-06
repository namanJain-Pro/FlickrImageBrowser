package com.android.flickrimagebrowser.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.flickrimagebrowser.R;
import com.android.flickrimagebrowser.data.Photo;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PhotoRecyclerAdapter extends RecyclerView.Adapter<PhotoRecyclerAdapter.PhotoViewHolder> {
    private static final String TAG = "PhotoRecyclerAdapter";
    private List<Photo> mPhotoList;
    private Context mContext;

    public PhotoRecyclerAdapter(List<Photo> photoList, Context context) {
        mPhotoList = photoList;
        mContext = context;
    }

    @NonNull
    @NotNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_photo_element, parent,false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PhotoRecyclerAdapter.PhotoViewHolder holder, int position) {

        Photo photo = mPhotoList.get(position);
        Picasso.get().load(photo.getImageLink())
                .error(R.drawable.ic_baseline_error_24)
                .into(holder.mImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressBar.setVisibility(View.GONE);
                        holder.mImageView.setVisibility(View.VISIBLE);

                        holder.mImageView.setAlpha(0f);
                        holder.mImageView.animate().setDuration(500).alpha(1f).start();
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });


        holder.mTextView.setText(photo.getTitle());
    }

    @Override
    public int getItemCount() {
        return ((mPhotoList != null) && (mPhotoList.size() != 0) ? mPhotoList.size() : 0);
    }

    void loadNewData(List<Photo> PhotoList) {
        mPhotoList = PhotoList;
        notifyDataSetChanged();
    }

    public Photo getPhoto(int position) {
        return ((mPhotoList != null) && (mPhotoList.size() != 0) ? mPhotoList.get(position) : null);
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        TextView mTextView;
        ProgressBar progressBar;

        public PhotoViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextView = itemView.findViewById(R.id.textView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
}
