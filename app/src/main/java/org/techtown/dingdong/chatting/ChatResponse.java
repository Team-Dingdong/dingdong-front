package org.techtown.dingdong.chatting;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ChatResponse {
    @SerializedName("code")
    @Expose
    private String result;

    @SerializedName("data")
    @Expose
    private ArrayList<Chat> chats;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ArrayList<Chat> getChats() {
        return chats;
    }

    public void setChats(ArrayList<Chat> chats) {
        this.chats = chats;
    }
}
