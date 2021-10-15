package org.techtown.dingdong.mypage;

import com.google.gson.annotations.SerializedName;

public class UserRatingRequest {
    @SerializedName("type")
    public String type;

    public UserRatingRequest(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
