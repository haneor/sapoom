package com.ezen.sapoom.commentitem;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ezen.sapoom.R;
import com.ezen.sapoom.listview_item.CommentItem;
import com.ezen.sapoom.listview_item.CommentItemView;
import com.ezen.sapoom.login.LoginActivity;
import com.ezen.sapoom.request.CommentInsertRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommentActivity extends AppCompatActivity {

    private ListView listView;
    private EditText editText;
    private Button savebutton;
    private ImageView goodBtn;

    private String image_id;
    private String comment_sub;
    private String user_id;
    private String good = "0";
    private ArrayList<String> comment_result;
    private ArrayList<String> userid_result;
    private ArrayList<String> good_result;
    private Boolean goodButton;
    private int result_length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_comment);

        // 이미지 아이디를 update_itemView에서 가져올수 있도록 처리
        if(savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                image_id = null;
            }else {
                image_id = extras.getString("image_id");
            }
        }else {
            image_id = (String) savedInstanceState.getSerializable("image_id");
        }

        listView = findViewById(R.id.comment_listView);
        editText = findViewById(R.id.comment_edit);
        savebutton = findViewById(R.id.comment_Btn);
        goodBtn = findViewById(R.id.goodBtn);

        goodButton = false;
        goodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(goodButton == false) {
                    goodBtn.setImageResource(R.drawable.good_up);
                    goodButton = true;
                    good = "1";
                }else {
                    goodBtn.setImageResource(R.drawable.good_down);
                    goodButton = false;
                    good = "0";
                }
            }
        });

        //image_id = ;  버튼 클릭 시 해당 이미지 아이디를 받아 와야함.
        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comment_sub = editText.getText().toString();
                setEditTextItem();
                Toast.makeText(CommentActivity.this, "댓글이 저장되었습니다.", Toast.LENGTH_SHORT).show();
                onAddCommentGood();
                Intent intent = getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                startActivity(getIntent());
            }
        });

        comment_result = new ArrayList<String>();
        userid_result = new ArrayList<String>();
        good_result = new ArrayList<String>();

        onSettingDate();
    }

    // 입력된 내용을 서버로 저장
    private void setEditTextItem () {
        String index = "0";
        String created = "date('Y-m-d H:i:s')";
        user_id = LoginActivity.db_id;
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        };
        CommentInsertRequest commentInsertRequest = new CommentInsertRequest(index, image_id, comment_sub, user_id, good, created, responseListener);
        RequestQueue queue = Volley.newRequestQueue(CommentActivity.this);
        queue.add(commentInsertRequest);
    }

    class CommentAdapter extends BaseAdapter {

        ArrayList<CommentItem> items = new ArrayList<CommentItem>();

        @Override
        public int getCount() {
            return items.size();  // 변경되는 갯수를 추가 할 수 있음.
        }

        public void addItem (CommentItem item) {
            items.add(item);
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override  // 리스트뷰에 들어갈 화면 구성을 xml로 생성해야 한다(singer_item.xml)
        public View getView(int position, View convertView, ViewGroup parent) {
            CommentItemView view = new CommentItemView(getApplicationContext());
            CommentItem item = items.get(position);
            view.setComment(item.getComment_comment());
            view.setNickname(item.getNickname_comment());
            view.setGood(item.getGood_comment());
            return view;
        }
    }

    // 서버에 올린 해당 이미지의 댓글 데이터를 받아오는 메소드
    private void onSettingDate () {
        RequestQueue postRequestQueue = Volley.newRequestQueue(this);
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, "http://eor0601.dothome.co.kr/comment_select.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject_response = new JSONObject(response);
                    JSONArray jsonArray = (JSONArray) jsonObject_response.get("response");
                    for(int i = 0; i<jsonArray.length(); i++) {
                        result_length = jsonArray.length();  // 댓글 갯수
                        JSONObject result = (JSONObject) jsonArray.get(i);
                        comment_result.add(result.getString("comment_sub"));
                        good_result.add(result.getString("good"));
                        userid_result.add(result.getString("user_id"));
                    }

                    // 임시 리스트 뷰 활성화  // 데이터를 받아와서 리스트 뷰에 뿌리기.
                    CommentAdapter adapter = new CommentAdapter();
                    for(int i = 0; i<result_length; i++) {
                        int image = 0;
                        if(good_result.get(i).equals("0")) {
                            image = R.drawable.good_down;
                        }else if (good_result.get(i).equals("1")) {
                            image = R.drawable.good_up;
                        }
                        adapter.addItem(new CommentItem(userid_result.get(i), comment_result.get(i), image));
                    }
                    listView.setAdapter(adapter);

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

    // 서버에 image정보를 수정한다. 좋아요 댓글수
    private void onAddCommentGood () {
        RequestQueue postRequestQueue = Volley.newRequestQueue(this);
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, "http://eor0601.dothome.co.kr/add_good.php", new Response.Listener<String>() {
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
                params.put("image_id", image_id.substring(48, 84));
                params.put("good", good);
                return params;
            }
        };
        postRequestQueue.add(postStringRequest);
    }
}
