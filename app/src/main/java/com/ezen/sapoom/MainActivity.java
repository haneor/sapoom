package com.ezen.sapoom;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ezen.sapoom.login.LoginActivity;
import com.ezen.sapoom.login.SplashActivity;
import com.ezen.sapoom.horizelistitem.HLVAdapter;
import com.ezen.sapoom.listview_item.UpDate_Item;
import com.ezen.sapoom.listview_item.UpDate_ItemView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ezen.sapoom.MineActivity.IMAGE_URL;

public class MainActivity extends AppCompatActivity {
    // 로그인된 아이디 값을 저장하여서 각종 서버의 데이터를 받아 올수 있도록 상수를 설정한다.
    public static String STATIC_ID;

    private ListView upDateList;
    private RecyclerView hotUserList;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter hotadapter;

    private List<String> image_db_id_list;
    private List<String> userImage_list;
    private List<String> userProfileImage_list;
    private List<String> substance_list;
    private List<String> comment_list;
    private List<String> good_list;
    private List<String> nickname_list;
    private ArrayList<String> horize_id_list;
    private ArrayList<String> horize_image_list;
    private String[] follow_id;
    private int follow_count; // 팔로우 명수를 받아옴
    private UpDateAdapter upDateAdapter;
    private int getFollow_count; // 현재 페이지에 출력할 팔로우의 정보를 세는 카운터

