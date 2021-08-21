package org.techtown.dingdong.login_register;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("accessToken")
    public String accesstoken;

    public LoginResponse(String accesstoken) {
        this.accesstoken = accesstoken;
    }


    public String getAccesstoken() {
        return accesstoken;
    }

    public void setAccesstoken(String accesstoken) {
        this.accesstoken = accesstoken;
    }
}
