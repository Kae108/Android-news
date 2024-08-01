package com.app.news;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.app.news.adapter.ViewPagerAdapter;
import com.app.news.pojo.Constant;
import com.app.news.pojo.Title;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    //    private String[] titles = {"娱乐", "军事", "教育", "文化", "将康", "财经", "体育", "汽车", "科技"};
    private List<Title> titles = new ArrayList<>();
    private List<Title> otherTitles = new ArrayList<>();

    private TabLayout tab_layout;
    private ViewPager viewPager;
    private ImageView btnPlus;
    private Intent intent;
    private Title selectedTitle;
    private final int REQUEST_CODE = 1001;
    private ViewPagerAdapter viewPagerAdapter;
    boolean titlesChanged = false;
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private List<TabNewsFragment> fragmentList = new ArrayList<>();
    private FragmentManager manager = getSupportFragmentManager();
    private boolean getNewIntent = false;
    private List<Bundle> bundleList = new ArrayList<>();
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private Lifecycle lifecycle = getLifecycle();

    public List<Bundle> getBundleList() {
        return bundleList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Log.e("info","onCreate执行了。。。。");

        //启动时进行判断该app是否第一次启动
        this.sharedPreferences = this.getSharedPreferences("MySharedPreferences",Context.MODE_PRIVATE);
        gson = new Gson();
        if(sharedPreferences.getString("titles1",null)!=null){
            String json1 = sharedPreferences.getString("titles1",null);
            String json2 = sharedPreferences.getString("titles2",null);
            titles = gson.fromJson(json1, new TypeToken<List<Title>>() {}.getType());
            otherTitles = gson.fromJson(json2, new TypeToken<List<Title>>() {}.getType());
        }else{
            titles = new ArrayList<>(Arrays.asList(Constant.TITLES));
        }

        //初始化控件
        tab_layout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.viewPager);
        btnPlus = findViewById(R.id.btn_plus);

        intent = getIntent();

        //viewPager设置一个adapter
        setupViewPagerAdapter();

        viewPager.setOffscreenPageLimit(5);

        //给ImageView添加点击事件，点击后跳转到标签管理页面
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNewIntent = false;
                for(Title title:titles){
                    System.out.println("startPos"+title.getStartPosition()+"---endPos"+title.getEndPosition());
                    System.out.println("btn-----"+title);
                }
                System.out.println("others:"+otherTitles);
                Intent intent = new Intent(MainActivity.this, SortManagerActivity.class);
                intent.putParcelableArrayListExtra("titles", (ArrayList<? extends Parcelable>) titles);
                intent.putParcelableArrayListExtra("others",(ArrayList<? extends Parcelable>) otherTitles);
                intent.putExtra("selectedTitle",selectedTitle);
                startActivity(intent);
            }
        });


        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition(), false);
                //点击后给选中位置变量赋值
                selectedTitle = titles.get(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tab_layout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.sharedPreferences = this.getSharedPreferences("MySharedPreferences",Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("titles1",new Gson().toJson(titles)).apply();
        sharedPreferences.edit().putString("titles2",new Gson().toJson(otherTitles)).apply();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        Log.e("info","onResume执行。。。");

        intent = getIntent();
        List<Title> titles1 = intent.getParcelableArrayListExtra("titles");
        List<Title> titles2 = intent.getParcelableArrayListExtra("others");
        System.out.println("----titles:"+titles);
        System.out.println("----titles1:"+titles1);
        System.out.println("----titles2:"+titles2);

        int selectPos = 0;
        if(titles2!=null){
            otherTitles.clear();
            otherTitles.addAll(titles2);
        }
        List<TabNewsFragment> list = new ArrayList<>();
        if(titles1!=null&&!titles1.equals(titles)){
            titles.clear();
            titles.addAll(titles1);
            System.out.println("iiiiiiiiiiiiiiiiiothers:"+otherTitles);
            //判断之前选中的标签有没有被删
            if(titles.contains(selectedTitle)){
                selectPos = titles.indexOf(selectedTitle);
            }
            System.out.println("--------------resume-----titles----"+titles);
            viewPagerAdapter.updateTitles();
//            viewPager.setCurrentItem(selectPos);
        }

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e("info","onNewIntent执行。。。");
        getNewIntent = true;
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if(intent!=null){
            this.intent.putParcelableArrayListExtra("titles",intent.getParcelableArrayListExtra("titles"))
                    .putParcelableArrayListExtra("others",intent.getParcelableArrayListExtra("others"));
        }
    }

    private void setupViewPagerAdapter() {
        viewPagerAdapter = new ViewPagerAdapter(fragmentManager,titles);
        viewPager.setAdapter(viewPagerAdapter);
    }

}