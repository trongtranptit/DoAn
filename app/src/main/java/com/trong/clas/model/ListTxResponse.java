package com.trong.clas.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ListTxResponse implements Parcelable {
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("result")
    private ArrayList<Transaction> result;

    public ListTxResponse() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Transaction> getResult() {
        return result;
    }

    public void setResult(ArrayList<Transaction> result) {
        this.result = result;
    }

    protected ListTxResponse(Parcel in) {
        status = in.readString();
        message = in.readString();
    }

    public static final Creator<ListTxResponse> CREATOR = new Creator<ListTxResponse>() {
        @Override
        public ListTxResponse createFromParcel(Parcel in) {
            return new ListTxResponse(in);
        }

        @Override
        public ListTxResponse[] newArray(int size) {
            return new ListTxResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(status);
        parcel.writeString(message);
    }
}
