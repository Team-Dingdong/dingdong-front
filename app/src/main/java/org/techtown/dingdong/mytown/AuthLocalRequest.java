package org.techtown.dingdong.mytown;

import com.google.gson.annotations.SerializedName;

public class AuthLocalRequest {
    @SerializedName("local1")
    int local1;

    @SerializedName("local2")
    int local2;

    public AuthLocalRequest(int local1, int local2) {
        this.local1 = local1;
        this.local2 = local2;
    }

    public int getLocal1() {
        return local1;
    }

    public void setLocal1(int local1) {
        this.local1 = local1;
    }

    public int getLocal2() {
        return local2;
    }

    public void setLocal2(int local2) {
        this.local2 = local2;
    }
}
