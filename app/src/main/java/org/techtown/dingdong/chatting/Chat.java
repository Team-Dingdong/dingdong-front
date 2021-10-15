package org.techtown.dingdong.chatting;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Chat {

    @SerializedName("message")
    @Expose
    private String content;
    @SerializedName("nickname")
    @Expose
    private String name;
    @SerializedName("profileImageUrl")
    @Expose
    private String profile=null;
    @SerializedName("sendTime")
    @Expose
    private String time;
    @SerializedName("isOwner")
    @Expose
    private String owner;
    private int viewType = 0;
    @SerializedName("type")
    @Expose
    private String type = "null";
    @SerializedName("userId")
    @Expose
    private String userId;

    public Chat(String content, String name, String profile, String time, String owner, int viewType) {
        this.content = content;
        this.name = name;
        this.profile = profile;
        this.time = time;
        this.owner = owner;
        this.viewType = viewType;
    }

    public Chat(String content, String name, String profile, String time, String owner, String id) {
        this.content = content;
        this.name = name;
        this.profile = profile;
        this.time = time;
        this.owner = owner;
        this.viewType = setViewType(id);
        //this.viewType = viewType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getViewType() {
        return viewType;
    }

    public int setViewType(String id) {
        if(id.equals(userId)){
            return this.viewType = ChatType.ViewType.RIGHT_CONTENT;
        }
        else{
            return this.viewType = ChatType.ViewType.LEFT_CONTENT;
        }

    }
}
