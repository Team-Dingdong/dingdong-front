package org.techtown.dingdong.mytown;

import com.google.gson.annotations.SerializedName;

public class AuthLocalResponse {
    @SerializedName("code")
    String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
