package com.ezen.sapoom.bookmarkfrag;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.ezen.sapoom.listview_item.Bookmark_Item;
import com.ezen.sapoom.listview_item.Bookmark_itemView;
import com.ezen.sapoom.commentitem.CommentActivity;
import com.ezen.sapoom.login.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class BookmarkFragment extends Fragment {
    // 내가 남의 이미지에 단 댓글을 모아서 보여줌
    private BookmarkActivity bookmarkActivity;
    private ListView bookmarkList;
    private ViewGroup rootView;
    private BookmarkAdapter bookmarkAdapter;
    public ArrayList<String> image_result;
    public ArrayList<String> comment_result;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        bookmarkActivity = (BookmarkActivity) context;
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
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_bookmark, container, false);
        // 처음 북마크 액티비티에 접근 시 이 페이지가 켜지도록 설정
        bookmarkList = rootView.findViewById(R.id.bookmarkList);

        comment_result = new ArrayList<>();
        image_result = new ArrayList<>();

        onSettingDate();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (bookmarkList == null) {
            Toast.makeText(bookmarkActivity, "좋아하는 사진에 첫 댓글을 달아 보세요!", Toast.LENGTH_LONG).show();
        }
    }

    public class BookmarkAdapter extends BaseAdapter {
        Context context;
        ArrayList<Bookmark_Item> ids = new ArrayList<Bookmark_Item>();

        public BookmarkAdapter(Context context) {
            this.context = context;
        }

        public void addItem(Bookmark_Item item) {
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
            Bookmark_itemView view = new Bookmark_itemView(context);
            Bookmark_Item id = ids.get(position);
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
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {   // 정상 적인 값이 들어감. php 파일 에서 문제가 있는것 같음. 전달되는 두 값이 서버에서 null로 입력됨
                    // 삭제 기능
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle(null)
                            .setMessage("댓글을 정말 삭제 하시겠습니까?")
                            .setCancelable(false)
                            .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    onDelChoiceComment(image_result.get(position), comment_result.get(position));
                                    Runnable runnable = new Runnable() {
                                        @Override
                                        public void run() {
                                            // 댓글 삭제 후 refresh
                                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                                            ft.detach(BookmarkFragment.this).attach(BookmarkFragment.this).commit();
                                        }
                                    };
                                    Handler handler = new Handler();
                                    handler.postDelayed(runnable, 500);
                                }
                            }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog alertDialog = alert.create();
                    alertDialog.show();
                    return false;
                }
            });
            return view;
        }
    }

    // 서버에 올린 데이터를 받아오는 메소드(서버에서 받아온 정보를 하나씩 리스트뷰에 추가함)
    private void onSettingDate() {
        RequestQueue postRequestQueue = Volley.newRequestQueue(getContext());
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, "http://eor0601.dothome.co.kr/mycomment_select.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject_response = new JSONObject(response);
                    JSONArray jsonArray = (JSONArray) jsonObject_response.get("response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject result = (JSONObject) jsonArray.get(i);
                        comment_result.add(result.getString("comment_sub")); // 댓글 내용을 가져온다.
                        image_result.add(result.getString("image_id"));
                    }
                    bookmarkAdapter = new BookmarkAdapter(bookmarkActivity);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        bookmarkAdapter.addItem(new Bookmark_Item(image_result.get(i), LoginActivity.db_id, comment_result.get(i)));
                    }
                    bookmarkList.setAdapter(bookmarkAdapter);
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
                params.put("user_id", LoginActivity.db_id);
                return params;
            }
        };
        postRequestQueue.add(postStringRequest);
    }

    // 해당 댓글을 삭제 하는 기능
    private void onDelChoiceComment(final String image, final String comment) {
        RequestQueue postRequestQueue = Volley.newRequestQueue(getContext());
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, "http://eor0601.dothome.co.kr/comment_del.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("del_user_id", LoginActivity.db_id);
                params.put("del_image_id", image);
                params.put("del_comment_sub", comment);
                return params;
            }
        };
        postRequestQueue.add(postStringRequest);
    }

}
