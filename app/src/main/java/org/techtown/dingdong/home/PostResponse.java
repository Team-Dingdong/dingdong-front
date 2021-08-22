package org.techtown.dingdong.home;

import android.content.ClipData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PostResponse {

    @SerializedName("data")
    @Expose
    private Data data;


    @SerializedName("code")
    @Expose
    private String result;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public class Data{

        @SerializedName("content")
        @Expose
        private ArrayList<Share> share;

        public ArrayList<Share> getShare() {
            return share;
        }

        public void setShare(ArrayList<Share> share) {
            this.share = share;
        }


    }
}
