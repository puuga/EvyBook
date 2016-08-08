package com.appspace.appspacelibrary.util;

import android.util.Log;

/**
 * Created by siwaweswongcharoen on 1/25/2016 AD.
 */
public class LoggerUtils {
    private boolean isLogEnabled = false;

    private static LoggerUtils instance;

    public static LoggerUtils getInstance() {
        if (instance == null)
            instance = new LoggerUtils();
        return instance;
    }

    private LoggerUtils() {

    }

    public void setLogEnabled(boolean isLogEnabled) {
        this.isLogEnabled = isLogEnabled;
    }

    public void logI(String tag, String message) {
        if (isLogEnabled) {
            Log.i(tag, message);
        }
    }

    public void logD(String tag, String message) {
        if (isLogEnabled) {
            Log.d(tag, message);
        }
    }

    public void logDT(String tag, String message, Throwable throwable) {
        if (isLogEnabled) {
            Log.d(tag, message, throwable);
        }
    }

    public void logE(String tag, String message, Throwable throwable) {
        if (isLogEnabled) {
            Log.e(tag, message, throwable);
        }
    }

    public static void log2I(String tag, String message) {
        if (getInstance().isLogEnabled) {
            Log.i(tag, message);
        }
    }

    public static void log2D(String tag, String message) {
        if (getInstance().isLogEnabled) {
            Log.d(tag, message);
        }
    }

    public static void log2DT(String tag, String message, Throwable throwable) {
        if (getInstance().isLogEnabled) {
            Log.d(tag, message, throwable);
        }
    }

    public static void log2E(String tag, String message, Throwable throwable) {
        if (getInstance().isLogEnabled) {
            Log.e(tag, message, throwable);
        }
    }
}
