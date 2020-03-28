package com.example.lib_api.model;

import android.os.Parcel;

public class ContactUser extends BaseUser {

    private String profileImageUrl;
    private String phoneNumber;

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.profileImageUrl);
        dest.writeString(this.phoneNumber);
    }

    public ContactUser() {
    }

    protected ContactUser(Parcel in) {
        super(in);
        this.profileImageUrl = in.readString();
        this.phoneNumber = in.readString();
    }

    public static final Creator<ContactUser> CREATOR = new Creator<ContactUser>() {
        @Override
        public ContactUser createFromParcel(Parcel source) {
            return new ContactUser(source);
        }

        @Override
        public ContactUser[] newArray(int size) {
            return new ContactUser[size];
        }
    };
}
