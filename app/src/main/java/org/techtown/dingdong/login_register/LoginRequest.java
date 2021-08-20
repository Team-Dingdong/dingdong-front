package org.techtown.dingdong.login_register;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    @SerializedName("phone")
    public String ID;

    @SerializedName("authNumber")
    public String PW;

    public LoginRequest(String ID, String PW) {
        this.ID = ID;
        this.PW = PW;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPW() {
        return PW;
    }

    public void setPW(String PW) {
        this.PW = PW;
    }


}
