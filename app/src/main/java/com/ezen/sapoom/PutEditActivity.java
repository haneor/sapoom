package com.ezen.sapoom;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.ezen.sapoom.request.PutEditRequest;
import com.ezen.sapoom.request.VolleyMultipartRequest;
import com.ezen.sapoom.login.LoginActivity;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;



public class PutEditActivity extends AppCompatActivity {

    // 서버로 이미지 저장
    public static String BASE_URL = "http://eor0601.dothome.co.kr/fileupload/up/upload_process.php";
    static final int PICK_IMAGE_REQUEST = 1;

    private ImageView imageBtn;
    private EditText subText;

    private String db_id;
    private String db_substance;
    private String image_id;
    private int comment_code;
    private String comment_sub;
    private int good;
    private String created;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_put_edit);
        created = "date('Y-m-d H:i:s')";
        comment_code = 1;  // 코멘트 기능을 넣은 후 내용을 넣을 수 있도록 설정한다.
        comment_sub = " ";
        good = 0;

        Button cancelBtn = findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 넣지 않을 거임.
        Button cropBtn = findViewById(R.id.puteditActivity_button_crop);

        Button saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 저장 하는 버튼
                db_id = LoginActivity.db_id;
                db_substance = subText.getText().toString();
                Toast.makeText(getApplicationContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
                setEditTextItem();
                // 저장 후 액티비티 refresh 기능
                Intent refresh = new Intent(PutEditActivity.this, MainActivity.class);
                startActivity(refresh);
                PutEditActivity.this.finish();
            }
        });

        TextView titleTextView = findViewById(R.id.titleTextView);

        subText = findViewById(R.id.substanceText);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    //로드버튼 클릭시 실행
    public void loadImagefromGallery(View view) {
        //Intent 생성
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*"); //이미지만 보이게
        //Intent 시작 - 갤러리앱을 열어서 원하는 이미지를 선택할 수 있다.
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //이미지 선택작업을 후의 결과 처리
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            //이미지를 하나 골랐을때
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && null != data) {
                //data에서 절대경로로 이미지를 가져옴
                Uri uri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                //이미지가 한계이상 크면 불러 오지 못하므로 사이즈를 줄여 준다.
                int nh = (int) (bitmap.getHeight() * (800.0 / bitmap.getWidth()));
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 800, nh, true);

                imageBtn = findViewById(R.id.imageBtn);
                imageBtn.setImageBitmap(scaled);

                uploadBitmap(scaled);

            } else {
                Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "로딩에 오류가 있습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    // 비트맵 타입으로 파일 변환
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    // 비트맵 타입으로 변환한 자료를 uri로 저장
    private void uploadBitmap(final Bitmap bitmap) {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, BASE_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            // 프로필 수정 시 업로드된 사진을 피드백 받아서 설정함.
                            String responsePath = new String(response.data, "UTF-8");
                            // 저장 시 버그로 ""의 포함으로 버그 발생하여 처리함.
                            image_id = responsePath.substring(1, 37);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // 만약 업데이트 사진의 주소를 확보 하려면 IMAGE_URL + image_id (String) 으로 받아 오면 됨.
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                db_id = LoginActivity.db_id;
                params.put("db_id", db_id);
                return params;
            }

            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("upfile", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

    // 입력된 내용을 서버로 저장
    private void setEditTextItem () {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if(success) {
                    }else {   }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        PutEditRequest putEditRequest = new PutEditRequest(db_id, db_substance, created, image_id, comment_code, comment_sub, good, responseListener);
        RequestQueue queue = Volley.newRequestQueue(PutEditActivity.this);
        queue.add(putEditRequest);
    }
}
