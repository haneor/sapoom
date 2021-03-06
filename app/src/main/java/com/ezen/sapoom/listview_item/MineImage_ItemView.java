package com.ezen.sapoom.listview_item;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ezen.sapoom.R;
import com.squareup.picasso.Picasso;

public class MineImage_ItemView extends LinearLayout{
    private ImageView imageButton1;
    private ImageView imageButton2;
    private ImageView imageButton3;

    public MineImage_ItemView(Context context) {
        super(context);
        init(context);
    }

    public MineImage_ItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init (Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.mine_item, this, true);
        imageButton1 = findViewById(R.id.imageButton1);
        imageButton2 = findViewById(R.id.imageButton2);
        imageButton3 = findViewById(R.id.imageButton3);
    }

    public void setMineImageButton(String id1, String id2, String id3) {  // 피드백으로 3개 받아서 주소를 나눌수 있도록 함
        // id를 서버와 동기화하여서 서버에 올라간 이미지를 id로 가져온 후 그 이미지를 hotButton에 넣는다.
        UpDate_ItemView.PicassoTransformations.targetWidth = 200;
        Picasso.with(getContext()).load(id1).transform(UpDate_ItemView.PicassoTransformations.resizeTransformation).into(imageButton1);
        Picasso.with(getContext()).load(id2).transform(UpDate_ItemView.PicassoTransformations.resizeTransformation).into(imageButton2);
        Picasso.with(getContext()).load(id3).transform(UpDate_ItemView.PicassoTransformations.resizeTransformation).into(imageButton3);
    }

}
