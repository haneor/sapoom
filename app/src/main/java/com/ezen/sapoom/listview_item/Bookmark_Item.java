package com.ezen.sapoom.listview_item;

public class Bookmark_Item {
    private String user;
    private String nickname;
    private String substance;

    public Bookmark_Item(String user, String nickname, String substance) {
        this.user = user;
        this.nickname = nickname;
        this.substance = substance;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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
}
