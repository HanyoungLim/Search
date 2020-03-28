package com.example.lib_commons.databinding;

import android.widget.TextView;

import com.example.lib_commons.ApplicationContextWrapper;

import androidx.databinding.BindingAdapter;

public class TextBindingAdapter {

    @BindingAdapter("bind_text:text")
    public static void setText (TextView view, String text) {
        ApplicationContextWrapper.getApplication();
        view.setText(text);
    }
}
