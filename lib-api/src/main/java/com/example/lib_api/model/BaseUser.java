package com.example.lib_api.model;

import android.os.Parcel;
import android.os.Parcelable;

public class BaseUser implements Parcelable {

    private String name;
    private boolean pinned;

    public BaseUser() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeByte(this.pinned ? (byte) 1 : (byte) 0);
    }

    protected BaseUser(Parcel in) {
        this.name = in.readString();
        this.pinned = in.readByte() != 0;
    }

    public static final Creator<BaseUser> CREATOR = new Creator<BaseUser>() {
        @Override
        public BaseUser createFromParcel(Parcel source) {
            return new BaseUser(source);
        }

        @Override
        public BaseUser[] newArray(int size) {
            return new BaseUser[size];
        }
    };
}
