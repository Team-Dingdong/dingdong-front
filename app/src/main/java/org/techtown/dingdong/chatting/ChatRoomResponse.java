package org.techtown.dingdong.chatting;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ChatRoomResponse {

    @SerializedName("code")
    @Expose
    private String result;


    @SerializedName("data")
    @Expose
    private ArrayList<ChatRoom> chatRooms;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ArrayList<ChatRoom> getChatRooms() {
        return chatRooms;
    }

    public void setChatRooms(ArrayList<ChatRoom> chatRooms) {
        this.chatRooms = chatRooms;
    }
}
