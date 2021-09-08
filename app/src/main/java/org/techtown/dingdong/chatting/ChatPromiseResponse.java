package org.techtown.dingdong.chatting;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatPromiseResponse {
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
        @SerializedName("promiseDate")
        @Expose
        private String promiseDate;
        @SerializedName("promiseTime")
        @Expose
        private String promiseTime;
        @SerializedName("promiseEndTime")
        @Expose
        private String promiseEndTime;
        @SerializedName("promiseLocal")
        @Expose
        private String promiseLocal;

        public String getPromiseDate() {
            return promiseDate;
        }

        public void setPromiseDate(String promiseDate) {
            this.promiseDate = promiseDate;
        }

        public String getPromiseTime() {
            return promiseTime;
        }

        public void setPromiseTime(String promiseTime) {
            this.promiseTime = promiseTime;
        }

        public String getPromiseEndTime() {
            return promiseEndTime;
        }

        public void setPromiseEndTime(String promiseEndTime) {
            this.promiseEndTime = promiseEndTime;
        }

        public String getPromiseLocal() {
            return promiseLocal;
        }

        public void setPromiseLocal(String promiseLocal) {
            this.promiseLocal = promiseLocal;
        }
    }
}
