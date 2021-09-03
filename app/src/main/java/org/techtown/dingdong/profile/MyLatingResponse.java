package org.techtown.dingdong.profile;

import com.google.gson.annotations.SerializedName;

public class MyLatingResponse {

    @SerializedName("code")
    public String code;

    @SerializedName("data")
    public MyLatingResponse.Data data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        @SerializedName("good")
        int good;

        @SerializedName("bad")
        int bad;

        public int getBad() {
            return bad;
        }

        public void setBad(int bad) {
            this.bad = bad;
        }

        public int getGood() {
            return good;
        }

        public void setGood(int good) {
            this.good = good;
        }
    }

}
