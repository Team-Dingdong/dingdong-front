package org.techtown.dingdong.login_register;

import com.google.gson.annotations.SerializedName;

public class AuthRequest {
    @SerializedName("to")
    public String ID;

    public AuthRequest(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
