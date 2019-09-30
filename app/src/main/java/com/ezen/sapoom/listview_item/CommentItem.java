package com.ezen.sapoom.listview_item;

public class CommentItem {

    private String nickname_comment;
    private String comment_comment;
    private int good_comment;

    public CommentItem(String nickname_comment, String comment_comment, int good_comment) {
        this.nickname_comment = nickname_comment;
        this.comment_comment = comment_comment;
        this.good_comment = good_comment;
    }

    public String getNickname_comment() {
        return nickname_comment;
    }

    public void setNickname_comment(String nickname_comment) {
        this.nickname_comment = nickname_comment;
    }

    public String getComment_comment() {
        return comment_comment;
    }

    public void setComment_comment(String comment_comment) {
        this.comment_comment = comment_comment;
    }

    public int getGood_comment() {
        return good_comment;
    }

    public void setGood_comment(int good_comment) {
        this.good_comment = good_comment;
    }
}
