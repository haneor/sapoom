package com.ezen.sapoom.listview_item;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ezen.sapoom.R;
import com.squareup.picasso.Picasso;

import static com.ezen.sapoom.MineActivity.IMAGE_URL;

public class Bookmark_itemView extends LinearLayout{
    ImageView user;
    TextView nickname;
    TextView substance;

    public Bookmark_itemView(Context context) {
        super(context);
        init(context);
    }

    public Bookmark_itemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init (Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.bookmark_item, this, true);
        user = findViewById(R.id.user);
        nickname = findViewById(R.id.nickname);
        substance = findViewById(R.id.substance);
    }



    public void setUser(String id) {
        Picasso.with(getContext()).load(id).into((ImageView)findViewById(R.id.user));
    }

    public void setNickname(String id) {
        nickname.setText(id);
    }

    public void setSubstance(String id) {
        substance.setText(id);
    }
}
