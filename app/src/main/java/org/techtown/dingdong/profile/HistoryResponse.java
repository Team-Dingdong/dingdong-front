package org.techtown.dingdong.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.techtown.dingdong.home.Share;

import java.util.ArrayList;

public class HistoryResponse {

    @SerializedName("code")
    @Expose
    private String result;

    @SerializedName("data")
    @Expose
    private ArrayList<Share> historys;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ArrayList<Share> getHistorys() {
        return historys;
    }

    public void setHistorys(ArrayList<Share> historys) {
        this.historys = historys;
    }
}
