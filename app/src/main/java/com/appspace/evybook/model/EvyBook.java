package com.appspace.evybook.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by siwaweswongcharoen on 8/9/2016 AD.
 */
public class EvyBook implements Parcelable {
    @SerializedName("eventevyid") public String bookId;
    @SerializedName("ddatetime") public String dateTime;
    @SerializedName("filename") public String fileUrl;
    @SerializedName("ebookfilename") public String fileName;
    @SerializedName("coverfilename") public String coverUrl;
    @SerializedName("publisher") public String publisher;
    @SerializedName("author") public String author;
    @SerializedName("downloadstatus") public String downloadStatus;
    @SerializedName("title") public String title;
    @SerializedName("bookshelfid") public String bookShelfId;
    @SerializedName("user") public EvyTinkUser user;

    public EvyBook() {

    }

    protected EvyBook(Parcel in) {
        bookId = in.readString();
        dateTime = in.readString();
        fileUrl = in.readString();
        fileName = in.readString();
        coverUrl = in.readString();
        publisher = in.readString();
        author = in.readString();
        downloadStatus = in.readString();
        title = in.readString();
        bookShelfId = in.readString();
        user = in.readParcelable(EvyTinkUser.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bookId);
        dest.writeString(dateTime);
        dest.writeString(fileUrl);
        dest.writeString(fileName);
        dest.writeString(coverUrl);
        dest.writeString(publisher);
        dest.writeString(author);
        dest.writeString(downloadStatus);
        dest.writeString(title);
        dest.writeString(bookShelfId);
        dest.writeParcelable(user, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EvyBook> CREATOR = new Creator<EvyBook>() {
        @Override
        public EvyBook createFromParcel(Parcel in) {
            return new EvyBook(in);
        }

        @Override
        public EvyBook[] newArray(int size) {
            return new EvyBook[size];
        }
    };
}
