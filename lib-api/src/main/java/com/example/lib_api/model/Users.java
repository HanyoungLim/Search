package com.example.lib_api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Users implements Parcelable {

    @SerializedName("contacts")
    private List<ContactUser> contactUserList;

    @SerializedName("accounts")
    private List<AccountUser> accountUserList;

    public List<ContactUser> getContactUserList() {
        return contactUserList;
    }

    public List<AccountUser> getAccountUserList() {
        return accountUserList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.contactUserList);
        dest.writeTypedList(this.accountUserList);
    }

    public Users() {
    }

    protected Users(Parcel in) {
        this.contactUserList = in.createTypedArrayList(ContactUser.CREATOR);
        this.accountUserList = in.createTypedArrayList(AccountUser.CREATOR);
    }

    public static final Parcelable.Creator<Users> CREATOR = new Parcelable.Creator<Users>() {
        @Override
        public Users createFromParcel(Parcel source) {
            return new Users(source);
        }

        @Override
        public Users[] newArray(int size) {
            return new Users[size];
        }
    };
}
