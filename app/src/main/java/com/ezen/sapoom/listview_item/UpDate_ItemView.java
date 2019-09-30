package com.ezen.sapoom.listview_item;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ezen.sapoom.R;
import com.ezen.sapoom.commentitem.CommentActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.InputStream;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.lang.System.out;

public class UpDate_ItemView extends LinearLayout {


    public UpDate_ItemView(Context context) {
        super(context);
        init(context);
    }

    public UpDate_ItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private CircleImageView userProfileImage;
    private Button nicknameBtn;
    private ImageView imageView;
    private TextView goodText;
    private TextView substanceText;
    private TextView commentText;
    private ImageButton commentBtn;
    private Context context;
    private String image_id;

    public void init (Context context){
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.update_item, this, true);
        nicknameBtn = findViewById(R.id.nicknameBtn);
        // 버튼 클릭 리스너로 이 닉네임을 가지고 있는 사람의 작품 목록을 불러 와야함.
        userProfileImage = findViewById(R.id.mainActivity_imageView_userIcon);
        imageView = findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.rogo1);

        goodText = findViewById(R.id.goodText);

        substanceText = findViewById(R.id.substanceText);

        commentText = findViewById(R.id.commentText);

        commentBtn = findViewById(R.id.commentBtn);
        commentBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CommentActivity.class);
                intent.putExtra("image_id", image_id);
                getContext().startActivity(intent);
            }
        });

    }
// setMethod에서 아이디를 받아서 서버에서 id에 대한 정보를 받아와서 적용해야 함.
    public void setNicknameBtn(String id) {
        nicknameBtn.setText(id);
    }

    // 피카소를 이용해서 이미지의 용량 및 사이즈를 줄임.
    public void setImageView(final String id) {
        PicassoTransformations.targetWidth = 300;
        image_id = id;
        Picasso.with(getContext())
                .load(id)
                .transform(PicassoTransformations.resizeTransformation)
                .into(imageView);
    }

    public void setCommentText(String id) {
        commentText.setText(id);
    }

    public void setGoodText(String id) {
        goodText.setText(id);
    }

    public void setSubstanceText(String id) {
        substanceText.setText(id);
    }

    public void setUserProfileImage(final String id) {
        final Handler handler = new Handler();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final CircleImageView iv = (CircleImageView) findViewById(R.id.mainActivity_imageView_userIcon);
                    URL url = new URL(id);
                    InputStream is = url.openStream();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {  }
                    };
                    handler.postDelayed(runnable, 500);
                    Bitmap bm = BitmapFactory.decodeStream(is);
                    runnable = new Runnable() {
                        @Override
                        public void run() {  }
                    };
                    handler.postDelayed(runnable, 500);
                    final Bitmap finalBm = bm;
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            iv.setImageBitmap(finalBm);
                        }
                    });
                    int height = bm.getHeight();
                    int width = bm.getWidth();
                    if(width>500){ /* Bitmap이미지의 크기를 가로사이즈1000을 기준으로 비율에 맞게 조정하고 파일로저장! * 조정된 가로, 세로의 크기를 각각 height, width에 저장한다! */
                        bm = Bitmap.createScaledBitmap(bm, (width=150), (height = height*150/width), true);
                        bm.compress(Bitmap.CompressFormat.PNG, 70, out); }
                    iv.setImageBitmap(bm);
                } catch (Exception e) { }
            }
        });
        t.start();
    }

    public void setCommentBtn(int id) {
    }

    public void setGoodBtn(int id) {
    }

    public void setCommentList(int id) {
    }


    // 이미지 사이즈를 줄이기 위한 메소드
    public static class PicassoTransformations {
        public static int targetWidth = 400;
        public static Transformation resizeTransformation = new Transformation() {
            @Override
            public Bitmap transform(Bitmap source) {
                double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                int targetHeight = (int) (targetWidth * aspectRatio);
                Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                if (result != source) {
                    source.recycle();
                }
                return result;
            }
            @Override
            public String key() {
                return "resizeTransformation#" + System.currentTimeMillis();
            }
        };
    }
}
