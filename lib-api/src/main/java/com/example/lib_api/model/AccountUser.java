package com.example.lib_api.model;

import android.os.Parcel;

public class AccountUser extends BaseUser {

    private String bankName;
    private String bankLogoUrl;
    private String bankAccountNo;

    public String getBankName() {
        return bankName;
    }

    public String getBankLogoUrl() {
        return bankLogoUrl;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.bankName);
        dest.writeString(this.bankLogoUrl);
        dest.writeString(this.bankAccountNo);
    }

    public AccountUser() {
    }

    protected AccountUser(Parcel in) {
        super(in);
        this.bankName = in.readString();
        this.bankLogoUrl = in.readString();
        this.bankAccountNo = in.readString();
    }

    public static final Creator<AccountUser> CREATOR = new Creator<AccountUser>() {
        @Override
        public AccountUser createFromParcel(Parcel source) {
            return new AccountUser(source);
        }

        @Override
        public AccountUser[] newArray(int size) {
            return new AccountUser[size];
        }
    };
}
