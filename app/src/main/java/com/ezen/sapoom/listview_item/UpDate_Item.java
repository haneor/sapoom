package com.ezen.sapoom.listview_item;

public class UpDate_Item {

    private String nickname;
    private String imageView;
    private String substance;
    private String comment;
    private String userprofile;
    private String good;

    public UpDate_Item(String nickname , String imageView , String substance , String comment , String userprofile , String good) {
        this.nickname = nickname;
        this.imageView = imageView;
        this.substance = substance;
        this.comment = comment;
        this.userprofile = userprofile;
        this.good = good;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getImageView() {
        return imageView;
    }

    public void setImageView(String imageView) {
        this.imageView = imageView;
    }

    public String getSubstance() {
        return substance;
    }

    public void setSubstance(String substance) {
        this.substance = substance;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserprofile() {
        return userprofile;
    }

    public void setUserprofile(String userprofile) {
        this.userprofile = userprofile;
    }

    public String getGood() {
        return good;
    }

    public void setGood(String good) {
        this.good = good;
    }
}
