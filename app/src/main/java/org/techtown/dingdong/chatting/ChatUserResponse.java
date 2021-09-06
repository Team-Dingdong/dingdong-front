package org.techtown.dingdong.chatting;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ChatUserResponse {
    @SerializedName("code")
    @Expose
    private String result;


    @SerializedName("data")
    @Expose
    private ArrayList<ChatUser> chatUsers;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ArrayList<ChatUser> getChatUsers() {
        return chatUsers;
    }

    public void setChatUsers(ArrayList<ChatUser> chatUsers) {
        this.chatUsers = chatUsers;
    }
}
