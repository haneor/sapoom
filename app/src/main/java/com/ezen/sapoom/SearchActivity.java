package com.ezen.sapoom;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import android.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ezen.sapoom.listview_item.NewestImage_Item;
import com.ezen.sapoom.listview_item.NewestImage_ItemView;
import com.ezen.sapoom.horizelistitem.HLVAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.ezen.sapoom.MineActivity.IMAGE_URL;

public class SearchActivity extends AppCompatActivity {

    private SearchView searchView;
    // 세로 리스트 뷰
    private ListView newestImageList;
    // 가로 리스트 뷰
    private RecyclerView newestView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private ArrayList<String> imageArray;
    private ArrayList<String> id;
    private HotAdapter hotAdapter;
    private String oddImage;
    private String evenImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_search);

        subCreate();
        searchView = findViewById(R.id.searchView);
        id = new ArrayList<>();

        // 최근 업데이트 사진 리스트 호출
        newestImageList = findViewById(R.id.newestImageList);

        newestView = findViewById(R.id.newestList);
        // 콘텐츠의 변경으로 인해 View의 크기가 변경되지 않는다는 것을 알고 있는 경우 성능 향상
        newestView.setHasFixedSize(true);
        // 선형 레이아웃 관리자를 사용
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        newestView.setLayoutManager(layoutManager);

        // 최신 업데이트한 유저의 리스트를 가로뷰로 정렬 (온크리에이트 시 제대로 호출되지 않음.)
        ArrayList<Integer> items;
        items = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));  // 최신 개정 10개 까지만 보여줌
        adapter = new HLVAdapter(SearchActivity.this, items);
        newestView.setAdapter(adapter);
        imageArray = new ArrayList<>();

        // 임시로 리스트에 아이탬을 넣음 수정할 때는 서버에서 로 id값을
        hotAdapter = new HotAdapter(getApplicationContext());

        onLoadUpDateItem();
        setSearchView();
    }

    private void subCreate() {
        ImageButton homeBtn = findViewById(R.id.homeBtn);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

        ImageButton searchBtn = findViewById(R.id.searchBtn);


        ImageButton upLoadBtn = findViewById(R.id.upLoadBtn);
        upLoadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PutEditActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        ImageButton bookmarkBtn = findViewById(R.id.bookmarkBtn);
        bookmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BookmarkActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

        ImageButton mineBtn = findViewById(R.id.mineBtn);
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
    }

    public class HotAdapter extends BaseAdapter {
        Context context;
        ArrayList<NewestImage_Item> ids = new ArrayList<NewestImage_Item>();

        public HotAdapter(Context context) {
            this.context = context;
        }

        public void addItem (NewestImage_Item item) {
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
            NewestImage_ItemView view = new NewestImage_ItemView(context);
            NewestImage_Item id = ids.get(position);
            view.setNewestImage(id.getUrl1(), id.getUrl2());
            ImageView imageView1 = view.findViewById(R.id.newestImage1);
            ImageView imageView2 = view.findViewById(R.id.newestImage2);
            imageView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog(IMAGE_URL+imageArray.get(2*position));
                }
            });
            imageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog(IMAGE_URL+imageArray.get((2*position)+1));
                }
            });
            return view;
        }
    }

    // 서버에 있는 모든 사진을 가져온다.(최신 순서)
    private void onLoadUpDateItem () {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://eor0601.dothome.co.kr/sel_newestImage.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    oddImage = null;
                    evenImage = null;
                    JSONObject jsonObject_response = new JSONObject(response);
                    JSONArray jsonArray = (JSONArray) jsonObject_response.get("response");
                    for(int i = 0; i<jsonArray.length(); i++) {
                        id.add(jsonArray.getJSONObject(i).getString("db_id"));
                        imageArray.add(jsonArray.getJSONObject(i).getString("image_id"));
                        if(i%2 == 0) {
                            evenImage = IMAGE_URL+imageArray.get(i);
                            continue;
                        }else {
                            oddImage = IMAGE_URL+imageArray.get(i);
                            hotAdapter.addItem(new NewestImage_Item(evenImage, oddImage));
                        }
                        newestImageList.setAdapter(hotAdapter);
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
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    // 리스트 뷰의 이미지를 클릭하면 이미지가 다이얼로그로 팝업됨.
    public void showDialog(String url){
        android.app.AlertDialog.Builder builder;
        android.app.AlertDialog alertDialog;

        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    private void setSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                hotAdapter = new HotAdapter(getApplicationContext());
                for(int i = 0; i<imageArray.size(); i++) {
                    if(s.equals(id.get(i))) {
                        if(i%2 == 0) {
                            evenImage = IMAGE_URL+imageArray.get(i);
                            continue;
                        }else {
                            oddImage = IMAGE_URL+imageArray.get(i);
                            hotAdapter.addItem(new NewestImage_Item(evenImage, oddImage));
                        }
                        newestImageList.setAdapter(hotAdapter);
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }


}
