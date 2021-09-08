package org.techtown.dingdong.mypage;

import com.google.gson.annotations.SerializedName;

public class EstimateResponse {
    @SerializedName("code")
    String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
