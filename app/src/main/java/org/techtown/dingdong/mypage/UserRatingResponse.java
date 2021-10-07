package org.techtown.dingdong.mypage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserRatingResponse {

    @SerializedName("code")
    @Expose
    private String result;

    @SerializedName("data")
    @Expose
    private Data data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{

        @SerializedName("good")
        @Expose
        private String good;

        @SerializedName("bad")
        @Expose
        private String bad;

        public String getGood() {
            return good;
        }

        public void setGood(String good) {
            this.good = good;
        }

        public String getBad() {
            return bad;
        }

        public void setBad(String bad) {
            this.bad = bad;
        }
    }

}
