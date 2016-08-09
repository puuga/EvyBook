package com.appspace.evybook.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by siwaweswongcharoen on 6/6/2016 AD.
 */
public class EvyTinkUser implements Parcelable {
    @SerializedName("evyaccountid") public String accountId;
    @SerializedName("evytinkaccountid") public String evytinkAccountId;
    @SerializedName("firebase_uid") public String firebaseUid;
    @SerializedName("publishtitle") public String publishTitle;
    @SerializedName("evyfacebookid") public String facebookId;
    @SerializedName("iddatetime") public String idDateTime;
    @SerializedName("imgprofile") public String imgProfile;
    @SerializedName("organizatitle") public String organizaTitle;
    @SerializedName("promotitle") public String promoTitle;
    @SerializedName("report") public String report;

    public EvyTinkUser() {
    }

    protected EvyTinkUser(Parcel in) {
        accountId = in.readString();
        evytinkAccountId = in.readString();
        firebaseUid = in.readString();
        publishTitle = in.readString();
        facebookId = in.readString();
        idDateTime = in.readString();
        imgProfile = in.readString();
        organizaTitle = in.readString();
        promoTitle = in.readString();
        report = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(accountId);
        dest.writeString(evytinkAccountId);
        dest.writeString(firebaseUid);
        dest.writeString(publishTitle);
        dest.writeString(facebookId);
        dest.writeString(idDateTime);
        dest.writeString(imgProfile);
        dest.writeString(organizaTitle);
        dest.writeString(promoTitle);
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
