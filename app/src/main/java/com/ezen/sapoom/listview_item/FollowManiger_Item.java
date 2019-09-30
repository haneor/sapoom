package com.ezen.sapoom.listview_item;

public class FollowManiger_Item {

    private String nickname;
    private String substance;
    private String user_image;

    public FollowManiger_Item(String nickname, String substance, String user_image) {
        this.nickname = nickname;
        this.substance = substance;
        this.user_image = user_image;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSubstance() {
        return substance;
    }

    public void setSubstance(String substance) {
        this.substance = substance;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }
}
