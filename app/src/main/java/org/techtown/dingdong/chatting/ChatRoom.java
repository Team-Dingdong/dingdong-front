package org.techtown.dingdong.chatting;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ChatRoom {
    @SerializedName("imageUrl")
    @Expose
    private String image;
    //private ArrayList<Chat> chats;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("userCount")
    @Expose
    private String personnel;
    //private String master;
    @SerializedName("lastChatMessage")
    @Expose
    private String lastMsg;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("lastChatTime")
    @Expose
    private String lastChatTime;
    @SerializedName("owner")
    @Expose
    private String isOwner;
    //private ArrayList<User> users;


    public ChatRoom(String image, String title, String personnel, String lastMsg, String id, String lastChatTime) {
        this.image = image;
        this.title = title;
        this.personnel = personnel;
        this.lastMsg = lastMsg;
        this.id = id;
        this.lastChatTime = lastChatTime;
    }

    public String getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(String isOwner) {
        this.isOwner = isOwner;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPersonnel() {
        return personnel;
    }

    public void setPersonnel(String personnel) {
        this.personnel = personnel;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastChatTime() {
        return lastChatTime;
    }

    public void setLastChatTime(String lastChatTime) {
        this.lastChatTime = lastChatTime;
    }
}
