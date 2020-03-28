package com.example.lib_commons.databinding;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import androidx.databinding.BindingAdapter;

public class BaseBindingAdapter {

    @BindingAdapter("bind:visible")
    public static void setVisible (View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
}
