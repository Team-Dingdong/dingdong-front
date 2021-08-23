package org.techtown.dingdong.login_register;
import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

public class AuthResponse {
   @SerializedName("result")
    public String result;
   @SerializedName("data")
   public Data data;

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

        @SerializedName("requestTime")
       public String requestTime;


        public String getRequestTime() {
            return requestTime;
        }

        public void setRequestTime(String requestTime) {
            this.requestTime = requestTime;
        }
    }
}
