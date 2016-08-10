package com.appspace.evybook.util;

import android.app.Activity;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;

import com.appspace.appspacelibrary.util.LoggerUtils;
import com.appspace.evybook.R;

/**
 * Created by siwaweswongcharoen on 8/10/2016 AD.
 */
public class ChromeCustomTabUtil {
    public static void open(Activity context, String url) {
        LoggerUtils.log2D("Chrome_url", url);
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
        builder.setShowTitle(true);
        builder.setStartAnimations(context, R.anim.left_to_right_start, R.anim.right_to_left_start);
        builder.setExitAnimations(context, R.anim.right_to_left_end, R.anim.left_to_right_end);
        builder.enableUrlBarHiding();

        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(context, Uri.parse(url));
    }
}
