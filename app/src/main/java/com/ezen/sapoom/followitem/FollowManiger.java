package com.ezen.sapoom.followitem;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ezen.sapoom.R;
import com.ezen.sapoom.listview_item.FollowManiger_Item;
import com.ezen.sapoom.listview_item.FollowManiger_ItemView;
import com.ezen.sapoom.login.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FollowManiger extends AppCompatActivity {

    private ListView followList;
    private String [] bookmark_follow;
    private String nickname;
    private String profile_sub;
    private String profile_image;
    private FollowAdapter followAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_follow_maniger);

        followList = findViewById(R.id.follow_maniger_listView);

        followAdapter = new FollowAdapter(getApplicationContext());
        onSelectFollow_my();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private class FollowAdapter extends BaseAdapter {
        Context context;
        ArrayList<FollowManiger_Item> ids = new ArrayList<FollowManiger_Item>();

        public FollowAdapter(Context context) {
            this.context = context;
        }

        public void addItem (FollowManiger_Item item) {
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
            FollowManiger_ItemView view = new FollowManiger_ItemView(context);
            FollowManiger_Item id = ids.get(position);
            view.setUser(id.getUser_image());
            view.setNickname(id.getNickname());
            view.setSubstance(id.getSubstance());
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    // my_id 와 follow_id 를 입력 받아서 해당 요소를 제거한다. (서버)
                     messageDialog(bookmark_follow[position]);
                    return false;
                }
            });
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
                    bookmark_follow = new String[jsonArray.length()];
                    for(int i = 0; i<jsonArray.length(); i++) {
                        bookmark_follow[i] = jsonArray.getJSONObject(i).getString("follow_id");
                        onSettingDate(bookmark_follow[i]);
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

    // 서버에 올린 데이터를 받아오는 메소드(서버에서 받아온 정보를 하나씩 리스트뷰에 추가함)
    private void onSettingDate (final String userid) {
        RequestQueue postRequestQueue = Volley.newRequestQueue(this);
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, "http://eor0601.dothome.co.kr/loadprofile.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject_response = new JSONObject(response);
                    JSONArray jsonArray = (JSONArray) jsonObject_response.get("response");
                    for(int i = 0; i<jsonArray.length(); i++) {
                        JSONObject result = (JSONObject) jsonArray.get(i);
                        nickname = (result.getString("db_nickname"));
                        profile_sub = (result.getString("profile_sub"));
                        profile_image = (result.getString("profile_image"));
                        // 리스트뷰 하나를 받아서 리스트뷰에 각각 추가하는 방법을 사용함.
                        followAdapter.addItem(new FollowManiger_Item("@"+userid+" ( "+nickname+" )", profile_sub, profile_image));
                    }
                    followList.setAdapter(followAdapter);
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
                params.put("db_id", userid);
                return params;
            }
        };
        postRequestQueue.add(postStringRequest);
    }

    public void messageDialog(final String follow_id) {
        android.app.AlertDialog.Builder builder;
        android.app.AlertDialog alertDialog;

        LayoutInflater inflater = (LayoutInflater)FollowManiger.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate( R.layout.dialog_user_image, (ViewGroup)findViewById( R.id.layout_root));
        builder = new android.app.AlertDialog.Builder(this);
        builder.setView(layout);
        builder.setTitle("정말 삭제 하시겠습니까?");
        builder.setMessage(" 해당 따름벗을 삭제 하시겠습니까?");
        builder.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {    }
        }).setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 삭제 관련 메소드
                delfollow(follow_id);
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
    private void delfollow(final String follow_id) {
        RequestQueue delRequestQueue = Volley.newRequestQueue(this);
        StringRequest delStringRequest = new StringRequest(Request.Method.POST, "http://eor0601.dothome.co.kr/delfollow.php", new Response.Listener<String>() {
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
                params.put("my_id", LoginActivity.db_id);
                params.put("follow_id", follow_id);
                return params;
            }
        };
        delRequestQueue.add(delStringRequest);
    }
}
