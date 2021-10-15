package org.techtown.dingdong.login_register;

import com.google.gson.annotations.SerializedName;

public class TokenRefreshRequest {

    @SerializedName("accessToken")
    String access;

    @SerializedName("refreshToken")
    String refresh;

    public TokenRefreshRequest(String access, String refresh) {
        this.access = access;
        this.refresh = refresh;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getRefresh() {
        return refresh;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }
}
