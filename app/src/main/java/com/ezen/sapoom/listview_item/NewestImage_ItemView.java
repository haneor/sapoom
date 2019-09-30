package com.ezen.sapoom.listview_item;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ezen.sapoom.R;
import com.squareup.picasso.Picasso;

public class NewestImage_ItemView extends LinearLayout{
    private ImageView newestItem1;
    private ImageView newestItem2;

    public NewestImage_ItemView(Context context) {
        super(context);
        init(context);
    }

    public NewestImage_ItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init (Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.newestimage_item, this, true);
        newestItem1 = findViewById(R.id.newestImage1);
        newestItem2 = findViewById(R.id.newestImage2);

    }

    public void setNewestImage (String id1, String id2) {
        // id를 서버와 동기화하여서 서버에 올라간 이미지를 id로 가져온 후 그 이미지를 hotButton에 넣는다.
        //imageButton1.setImageResource(id); // 프로필 설정 시 유저 이미지를 받아서 서버에 저장 후 여기로 가져 올수 있어야 함.
        UpDate_ItemView.PicassoTransformations.targetWidth = 200;
        Picasso.with(getContext())
                .load(id1)
                .transform(UpDate_ItemView.PicassoTransformations.resizeTransformation)
                .into((ImageView)findViewById(R.id.newestImage1));

        Picasso.with(getContext())
                .load(id2)
                .transform(UpDate_ItemView.PicassoTransformations.resizeTransformation)
                .into((ImageView)findViewById(R.id.newestImage2));
    }

}
