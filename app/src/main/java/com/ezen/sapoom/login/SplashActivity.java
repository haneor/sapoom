package com.ezen.sapoom.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ezen.sapoom.R;

public class SplashActivity extends AppCompatActivity {

    private LinearLayout linearLayout;
    private String splash_message;
    private ImageView imageView;
    private Handler handler;
    private Runnable runnable;
    public static Boolean keepLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        linearLayout = (LinearLayout) findViewById(R.id.splashactivity_linearlayout);
        imageView = findViewById(R.id.splashactivity_image);
        // 여기에 서버에서 구성하는 데이터를 셋팅을 진행 한다.

        if(splash_message != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(splash_message).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            builder.create().show();
        }else {
            runnable = new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            };
            handler = new Handler();
            handler.postDelayed(runnable, 1000);
        }

    }
}
