package org.techtown.dingdong.chatting;

import java.util.ArrayList;

public class ChatRoom {
    private String image;
    private ArrayList<Chat> chats;
    private String title;
    private String personnel;
    private String master;
    //private ArrayList<User> users;

    public ChatRoom(String image, ArrayList<Chat> chats, String title, String personnel) {
        this.image = image;
        this.chats = chats;
        this.title = title;
        this.personnel = personnel;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<Chat> getChats() {
        return chats;
    }

    public void setChats(ArrayList<Chat> chats) {
        this.chats = chats;
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
}
