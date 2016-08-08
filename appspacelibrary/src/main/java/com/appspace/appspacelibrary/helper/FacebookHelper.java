package com.appspace.appspacelibrary.helper;

/**
 * Created by siwaweswongcharoen on 3/29/2016 AD.
 */
public class FacebookHelper {
    public enum Size {
        SMALL, NORMAL, LARGE, SQUARE
    }

    public static String getFacebookProfilePictureUrl(String facebookId) {
        return getFacebookProfilePictureUrl(facebookId, Size.LARGE);
    }

    public static String getFacebookProfilePictureUrl(String facebookId, Size size) {

        String base = "https://graph.facebook.com/" + facebookId + "/picture?type=";
        switch (size) {
            case SMALL:
                return base.concat("small");
            case NORMAL:
                return base.concat("normal");
            case LARGE:
                return base.concat("large");
            case SQUARE:
                return base.concat("square");
        }
        return base.concat("large");
    }
}
