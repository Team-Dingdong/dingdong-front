package org.techtown.dingdong.profile;

import com.google.gson.annotations.SerializedName;

public class UserProfileRequest {
    @SerializedName("nickname")
    String nickname;

    public UserProfileRequest(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
