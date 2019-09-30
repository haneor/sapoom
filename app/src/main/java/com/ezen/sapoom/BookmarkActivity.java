package com.ezen.sapoom;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.ezen.sapoom.bookmarkfrag.BookmarkFragment;
import com.ezen.sapoom.bookmarkfrag.DoggingFragment;

public class BookmarkActivity extends AppCompatActivity {

    private DoggingFragment fragment_do;
    private BookmarkFragment fragment_book;

    private String follow_comment;
    private String dogging_comment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_bookmark);

        subCreate();

        // 프래그먼트를 사용해서 화면을 전환 할 수 있게 한다. 팔로잉 / 내가 단 댓글에 대한 정보
        //doggingFragment, bookmarkFragment를 만들고 붙여 넣기
        fragment_do = new DoggingFragment();
        fragment_book = new BookmarkFragment();

        // mineActivity에서 화면이 전환 되었을 때 화면 설정을 한다.
        if(MineActivity.BOOKMARK_PAGE_CHECK == 1){
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment_book).commit();
            MineActivity.BOOKMARK_PAGE_CHECK = 0;
        }else {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment_do).commit();
        }

        follow_comment = "남긴 댓글";
        dogging_comment = "작성한 댓글";

        final TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText(follow_comment));
        tabs.addTab(tabs.newTab().setText(dogging_comment));

        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getText().equals(follow_comment)) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment_do).commit();
                }else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment_book).commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

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
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

        ImageButton upLoadBtn = findViewById(R.id.upLoadBtn);
        upLoadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PutEditActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
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
}
