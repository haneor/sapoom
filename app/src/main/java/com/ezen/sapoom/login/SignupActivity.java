package com.ezen.sapoom.login;

import android.app.AlertDialog;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.ezen.sapoom.R;
import com.ezen.sapoom.request.RegisterRequest;
import com.ezen.sapoom.request.ValidateRequest;

import org.json.JSONException;
import org.json.JSONObject;


public class SignupActivity extends AppCompatActivity {

    private EditText nickname;
    private EditText name;
    private EditText password;
    private Button signup;
    private Button validateButton;

    private String db_id;
    private String db_pw;
    private String db_nickname;
    private boolean validate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_signup);

        nickname = findViewById(R.id.signyupActivity_edittext_nickname);
        name = findViewById(R.id.signyupActivity_edittext_name);
        password = findViewById(R.id.signyupActivity_edittext_password);
        signup = findViewById(R.id.signyupActivity_button_signup);
        validateButton = findViewById(R.id.validateButton);
        // 판정 버튼
        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db_nickname = nickname.getText().toString();
                db_id = name.getText().toString();
                db_pw = password.getText().toString();
                vailDateID();

            }
        });
        // 회원 가입 버튼
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("SignupActivity" , "회원가입 완료");

                db_nickname = nickname.getText().toString();
                db_id = name.getText().toString();
                db_pw = password.getText().toString();
                joinID();
                finish();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 조건이 뭐가 들어 와야 할까? 그냥 이대로 써도 되는건가??
    }

    public void vailDateID () {
        if(validate)
            return;
        if(db_id.equals("") || db_pw.equals("") || db_nickname.equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
            builder.setMessage("ID, PASSWORD, EMAIL은 반드시 입력해 주세요.").setPositiveButton("확인", null).show();
            return;
        }
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if(success) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                        builder.setMessage("사용할 수 있는 아이디 입니다.").setPositiveButton("확인", null).show();
                        name.setEnabled(false);
                        validate = true;
                        name.setBackgroundColor(Color.GRAY);
                        validateButton.setBackgroundColor(Color.GRAY);
                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                        builder.setMessage("사용할 수 없는 아이디 입니다.").setNegativeButton("확인", null).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        ValidateRequest validateRequest = new ValidateRequest(db_id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(SignupActivity.this);
        queue.add(validateRequest);
    }

    public void joinID () {
        if(!validate) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
            builder.setMessage("먼저 중복 체크를 해주세요.").setNegativeButton("확인", null).show();
            return;
        }

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if(success) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                        builder.setMessage("회원가입에 성공하였습니다.").setPositiveButton("확인", null).show();
                        finish();
                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                        builder.setMessage("회원가입에 실패하셨습니다.").setPositiveButton("확인", null).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        RegisterRequest registerRequest = new RegisterRequest( db_nickname, db_id, db_pw, responseListener);
        RequestQueue queue = Volley.newRequestQueue(SignupActivity.this);
        queue.add(registerRequest);
    }
}
