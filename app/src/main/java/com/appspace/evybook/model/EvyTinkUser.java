package com.appspace.evybook.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by siwaweswongcharoen on 6/6/2016 AD.
 */
public class EvyTinkUser implements Parcelable {
    public String evyaccountid;
    public String evytinkaccountid;
    @SerializedName("firebase_uid") public String firebaseUid;
    public String publishtitle;
    public String evyfacebookid;
    public String iddatetime;
    public String imgprofile;
    public String organizatitle;
    public String promotitle;
    public String report;

    public EvyTinkUser() {
    }

    protected EvyTinkUser(Parcel in) {
        evyaccountid = in.readString();
        evytinkaccountid = in.readString();
        firebaseUid = in.readString();
        publishtitle = in.readString();
        evyfacebookid = in.readString();
        iddatetime = in.readString();
        imgprofile = in.readString();
        organizatitle = in.readString();
        promotitle = in.readString();
        report = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(evyaccountid);
        dest.writeString(evytinkaccountid);
        dest.writeString(firebaseUid);
        dest.writeString(publishtitle);
        dest.writeString(evyfacebookid);
        dest.writeString(iddatetime);
        dest.writeString(imgprofile);
        dest.writeString(organizatitle);
        dest.writeString(promotitle);
        dest.writeString(report);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EvyTinkUser> CREATOR = new Creator<EvyTinkUser>() {
        @Override
        public EvyTinkUser createFromParcel(Parcel in) {
            return new EvyTinkUser(in);
        }

        @Override
        public EvyTinkUser[] newArray(int size) {
            return new EvyTinkUser[size];
        }
    };
}