    private AdView mAdView;
    private ImageButton searchBtn;
    private ImageButton upLoadBtn;
    private ImageButton bookmarkBtn;
    private ImageButton mineBtn;
    private ImageView imageView;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_main);

        ImageButton picture = findViewById(R.id.logoutBtn);
        ImageButton optionBtn = findViewById(R.id.optionBtn);
        ImageView appName = findViewById(R.id.appName);
        searchBtn = findViewById(R.id.searchBtn);
        upLoadBtn = findViewById(R.id.upLoadBtn);
        bookmarkBtn = findViewById(R.id.bookmarkBtn);
        mineBtn = findViewById(R.id.mineBtn);
        mAdView = (AdView) findViewById(R.id.adView);  // 될지 안될지 확인 후 제거(되면 두고)
        imageView = findViewById(R.id.imageView);

        subCreate();
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 로그아웃 관련
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("로그아웃 하시겠습니까?").setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        LoginActivity.db_id = null;
                        SplashActivity.keepLogin = false;
                        Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).show();
            }
        });
        optionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //새로고침 관련
                refresh();
            }
        });
        // 로고 이미지 넣기
        appName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "사품 : 어떤 일이나 동작이 진행되는 '마침 그 때(기회)'를 뜻하는 순우리말 입니다.", Toast.LENGTH_LONG).show();
            }
        });

        upDateList = findViewById(R.id.upDateList);
        hotUserList = findViewById(R.id.newestList);
        // 콘텐츠의 변경으로 인해 View의 크기가 변경되지 않는다는 것을 알고 있는 경우 성능 향상
        hotUserList.setHasFixedSize(true);
        // 선형 레이아웃 관리자를 사용
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        hotUserList.setLayoutManager(layoutManager);

        // 메인 페이지 에서 update아이탬들을 모두 순서대로 받아서 뿌리도록 배열을 만듦.
        userProfileImage_list = new ArrayList<String>();
        image_db_id_list = new ArrayList<String>();
        userImage_list = new ArrayList<String>();
        substance_list = new ArrayList<String>();
        nickname_list = new ArrayList<String>();
        comment_list = new ArrayList<String>();
        good_list = new ArrayList<String>();
        // 가로 리스트 뷰에 정보 넣기 위한 배열
        horize_id_list = new ArrayList<String>();
        horize_image_list = new ArrayList<String>();

        upDateAdapter = new UpDateAdapter(this);

    }

    private void subCreate() {
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
        mineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MineActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        onSelectFollow_my();

        onLoadUpDateItem();
    }

    public class UpDateAdapter extends BaseAdapter {
        Context context;

        ArrayList<UpDate_Item> ids = new ArrayList<UpDate_Item>();

        public UpDateAdapter(Context context) {
            this.context = context;
        }

        public void addItem(UpDate_Item item) {
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
            UpDate_ItemView view = new UpDate_ItemView(context);
            UpDate_Item id = ids.get(position);
            view.setNicknameBtn(id.getNickname());
            view.setUserProfileImage(id.getUserprofile());
            view.setImageView(id.getImageView());
            view.setCommentText(id.getComment());
            view.setGoodText(id.getGood());
            view.setSubstanceText(id.getSubstance());
            // 해당 이미지의 다이얼로그 클릭 액션을 주기 위한 작업
            ImageView imageView = view.findViewById(R.id.imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog(IMAGE_URL + userImage_list.get(position));
                }
            });
            // 광고 넣기
            mAdView = (AdView) view.findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            mAdView.loadAd(adRequest);
            return view;
        }
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
                    follow_id = new String[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject result = (JSONObject) jsonArray.getJSONObject(i);
                        follow_id[i] = result.getString("follow_id");
                        follow_count = jsonArray.length();
                    }
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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("my_id", LoginActivity.db_id); // 내 아이디를 보냄.
                return params;
            }
        };
        postRequestQueue.add(postStringRequest);
    }
    // 사진과 관련된 아이탬들을 최신 순서로 가져와서 뿌려줌
    private void onLoadUpDateItem() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://eor0601.dothome.co.kr/loadupdate.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject_response = new JSONObject(response);
                    JSONArray jsonArray = (JSONArray) jsonObject_response.get("response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject result = (JSONObject) jsonArray.get(i);
                        horize_id_list.add(result.getString("db_id"));
                        horize_image_list.add(result.getString("profile_image"));
                        // horizelist 에 있는 정보를 가져와서 내부에 저장
                        HLVAdapter.profile_image_horizelist.add(IMAGE_URL + horize_image_list.get(i));
                        HLVAdapter.addfollow_id_list.add(horize_id_list.get(i));
                        for (int j = 0; j < follow_count; j++) {
                            if (follow_id[j].equals(result.getString("db_id"))) {
                                image_db_id_list.add(result.getString("db_id"));
                                userImage_list.add(result.getString("image_id"));
                                substance_list.add(result.getString("db_substance"));
                                comment_list.add(result.getString("comment_sub"));
                                good_list.add(result.getString("good"));
                                nickname_list.add(result.getString("db_nickname"));
                                userProfileImage_list.add(result.getString("profile_image"));
                                getFollow_count++;
                            }
                        }
                    }
                    updateListView();
                    horizeListAdd();
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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    // 가로 리스트 뷰의 내용을 넣음
    public void horizeListAdd() {
        // 가로 리스트 뷰 (최신 업데이트) 순서로 아이콘 정렬(제대로 호출되지 않음)
        ArrayList<Integer> items;
        items = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));  // 최신 개정 10개 까지만 보여줌
        hotadapter = new HLVAdapter(MainActivity.this, items);
        hotUserList.setAdapter(hotadapter);
    }
    // 리스트 뷰에 업데이트 된 내용의 정보를 넣는다.
    public void updateListView() {
        // 리스트 뷰에 업데이트 된 목록들이 전부 로딩 된다.
        UpDateAdapter upDateAdapter = new UpDateAdapter(this);
        for (int i = 0; i < getFollow_count; i++) {
            upDateAdapter.addItem(new UpDate_Item(nickname_list.get(i), MineActivity.IMAGE_URL + userImage_list.get(i), substance_list.get(i),
                    "댓글 "+comment_list.get(i)+"개", MineActivity.IMAGE_URL + userProfileImage_list.get(i),
                    "좋아요 " + good_list.get(i) + " 개"));
        }
        upDateList.setAdapter(upDateAdapter);
    }
    // 리스트 뷰의 이미지를 클릭하면 이미지가 다이얼로그로 팝업됨.
    public void showDialog(String url) {
        android.app.AlertDialog.Builder builder;
        android.app.AlertDialog alertDialog;

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_user_image, (ViewGroup) findViewById(R.id.layout_root));
        ImageView image = (ImageView) layout.findViewById(R.id.imageView);
        Picasso.with(this).load(url).into(image);
        builder = new android.app.AlertDialog.Builder(this);
        builder.setView(layout);
        builder.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }
    // 화면 재설정
    public void refresh() {
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(getIntent());
    }
}
