package com.toss.im.test.hanyoung;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.example.lib_commons.util.ParameterContants;
import com.example.lib_commons.util.StringUtility;
import com.toss.im.test.hanyoung.feature.search.SearchActivity;

import androidx.annotation.NonNull;

public class UriParser {

    public enum SCHEME {
        TEST,
        HTTP,
        HTTPS,
        UNKNOWN;

        public static SCHEME parseOf (@NonNull Uri uri) {
            for (SCHEME scheme : SCHEME.values()) {
                if (StringUtility.equalsIgnoreCase(scheme.name(), uri.getScheme())) {
                    return scheme;
                }
            }

            return UNKNOWN;
        }
    }

    @NonNull
    public static Intent parseUri (Activity activity) {
        Intent intent = parseUriInternal(activity);
        if (intent == null) {
            intent = new Intent(activity, SearchActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        return intent;
    }

    private static Intent parseUriInternal (Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return null;
        }

        if (activity.getIntent() == null || activity.getIntent().getData() == null) {
            return null;
        }

        Uri uri = activity.getIntent().getData();
        SCHEME scheme = SCHEME.parseOf(uri);
        switch (scheme) {
            case TEST:
                Intent intent = new Intent(activity, SearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                String host = uri.getHost();
                if (StringUtility.equalsIgnoreCase(host, "search")) {
                    String keyword = uri.getQueryParameter("keyword");
                    intent.putExtra(ParameterContants.PARAM_SEARCH_DEFAULT_KEYWORD, keyword);
                }
                return intent;
            case HTTP:
            case HTTPS:
            case UNKNOWN:
            default:
                return null;
        }
    }
}
