package org.techtown.dingdong.login_register;

import com.google.gson.annotations.SerializedName;

public class AuthNickRequset {
    @SerializedName("nickname")
    String nickname;

    public AuthNickRequset(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


}
