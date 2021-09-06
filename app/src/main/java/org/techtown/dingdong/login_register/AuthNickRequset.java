package org.techtown.dingdong.login_register;

import com.google.gson.annotations.SerializedName;

public class AuthNickRequset {
    @SerializedName("nickname")
    String nickname;

    public AuthNickRequset(String nickname) {
        this.nickname = nickname;
    }

}
