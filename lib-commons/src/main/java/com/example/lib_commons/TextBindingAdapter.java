package com.example.lib_commons;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;

public class TextBindingAdapter {

    @BindingAdapter("bind_text:text")
    public static void setText (TextView view, String text) {
        view.setText(text);
    }
}
