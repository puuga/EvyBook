package com.appspace.evybook.util;

import android.net.Uri;

import com.appspace.appspacelibrary.util.FacebookApiUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

/**
 * Created by siwaweswongcharoen on 8/8/2016 AD.
 */
public class FirebaseUserUtil {
    public static void updateProfilePhotoUri(OnCompleteListener<Void> listener) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uri = FacebookApiUtil.getFacebookProfilePicture(
                    DataStoreUtils.getInstance().getFacebookId(),
                    FacebookApiUtil.FacebookProfilePictureSize.LARGE
            );
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(Uri.parse(uri))
                    .build();
            user.updateProfile(profileUpdates).addOnCompleteListener(listener);
        }
    }
}
