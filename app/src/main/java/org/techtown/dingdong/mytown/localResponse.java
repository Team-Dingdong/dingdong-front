package org.techtown.dingdong.mytown;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class localResponse {
    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("data")
    @Expose
    public List<TownItem> data;

    @SerializedName("code")
    public String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<TownItem> getData() {
        return data;
    }

    public void setData(List<TownItem> data) {
        this.data = data;
    }
}
