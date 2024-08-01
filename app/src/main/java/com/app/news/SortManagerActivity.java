package com.app.news;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.app.news.adapter.Grv1Adapter;
import com.app.news.adapter.Grv2Adapter;
import com.app.news.pojo.Title;
import com.app.news.utils.TouchHelperCallBack;

import java.util.ArrayList;
import java.util.List;

public class SortManagerActivity extends AppCompatActivity implements OnItemLongClickListener, OnGridItemClickListener {

    //展示标签集合
    List<Title> items1 = new ArrayList<>();
    //待添加标签集合
    List<Title> items2 = new ArrayList<>();
    //总标签集合
    List<Title> sumTitles = new ArrayList<>();
    //修改前待添加标签集合
    List<Title> oldTitles = new ArrayList<>();
    //adapter1中的items
    List<Title> items = new ArrayList<>();

    //展示标签的recyclerView适配器
    private Grv1Adapter adapter1;
    //待添加标签的recyclerView适配器
    private Grv2Adapter adapter2;
    private RecyclerView rv1;
    private RecyclerView rv2;
    private Button finishBtn;
    private boolean editMode;
    private Intent intent;
    //主页面选中的title
    private Title selectedTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sort_manager);
        rv1 = findViewById(R.id.rv_1);
        rv2 = findViewById(R.id.rv_2);
        rv1.setLayoutManager(new GridLayoutManager(this, 4));
        rv2.setLayoutManager(new GridLayoutManager(this, 4));

//        sumTitles.add(new Title("推荐", "top"));
//        sumTitles.add(new Title("国内", "guonei"));
//        sumTitles.add(new Title("国际", "guoji"));
//        sumTitles.add(new Title("娱乐", "yule"));
//        sumTitles.add(new Title("体育", "tiyu"));
//        sumTitles.add(new Title("军事", "junshi"));
//        sumTitles.add(new Title("科技", "keji"));
//        sumTitles.add(new Title("财经", "caijing"));
//        sumTitles.add(new Title("游戏", "youxi"));
//        sumTitles.add(new Title("汽车", "qiche"));
//        sumTitles.add(new Title("健康", "jiankang"));

        intent = getIntent();

        List<Title> titles1 = intent.getParcelableArrayListExtra("titles");
        List<Title> titles2 = intent.getParcelableArrayListExtra("others");


        //设定titles变化前起始状态
        if(titles1.size()!=0){
            for(int i = 0;i<titles1.size();i++){
                System.out.println("sortm titles.i:"+titles1.get(i));
                titles1.get(i).setEndPosition(-1);
                titles1.get(i).setStartPosition(i);
            }
        }

        //others重置状态-1
        if(titles2.size()!=0){
            for(int i = 0;i<titles2.size();i++){
                titles2.get(i).setStartPosition(-1);
                titles2.get(i).setEndPosition(-1);
            }
        }

        System.out.println(titles1.size());
        for(int i = 0;i<titles1.size();i++){
            Title title = titles1.get(i);
            System.out.println(title);
            System.out.println("sortM---startPos"+title.getStartPosition()+"---endPos"+title.getEndPosition());
        }

        if(titles1!=null&&titles2!=null){
            items1 = titles1;
            items2 = titles2;
            //将原items1数据做存储，用来和操作后的数据作比较，判断标签内容是否变化
            oldTitles.addAll(items1);
        }

        defaultShow();
        finishBtn = findViewById(R.id.btn_ok);
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (items1.containsAll(oldTitles) && items1.size() == oldTitles.size()) {
//                    items.addAll(i);
                    if (items1.equals(oldTitles)) {
                        //什么都没变
                        Log.e("items1:", items1.toString());
                        Log.e("items:", items.toString());
                        finish();
                    } else {
                        //顺序变了
//                        items1.clear();
//                        items1.addAll(items);
                        defaultShow();
                        if(items1.size()!=0){
                            for(int i = 0;i<items1.size();i++){
                                items1.get(i).setEndPosition(i);
                            }
                        }

                        Intent intent = new Intent(SortManagerActivity.this, MainActivity.class);
                        intent.putParcelableArrayListExtra("titles", (ArrayList<? extends Parcelable>) items1);
                        intent.putParcelableArrayListExtra("others", (ArrayList<? extends Parcelable>) items2);
                        System.out.println("顺序变了："+items1);
                        startActivity(intent);
                    }
                } else {
                    //元素变了
                    defaultShow();
                    if(items1.size()!=0){
                        for(int i = 0;i<items1.size();i++){
                            items1.get(i).setEndPosition(i);
                        }
                    }
                    Intent intent = new Intent(SortManagerActivity.this, MainActivity.class);
                    intent.putParcelableArrayListExtra("titles", (ArrayList<? extends Parcelable>) items1);
                    intent.putParcelableArrayListExtra("others", (ArrayList<? extends Parcelable>) items2);
                    System.out.println("元素变了："+items1);
                    startActivity(intent);
                }
            }
        });

    }

    //默认展示页面，实例化两个编辑模式为false的适配器
    private void defaultShow() {
        editMode = false;
        adapter1 = new Grv1Adapter(items1, false);
        adapter2 = new Grv2Adapter(items2, false);
        adapter1.setOnItemLongClickListener(this);
        adapter2.setOnItemLongClickListener(this);
        rv1.setAdapter(adapter1);
        rv2.setAdapter(adapter2);
    }

    //长按显示编辑模式界面，通过实例化两个编辑模式为true的适配器实现
    @Override
    public void onLongClick(View view, int position) {
        editMode = true;
        adapter1 = new Grv1Adapter(items1, true);
        adapter2 = new Grv2Adapter(items2, true);
        adapter1.setOnGridItemClickListener(this);
        adapter2.setOnGridItemClickListener(this);
        rv1.setAdapter(adapter1);
        rv2.setAdapter(adapter2);

        ItemTouchHelper.Callback callback = new TouchHelperCallBack(adapter1);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(rv1);
    }

    //上下元素交换
    public void swap(Title title, int position) {
        if (items1.contains(title)) {
            items1.remove(position);
            items2.add(title);
        } else if (items2.contains(title)) {
            items2.remove(position);
            items1.add(title);
        }
    }

    //点击添加删除元素
    @Override
    public void onItemClick(Title title, int position) {
        System.out.println("------onItemClick-items1:"+items1);
        System.out.println("删除位置：" + position + ",1中删除的元素为：" + items1.get(position).getCnTitle());
        System.out.println("-------------items1:"+items1.toString());
        System.out.println("-------------删除了："+title.toString());
        swap(title, position);
        adapter1.notifyDataSetChanged();
        adapter2.notifyDataSetChanged();
    }

}