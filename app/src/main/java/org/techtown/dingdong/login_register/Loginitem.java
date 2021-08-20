package org.techtown.dingdong.login_register;

import com.google.gson.annotations.SerializedName;

public class Loginitem {
    @SerializedName("status")
    public int status;

    @SerializedName("data")
    public LoginResponse loginResponse;

}
