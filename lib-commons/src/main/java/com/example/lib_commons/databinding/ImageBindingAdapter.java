package com.example.lib_commons.databinding;

import android.widget.ImageView;

import com.bumptech.glide.Glide;

import androidx.databinding.BindingAdapter;

public class ImageBindingAdapter {

    @BindingAdapter("bind_image:url")
    public static void setUrl (ImageView view, String url) {
        Glide.with(view.getContext())
                .load(url)
                .fitCenter()
                .into(view);
    }
}
