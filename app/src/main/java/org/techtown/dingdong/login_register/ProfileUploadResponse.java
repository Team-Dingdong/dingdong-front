package org.techtown.dingdong.login_register;

import com.google.gson.annotations.SerializedName;

public class ProfileUploadResponse {

    @SerializedName("code")
   String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
