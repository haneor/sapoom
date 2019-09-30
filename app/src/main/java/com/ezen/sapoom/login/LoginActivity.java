package com.ezen.sapoom.login;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.ezen.sapoom.MainActivity;
import com.ezen.sapoom.R;
import com.ezen.sapoom.request.LoginRequest;

import org.json.JSONObject;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private Button login;
    private Button signup;
    private EditText login_id;
    private EditText login_pw;
    public static String db_id;
    private String db_pw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login);

        // 로그인 유지 하기 위한 메소드
        if(SplashActivity.keepLogin) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        login_id = findViewById(R.id.loginActivity_edittext_id);
        login_id.setFilters(new InputFilter[] {filterAlphaNumber});
        login_pw = findViewById(R.id.loginActivity_edittext_pw);
        login = findViewById(R.id.loginActivity_button_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("LoginActivity", "onclick");
                db_id = login_id.getText().toString();
                db_pw = login_pw.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("LoginActivity", "onresponse");
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success) {
                                Toast.makeText(LoginActivity.this, "로딩중...조금만 기다리세요.", Toast.LENGTH_LONG).show();
                                // 자동 로그인 기능
                                SplashActivity.keepLogin = true;
                                // 로그인된 아이디를 저장
                                MainActivity.STATIC_ID = db_id;
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("계정을 다시 확인하세요.").setNegativeButton("다시 시도",null).show();
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                LoginRequest loginRequest = new LoginRequest(db_id, db_pw, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });

        signup = findViewById(R.id.loginActivity_button_signup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

    }
    // 영문 및 숫자만 입력이 가능하도록 하는 메소드 생성
    public InputFilter filterAlphaNumber = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[a-zA-Z0-9]+$");
            if (!ps.matcher(source).matches()) {
                return "";
            }
            return null;
        }
    };

}
