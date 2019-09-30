package com.ezen.sapoom.horizelistitem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.ezen.sapoom.MineActivity;
import com.ezen.sapoom.R;
import com.ezen.sapoom.request.AddFollowRequest;
import com.ezen.sapoom.login.LoginActivity;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.lang.System.out;


public class HLVAdapter extends RecyclerView.Adapter<HLVAdapter.ViewHolder> {

    Context context;
    ArrayList<Integer> items;
    Bitmap bitmap;
    public static ArrayList<String> profile_image_horizelist = new ArrayList<String>();
    public static ArrayList<String> addfollow_id_list = new ArrayList<String>();

    String addfollow_id;

    public HLVAdapter(Context context, ArrayList<Integer> items) {
        super();
        this.context = context;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.hot_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        final Thread mThread = new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(profile_image_horizelist.get(i));
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    int height = bitmap.getHeight();
                    int width = bitmap.getWidth();
                    if(width>1000){ /* Bitmap이미지의 크기를 가로사이즈500을 기준으로 비율에 맞게 조정하고 파일로저장! * 조정된 가로, 세로의 크기를 각각 height, width에 저장한다! */
                        bitmap = Bitmap.createScaledBitmap(bitmap, (width=100), (height = height*100/width), true);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 70, out); }
                }catch (Exception e){ e.printStackTrace(); }
            }
        };
        mThread.start();
        try {
            mThread.join();
            viewHolder.imgThumbnail.setImageBitmap(bitmap);
        } catch (Exception e) {e.printStackTrace();}

        viewHolder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, final int position, boolean isLongClick) {
                if (isLongClick) {
                    context.startActivity(new Intent(context, MineActivity.class)); // 원하는 액티비티 (해당 사용자의 정보 창으로 간다.)
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle(null)
                            .setMessage("따름벗을 추가하시겠습니까?")
                            .setCancelable(false)
                            .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // 따름벗 추가 정보를 서버로 보냄.
                                    addfollow_id = addfollow_id_list.get(items.get(position));
                                    follow_add(LoginActivity.db_id, addfollow_id, "  ");
                                }
                            }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog alertDialog = alert.create();
                    alertDialog.show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public CircleImageView imgThumbnail;
        private ItemClickListener clickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = (CircleImageView) itemView.findViewById(R.id.hotuserView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onClick(view, getPosition(), true);
            return true;
        }
    }

    // 친구추가 정보를 서버로 보내기
    public void follow_add(String my_id, String follow_id, String following_id) {
        String follow_index = "00"; // 임의의 값을 넣었음.

        final Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if(success) {
                        Toast.makeText(context, "따름벗이 추가되었습니다.", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(context, "이미 추가되었거나, 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        AddFollowRequest addFollowRequest = new AddFollowRequest(follow_index, my_id, follow_id, following_id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(addFollowRequest);
    }
}