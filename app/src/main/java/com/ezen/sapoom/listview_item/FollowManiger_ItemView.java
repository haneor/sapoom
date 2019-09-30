package com.ezen.sapoom.listview_item;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ezen.sapoom.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.ezen.sapoom.MineActivity.IMAGE_URL;

public class FollowManiger_ItemView extends LinearLayout {

    public FollowManiger_ItemView(Context context) {
        super(context);
        init(context);
    }

    public FollowManiger_ItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    CircleImageView user;
    TextView nickname;
    TextView substance;

    public void init (Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.followmaniger_item, this, true);

        user = findViewById(R.id.user);
        nickname = findViewById(R.id.nickname);
        substance = findViewById(R.id.substance);


    }

    public void setUser(String id) {
        Picasso.with(getContext())
                .load(IMAGE_URL+id)
                .into((CircleImageView)findViewById(R.id.user));
    }

    public void setNickname(String id) {
        nickname.setText(id);
    }

    public void setSubstance(String id) {
        substance.setText(id);
    }
}
