package com.ezen.sapoom.followitem;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

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

public class DoggingManiger extends AppCompatActivity {

    private ListView followList;
    private String [] dogging_follow;
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
        onSelectFollow_dogging();
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
        public View getView(int position, View convertView, ViewGroup parent) {
            FollowManiger_ItemView view = new FollowManiger_ItemView(context);
            FollowManiger_Item id = ids.get(position);
            view.setUser(id.getUser_image());
            view.setNickname(id.getNickname());
            view.setSubstance(id.getSubstance());
            return view;
        }
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
                    dogging_follow = new String[jsonArray.length()];
                    for(int i = 0; i<jsonArray.length(); i++) {
                        dogging_follow[i] = jsonArray.getJSONObject(i).getString("my_id");
                        onSettingDate(dogging_follow[i]);
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
}
