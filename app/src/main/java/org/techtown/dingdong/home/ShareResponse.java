package org.techtown.dingdong.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShareResponse {

    @SerializedName("data")
    @Expose
    public Share share;

    @SerializedName("code")
    @Expose
    private String result;

    public Share getShare() {
        return share;
    }

    public void setShare(Share share) {
        this.share = share;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
