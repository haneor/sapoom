package com.ezen.sapoom.listview_item;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ezen.sapoom.R;

public class CommentItemView extends LinearLayout {

    private TextView nickname_comment;
    private TextView comment_comment;
    private ImageView good_comment;

    public CommentItemView(Context context) {
        super(context);
        init(context);
    }

    public CommentItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init (Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        // 만들어진 singer_item을 인플레이트 한다.
        inflater.inflate(R.layout.comment_item, this, true);

        nickname_comment = findViewById(R.id.nicknameText);
        comment_comment = findViewById(R.id.commentText);
        good_comment = findViewById(R.id.goodimage);
    }

    public void setNickname(String nick) {
        nickname_comment.setText(nick);
    }

    public void setComment(String com) {
        comment_comment.setText(com);
    }

    public void setGood(int good) {
        good_comment.setImageResource(good);
    }
}
