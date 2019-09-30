package com.ezen.sapoom;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ezen.sapoom.followitem.DoggingManiger;
import com.ezen.sapoom.followitem.FollowManiger;
import com.ezen.sapoom.request.VolleyMultipartRequest;
import com.ezen.sapoom.login.LoginActivity;
import com.ezen.sapoom.listview_item.MineImage_Item;
import com.ezen.sapoom.listview_item.MineImage_ItemView;
import com.ezen.sapoom.request.UserDateRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MineActivity extends AppCompatActivity {

    public static String BASE_URL = "http://eor0601.dothome.co.kr/fileupload/up/upload_process.php";
    public static String IMAGE_URL = "http://eor0601.dothome.co.kr/fileupload/up/data/";
    private static int PICK_IMAGE_REQUEST = 1;
    public static int BOOKMARK_PAGE_CHECK = 0;
    private CircleImageView profileImage;
    private ListView mineUserList;
    private TextView nicknameText;
    private TextView profileText;
    private TextView postText;

    private String id;
    private String nickname;
    private String profile_sub;
    private String e_mail;
    private String profile_image;
    public String [] arrayStr;

    private Button postBtn;
    private Button doggingBtn;
    private Button bookmarkerBtn;
    private ImageButton homeBtn;
    private ImageButton searchBtn;
    private ImageButton upLoadBtn;
    private ImageButton bookmarkBtn;
    private ImageButton mineBtn;
    private TextView doggingText;
    private TextView bookmarkText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_mine);
        // 해당 아이디에 따른 프로필 정보 설정
        onSettingDate();
        // 프로필 사진 및 내가 등록한 사진을 다운해서 보여준다.
        onSettingImage();
        // 닉네임 설정 기능
        nicknameText = findViewById(R.id.nicknameText);
        // 자기소개 설정 기능
        profileText = findViewById(R.id.mineactivity_textview_profile);
        postText = findViewById(R.id.mineActivity_textview_post);
        doggingText = findViewById(R.id.mineActivity_textview_dogging);
        bookmarkText = findViewById(R.id.mineActivity_textview_bookmark);

        postBtn = findViewById(R.id.postBtn);
        doggingBtn = findViewById(R.id.doggingBtn);
        bookmarkerBtn = findViewById(R.id.bookmarkerBtn);
        homeBtn = findViewById(R.id.homeBtn);
        searchBtn = findViewById(R.id.searchBtn);
        upLoadBtn = findViewById(R.id.upLoadBtn);
        bookmarkBtn = findViewById(R.id.bookmarkBtn);
        mineBtn = findViewById(R.id.mineBtn);
        mineUserList = findViewById(R.id.mineList);

        onSelectFollow_my();
        onSelectFollow_dogging();

        subCreate();
    }

    private void subCreate() {
        nicknameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MineActivity.this);
                final EditText editText = new EditText(MineActivity.this);
                builder.setView(editText);

                builder.setMessage("닉네임을 변경해주세요.").setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        nicknameText.setText(editText.getText().toString());
                        nickname = nicknameText.getText().toString();
                        // uri로 데이터를 전송
                        onPutDate();
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).show();
            }
        });

        // 자기소개 설정 기능
        profileText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MineActivity.this);
                final EditText editText = new EditText(MineActivity.this);
                builder.setView(editText);
                builder.setMessage("자기소개 내용을 입력해주세요. (2줄까지 입력가능)").setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        profileText.setText(editText.getText().toString());
                        profile_sub = profileText.getText().toString();
                        // uri로 데이터를 전송
                        onPutDate();
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).show();
            }
        });

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 텍스트 셋팅을 게시물의 갯수에서 받아와서 함.
                // putedit로 전환
            }
        });

        doggingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 딸림벗 (나를 팔로우한 벗) 의 목록을 보여주는 액티비티로 전환한다.
                Intent intent = new Intent (getApplicationContext(), DoggingManiger.class);
                startActivity(intent);
            }
        });

        bookmarkerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 따름벗 (내가 팔로우한 벗)의 목록을 보여주는 액티비티로 전환한다.
                Intent intent = new Intent (getApplicationContext(), FollowManiger.class);
                startActivity(intent);
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

        upLoadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PutEditActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        bookmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BookmarkActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

        /*mineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MineActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        downLoadImageURL(IMAGE_URL+profile_image);
    }

    public class MineUserAdapter extends BaseAdapter {
        Context context;
        ArrayList<MineImage_Item> ids = new ArrayList<MineImage_Item>();

        public MineUserAdapter(Context context) {
            this.context = context;
        }

        public void addItem (MineImage_Item item) {
            ids.add(item);
        }

        @Override
        public int getCount() {
            return ids.size();
        }

        @Override
        public Object getItem(int position) {
            return ids.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            MineImage_ItemView view = new MineImage_ItemView(context);
            MineImage_Item id = ids.get(position);
            view.setMineImageButton(id.getID1(), id.getID2(), id.getID3());
            // 해당 이미지의 다이얼로그 클릭 액션을 주기 위한 작업
            ImageView imageView1 = view.findViewById(R.id.imageButton1);
            ImageView imageView2 = view.findViewById(R.id.imageButton2);
            ImageView imageView3 = view.findViewById(R.id.imageButton3);
            imageView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog(arrayStr[(3*position)]);
                } // 롱 클릭 리스너 넣기.
            });
            imageView1.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    messageDialog(arrayStr[(3*position)]);
                    return false;
                }
            });
            imageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog(arrayStr[(3*position)+1]);
                }
            });
            imageView2.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    messageDialog(arrayStr[(3*position)+1]);
                    return false;
                }
            });
            imageView3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog(arrayStr[(3*position)+2]);
                }
            });
            imageView3.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    messageDialog(arrayStr[(3*position)+2]);
                    return false;
                }
            });
            return view;
        }
    }
    //로드버튼 클릭시 실행 (이미지뷰에 넣기)
    public void loadImagefromGallery(View view) {
        //Intent 생성
        Intent intent = new Intent(Intent.ACTION_PICK); //ACTION_PIC과 차이점?
        intent.setType("image/*"); //이미지만 보이게
        //Intent 시작 - 갤러리앱을 열어서 원하는 이미지를 선택할 수 있다.
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    //이미지 선택작업을 후의 결과 처리
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        onResume();
        try {
            //이미지를 하나 골랐을때
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && null != data) {
                //data에서 절대경로로 이미지를 가져옴
                Uri uri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                int nh = (int) (bitmap.getHeight() * (200.0 / bitmap.getWidth()));
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 200, nh, true);

                profileImage = (CircleImageView) findViewById(R.id.profileImageBtn);
                profileImage.setImageBitmap(scaled);
                uploadBitmap(bitmap);

            } else {
                Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "로딩에 오류가 발생하였습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    // 비트맵 타입으로 파일 변환
    private byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    // 비트맵 타입으로 변환한 자료를 uri로 저장
    private void uploadBitmap(final Bitmap bitmap) {

        // 발리를 이용해서 피드백을 받음
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, BASE_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            // 프로필 수정 시 업로드된 사진을 피드백 받아서 설정함.
                            String responsePath = new String(response.data, "UTF-8");
                            // 저장 시 버그로 ""의 포함으로 버그 발생하여 처리함.
                            profile_image = responsePath.substring(1, 37);
                            Log.d("response2", profile_image);
                            onPutDate();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }

            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("upfile", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //볼리 리퀘스트에 정보 저장
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }
    // 서버에 올린 데이터를 받아오는 메소드
    private void onSettingDate () {
        RequestQueue postRequestQueue = Volley.newRequestQueue(this);
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, "http://eor0601.dothome.co.kr/loadprofile.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject_response = new JSONObject(response);
                    JSONArray jsonArray = (JSONArray) jsonObject_response.get("response");
                    for(int i = 0; i<jsonArray.length(); i++) {
                        JSONObject result = (JSONObject) jsonArray.get(i);
                        nickname = result.getString("db_nickname");
                        profile_sub = result.getString("profile_sub");
                        e_mail = result.getString("e_mail");
                        profile_image = result.getString("profile_image");
                    }
                    nicknameText.setText(nickname);
                    profileText.setText(profile_sub);
                    downLoadImageURL(IMAGE_URL+profile_image);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("db_id", LoginActivity.db_id);
                return params;
            }
        };
        postRequestQueue.add(postStringRequest);
    }
    // 서버의 프로필 데이터를 넣는 메소드
    private void onPutDate() {

        id = LoginActivity.db_id;
        e_mail = "asdasd";  // 추가로 이메일을 입력하는 작업 진행

        final Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if(success) {
                        Toast.makeText(MineActivity.this, "개인 정보가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(MineActivity.this, "개인 정보 변경이 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        UserDateRequest userDateRequest = new UserDateRequest(id, nickname, profile_sub, e_mail, profile_image, responseListener);
        RequestQueue queue = Volley.newRequestQueue(MineActivity.this);
        queue.add(userDateRequest);

        onResume();
    }
    // 스트링 배열을 생성한 후 사진의 주소값을 넣음
    private void onSettingImage() {
        RequestQueue postRequestQueue = Volley.newRequestQueue(this);
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, "http://eor0601.dothome.co.kr/imageset.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject_response = new JSONObject(response);
                    JSONArray jsonArray = (JSONArray) jsonObject_response.get("response");
                    arrayStr = new String[jsonArray.length()];
                    for(int i = 0; i<jsonArray.length(); i++) {
                        String result = (String) jsonArray.get(i);
                        arrayStr[i] = IMAGE_URL + result;
                        // 리스트 뷰에 이미지 넣기
                        onListImageSet(jsonArray.length());
                        postText.setText(String.valueOf(jsonArray.length())); // 여기에 서버에서 계산되어 입력 받은 게시물 개수를 표시
                    }
                } catch (JSONException e) { e.printStackTrace(); }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("db_id", LoginActivity.db_id);
                return params;
            }
        };
        postRequestQueue.add(postStringRequest);
    }
    // 원형 이미지를 URL로 받아서 보여줌.
    private void downLoadImageURL (final String imageUrl) {
        final Handler handler = new Handler();
        //Thread t = new Thread(Runnable 객체를 만든다);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {    // 오래 거릴 작업을 구현한다
                try {
                    final CircleImageView iv = (CircleImageView) findViewById(R.id.profileImageBtn);
                    URL url = new URL(imageUrl);
                    InputStream is = url.openStream();
                    final Bitmap bm = BitmapFactory.decodeStream(is);
                    handler.post(new Runnable() {

                        @Override
                        public void run() {  // 화면에 그려줄 작업
                            iv.setImageBitmap(bm);
                        }
                    });
                    iv.setImageBitmap(bm); //비트맵 객체로 보여주기
                } catch (Exception e) { }
            }
        });
        t.start();
    }
    // 내가 올린 이미지를 로딩하기위한 처리
    private void onListImageSet (int arrayLength) {
        MineUserAdapter mineAdapter = new MineUserAdapter(getApplicationContext());
        String [] divItem = new String[3];
        for (int i = 0; i < arrayLength; i++) {
            if(i%3 == 0) {
                divItem[0] = arrayStr[i];
                if(i==arrayLength-1) {
                    divItem[1] = "http://eor0601.dothome.co.kr/fileupload/up/data/rogo1.png";
                    divItem[2] = "http://eor0601.dothome.co.kr/fileupload/up/data/rogo1.png";
                    mineAdapter.addItem(new MineImage_Item(divItem[0], divItem[1], divItem[2]));
                }
            }else if(i%3 == 1) {
                divItem[1] = arrayStr[i];
                if(i==arrayLength-1){
                    divItem[2] = "http://eor0601.dothome.co.kr/fileupload/up/data/rogo1.png";
                    mineAdapter.addItem(new MineImage_Item(divItem[0], divItem[1], divItem[2]));
                }
            }else if(i%3 == 2) {
                divItem[2] = arrayStr[i];
                mineAdapter.addItem(new MineImage_Item(divItem[0], divItem[1], divItem[2]));
            }
        }
        mineUserList.setAdapter(mineAdapter);
    }
    // 리스트 뷰의 이미지를 클릭하면 이미지가 다이얼로그로 팝업됨.
    public void showDialog (String url) {
        android.app.AlertDialog.Builder builder;
        android.app.AlertDialog alertDialog;

        LayoutInflater inflater = (LayoutInflater)MineActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate( R.layout.dialog_user_image, (ViewGroup)findViewById( R.id.layout_root));
        ImageView image = (ImageView)layout.findViewById( R.id.imageView);
        Picasso.with(this).load(url).into(image);
        builder = new android.app.AlertDialog.Builder(this);
        builder.setView(layout);
        builder.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {    }
        });

        alertDialog = builder.create();
        alertDialog.show();
    }
    // 메세지를 출력하는 다이얼로그(선택 삭제)
    public void messageDialog(final String image_url) {
        android.app.AlertDialog.Builder builder;
        android.app.AlertDialog alertDialog;

        LayoutInflater inflater = (LayoutInflater)MineActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate( R.layout.dialog_user_image, (ViewGroup)findViewById( R.id.layout_root));
        builder = new android.app.AlertDialog.Builder(this);
        builder.setView(layout);
        builder.setTitle("정말 삭제 하시겠습니까?");
        builder.setMessage("삭제를 누르면 사진에 대한 모든 데이터가 사라집니다.");
        builder.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {    }
        }).setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 삭제 관련 메소드
                delImage(image_url);
                Toast.makeText(getApplicationContext(), "삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                startActivity(getIntent());
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }
    // 이미지 정보 제거
    private void delImage(final String image_url) {
        final String url = image_url.substring(48, 84);
        RequestQueue delRequestQueue = Volley.newRequestQueue(this);
        StringRequest delStringRequest = new StringRequest(Request.Method.POST, "http://eor0601.dothome.co.kr/delimage.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("del_image_path", url);
                return params;
            }
        };
        delRequestQueue.add(delStringRequest);
    }
    // 내가 팔로우 한 사람 리스트 가져오기
    private void onSelectFollow_my() {
        RequestQueue postRequestQueue = Volley.newRequestQueue(this);
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, "http://eor0601.dothome.co.kr/selectfollow.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject_response = new JSONObject(response);
                    JSONArray jsonArray = (JSONArray) jsonObject_response.get("response");
                    for(int i = 0; i<jsonArray.length(); i++) {
                        bookmarkText.setText(String.valueOf(jsonArray.length()));// 여기에 서버에서 계산되어 입력 받은 따라와 개수를 표시
                    }
                } catch (JSONException e) { e.printStackTrace(); }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("my_id", LoginActivity.db_id); // 내 아이디를 보냄.
                return params;
            }
        };
        postRequestQueue.add(postStringRequest);
    }
    // 나를 팔로우한 사람 리스트 가져오기
    private void onSelectFollow_dogging() {
        RequestQueue postRequestQueue = Volley.newRequestQueue(this);
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, "http://eor0601.dothome.co.kr/selectfollow.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject_response = new JSONObject(response);
                    JSONArray jsonArray = (JSONArray) jsonObject_response.get("response");
                    for(int i = 0; i<jsonArray.length(); i++) {
                        doggingText.setText(String.valueOf(jsonArray.length()));
                    }
                } catch (JSONException e) { e.printStackTrace(); }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("follow_id", LoginActivity.db_id); // 내 아이디를 보냄.
                return params;
            }
        };
        postRequestQueue.add(postStringRequest);
    }
}
