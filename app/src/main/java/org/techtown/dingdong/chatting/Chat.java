package org.techtown.dingdong.chatting;

public class Chat {

    private String content;
    private String name;
    private String profile;
    private String time;
    private Boolean isMaster;
    private int viewType;

    public Chat(String content, String name, String profile, String time, Boolean isMaster, int viewType) {
        this.content = content;
        this.name = name;
        this.profile = profile;
        this.time = time;
        this.isMaster = isMaster;
        this.viewType = viewType;
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

    public Boolean getMaster() {
        return isMaster;
    }

    public void setMaster(Boolean master) {
        isMaster = master;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
