package com.app.news;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.news.adapter.NewsListAdapter;
import com.app.news.pojo.News;
import com.app.news.utils.FileUtil;
import com.app.news.utils.Internet;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;


public class TabNewsFragment extends Fragment implements OnItemClickListener{

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

            private String url = "http://v.juhe.cn/toutiao/index?type={new_type}&page={page_num}&page_size=30&is_filter=0&key=e589428dc20c78f0ce7181dac1339a62";
//    private String url = "http://v.juhe.cn/toutiao/index?type={new_type}&page={page_num}&page_size=30&is_filter=0&key=d2a3b64afeda802fedbcac542c03d487";
//    private String url = "http://v.juhe.cn/toutiao/index?type={new_type}&page={page_num}&page_size=30&is_filter=0&key=cb53bd41a74ef445a2d1b7fcfebe6fa0";

    //新闻分类标题
    private static final String ARG_PARAM = "title";

    private String title;

    public String getTitle() {
        return title;
    }

    private View rootView;

    private RecyclerView recyclerView;

    private SwipeRefreshLayout swipeRefreshLayout;

    private NewsListAdapter newsListAdapter;

    private LinearLayoutManager layoutManager;

    private int pageNum = 1;

    private List<News.ResultDTO.DataDTO> data;

    //是否正在加载
    private boolean isLoading = false;

    //判断是否有网络
    private boolean isOnline = true;

    private MainActivity mainActivity;


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putSerializable(title, (Serializable) data);
        outState.putSerializable("vcache", (Serializable) data);

    }

    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 100) {
                String result = (String) msg.obj;
//                System.out.println("result:" + result);
                News news = new Gson().fromJson(result, News.class);
                Gson gson = new Gson();
                String newsJson = gson.toJson(news);
//                System.out.println("news---"+news.toString());
                if (news != null && news.getError_code() == 0) {
                    isOnline = true;
                    if (newsListAdapter != null) {
                        data = news.getResult().getData();
                        newsListAdapter.setListData(data);
                        isLoading = false;
                        Toast.makeText(getActivity(), "获取数据成功", Toast.LENGTH_SHORT).show();
                        Log.d("REQUEST-OK","请求完成。");
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                FileUtil.writeNewsJsonToCache(getContext(), title, newsJson);
                            }
                        });
                        thread.start();
                    }
                } else {
                    if(isOnline){
                        String newsJsonCache = FileUtil.readNewsJsonCache(getContext(), title);
                        News newsCache = new Gson().fromJson(newsJsonCache, News.class);
                        if (newsListAdapter != null) {
                            data = newsCache.getResult().getData();
                            newsListAdapter.setListData(data);
                            isLoading = false;
                            isOnline = false;
                        }
                    }
                    Toast.makeText(getActivity(), "获取数据失败，请重试！", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    public TabNewsFragment() {

    }

    public static TabNewsFragment newInstance(String title) {
//        this.title = title;
        TabNewsFragment tabNewsFragment = new TabNewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, title);
        tabNewsFragment.setArguments(args);
        return tabNewsFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //给title初始化值
            title = getArguments().getString(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_tab_news, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        //设置刷新监听器
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                newsListAdapter.resetDatas();
                getNewsData(pageNum);
                // 结束刷新状态
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //初始化适配器
        newsListAdapter = new NewsListAdapter(getActivity());
        //设置Adapter
        recyclerView.setAdapter(newsListAdapter);
        //设置recyclerView点击事件
        newsListAdapter.setOnItemClickListener(this);
        //设置滚动监听
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                System.out.println("-------1:"+newsListAdapter.getItemCount());
                // 检查是否滚动到底部
//                System.out.println("-------2:"+layoutManager.findLastVisibleItemPosition());
                if (!isLoading && layoutManager.findLastVisibleItemPosition() + 1 == newsListAdapter.getItemCount()) {
                    // 加载更多数据
                    loadMoreData();
                }
            }
        });
        //获取数据
        if(savedInstanceState!=null){
            data = (List<News.ResultDTO.DataDTO>) savedInstanceState.getSerializable("vcache");
            System.out.println("！！！！！！！！！！！！！！！！！！准备获取："+title+" 的数据");
//            data = (List<News.ResultDTO.DataDTO>) savedInstanceState.getSerializable(title);
            newsListAdapter.setListData(data);
        }else{
            getNewsData(pageNum);
        }
////        获取数据
//        if(savedInstanceState!=null&&savedInstanceState.getSerializable(title)!=null){
//            System.out.println("！！！！！！！！！！！！！！！！！！准备获取："+title+" 的数据");
//            data = (List<News.ResultDTO.DataDTO>) savedInstanceState.getSerializable(title);
//            newsListAdapter.setListData(data);
//        } else{
//            getNewsData(pageNum);
//        }
    }

    //加载更多数据
    private void loadMoreData() {
            isLoading = true;
            pageNum = pageNum + 1;
            getNewsData(pageNum);
    }

    //向服务端请求，获取新闻数据
    private void getNewsData(int pageNum) {
        System.out.println("--------------newsData:"+title+"pageNum:"+pageNum);

        String newUrl = url.replace("{new_type}", title).replace("{page_num}", String.valueOf(pageNum));
        Internet internet = new Internet(newUrl);
        System.out.println("-----newUrl:" + newUrl);
        FutureTask<String> futureTask = new FutureTask<>(internet);
        new Thread(futureTask).start();
        try {
            String result = futureTask.get();
            Message message = new Message();
            message.what = 100;
            message.obj = result;
            handler.sendMessage(message);

        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onItemClick(News.ResultDTO.DataDTO dataDTO, int position) {
        //跳转到详情页
        Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
        intent.putExtra("dataDTO", dataDTO);
        startActivity(intent);
    }

}