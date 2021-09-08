package org.techtown.dingdong.mypage;

import com.google.gson.annotations.SerializedName;

public class EstimateRequset {
    @SerializedName("type")
    String type;

    public EstimateRequset(String type) {
        this.type = type;
    }
}
