package org.techtown.dingdong.chatting;

public class ChatUser {

    private String username;
    private String imgUrl;
    private Boolean ismaster;

    public ChatUser(String username, String imgUrl, Boolean ismaster) {
        this.username = username;
        this.imgUrl = imgUrl;
        this.ismaster = ismaster;
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

    public Boolean getIsmaster() {
        return ismaster;
    }

    public void setIsmaster(Boolean ismaster) {
        this.ismaster = ismaster;
    }
}
