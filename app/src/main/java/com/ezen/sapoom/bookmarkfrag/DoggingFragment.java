package com.ezen.sapoom.bookmarkfrag;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.ezen.sapoom.BookmarkActivity;
import com.ezen.sapoom.R;
import com.ezen.sapoom.commentitem.CommentActivity;
import com.ezen.sapoom.listview_item.Dogging_Item;
import com.ezen.sapoom.listview_item.Dogging_ItemView;
import com.ezen.sapoom.login.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.ezen.sapoom.MineActivity.IMAGE_URL;


public class DoggingFragment extends Fragment {

    private BookmarkActivity bookmarkActivity;
    private ListView doggingList;
    private ViewGroup rootView;
    private DoggingAdapter doggingAdapter;
    public ArrayList<String> image_result;
    public String comment_result;
    public String userid_result;
    private String[] arrayStr;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        bookmarkActivity = (BookmarkActivity)context;
        Log.d("Dogging", "onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        bookmarkActivity = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_dogging, container, false);
        // 처음 북마크 액티비티에 접근 시 이 페이지가 켜지도록 설정
        doggingList = rootView.findViewById(R.id.doggingList);

        image_result = new ArrayList<>();

        onSettingImage();
        doggingAdapter = new DoggingAdapter(bookmarkActivity);

       return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(doggingList == null) {
            Toast.makeText(bookmarkActivity, "아직 첫 댓글이 달리지 않았어요ㅜ.ㅜ", Toast.LENGTH_LONG).show();
        }
    }

    public class DoggingAdapter extends BaseAdapter {
        Context context;
        ArrayList<Dogging_Item> ids = new ArrayList<Dogging_Item>();

        public DoggingAdapter(Context context) {
            this.context = context;
        }

        public void addItem(Dogging_Item item) {
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
            Dogging_ItemView view = new Dogging_ItemView(context);
            Dogging_Item id = ids.get(position);
            view.setNickname(id.getNickname());
            view.setUser(id.getUser());
            view.setSubstance(id.getSubstance());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 해당 이미지의 댓글 화면으로 전환
                    Intent intent = new Intent(context, CommentActivity.class);
                    intent.putExtra("image_id", image_result.get(position));
                    context.startActivity(intent);
                }
            });
            return view;
        }
    }

    // 서버에 올린 데이터를 받아오는 메소드
    private void onSettingImage() {
        RequestQueue postRequestQueue = Volley.newRequestQueue(bookmarkActivity.getApplicationContext());
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
                        onSettingDate(arrayStr[i]);
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
    // 서버에 올린 해당 이미지의 댓글 데이터를 받아오는 메소드
    private void onSettingDate (final String image_id) {
        RequestQueue postRequestQueue = Volley.newRequestQueue(getContext());
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, "http://eor0601.dothome.co.kr/comment_select.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject_response = new JSONObject(response);
                    JSONArray jsonArray = (JSONArray) jsonObject_response.get("response");
                    for(int i = 0; i<jsonArray.length(); i++) {
                        JSONObject result = (JSONObject) jsonArray.get(i);
                        if (!LoginActivity.db_id.equals(result.getString("user_id")) && !result.getString("user_id").equals(null)) {
                            image_result.add(result.getString("image_id"));
                            comment_result = (result.getString("comment_sub"));
                            userid_result = (result.getString("user_id"));
                            doggingAdapter.addItem(new Dogging_Item(image_id, "@"+userid_result, comment_result));
                        }
                    }
                    doggingList.setAdapter(doggingAdapter);
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
                params.put("image_id", image_id);
                return params;
            }
        };
        postRequestQueue.add(postStringRequest);
    }

}
