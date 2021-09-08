package org.techtown.dingdong.mypage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.techtown.dingdong.home.Share;

import java.util.ArrayList;

public class SalesResponse {

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

       @SerializedName("content")
       @Expose
       private ArrayList<Sales> sales = new ArrayList<>();

       public ArrayList<Sales> getSales() {
           return sales;
       }

       public void setSales(ArrayList<Sales> sales) {
           this.sales = sales;
       }
   }
}
