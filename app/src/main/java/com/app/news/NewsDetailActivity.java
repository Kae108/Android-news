package com.app.news;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.app.news.pojo.News;


public class NewsDetailActivity extends AppCompatActivity {

    private News.ResultDTO.DataDTO dataDTO;
    private Toolbar toolbar;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_news_detail);
        //初始化视图组件
        toolbar = findViewById(R.id.toolbar);
        webView = findViewById(R.id.webView);
        //取值
        dataDTO = (News.ResultDTO.DataDTO) getIntent().getSerializableExtra("dataDTO");
        //绑定
        if(dataDTO!=null){
            toolbar.setTitle(dataDTO.getTitle());
            webView.loadUrl(dataDTO.getUrl());
        }

        //返回
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}