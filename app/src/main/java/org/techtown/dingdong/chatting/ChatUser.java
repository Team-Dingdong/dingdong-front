package org.techtown.dingdong.chatting;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatUser {

    @SerializedName("nickname")
    @Expose
    private String username;
    @SerializedName("profileImageUrl")
    @Expose
    private String imgUrl;
    @SerializedName("owner")
    @Expose
    private String ismaster;
    @SerializedName("userId")
    @Expose
    private String id;

    public ChatUser(String username, String imgUrl, String ismaster) {
        this.username = username;
        this.imgUrl = imgUrl;
        this.ismaster = ismaster;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getIsmaster() {
        return ismaster;
    }

    public void setIsmaster(String ismaster) {
        this.ismaster = ismaster;
    }
}
