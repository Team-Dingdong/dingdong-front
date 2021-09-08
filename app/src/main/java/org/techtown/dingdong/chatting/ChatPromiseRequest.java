package org.techtown.dingdong.chatting;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatPromiseRequest {
    @SerializedName("promiseDate")
    @Expose
    private String promiseDate;
    @SerializedName("promiseTime")
    @Expose
    private String promiseTime;
    @SerializedName("promiseLocal")
    @Expose
    private String promiseLocal;

    public ChatPromiseRequest(String promiseDate, String promiseTime, String promiseLocal) {
        this.promiseDate = promiseDate;
        this.promiseTime = promiseTime;
        this.promiseLocal = promiseLocal;
    }
}
