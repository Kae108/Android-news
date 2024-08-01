package com.app.news.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.news.OnItemClickListener;
import com.app.news.R;
import com.app.news.pojo.News;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.MyHolder> {

    static class MyHolder extends RecyclerView.ViewHolder{
        ImageView thumbnail_pic_s;
        ImageView thumbnail_pic_s02;
        ImageView thumbnail_pic_s03;
        TextView author_name;
        TextView date;
        TextView title;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail_pic_s = itemView.findViewById(R.id.thumbnail_pic_s);
            thumbnail_pic_s02 = itemView.findViewById(R.id.thumbnail_pic_s02);
            thumbnail_pic_s03 = itemView.findViewById(R.id.thumbnail_pic_s03);
            author_name = itemView.findViewById(R.id.author_name);
            date = itemView.findViewById(R.id.date);
            title = itemView.findViewById(R.id.title);
        }
    }

    private List<News.ResultDTO.DataDTO> dataList = new ArrayList<>();
    private Context mContext;

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private OnItemClickListener onItemClickListener;

    public NewsListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setListData(List<News.ResultDTO.DataDTO> dataList){
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //加载布局文件
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_item,null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        //将数据绑定到ViewHolder
        News.ResultDTO.DataDTO dataDTO = dataList.get(position);
        holder.author_name.setText("来源："+dataDTO.getAuthor_name());
        holder.date.setText(dataDTO.getDate());
        holder.title.setText(dataDTO.getTitle());
        if(!dataDTO.getThumbnail_pic_s().equals("")&&dataDTO.getThumbnail_pic_s()!=null){
            Glide.with(mContext).load(dataDTO.getThumbnail_pic_s()).into(holder.thumbnail_pic_s);
            Glide.with(mContext).load(dataDTO.getThumbnail_pic_s02()).into(holder.thumbnail_pic_s02);
            Glide.with(mContext).load(dataDTO.getThumbnail_pic_s03()).into(holder.thumbnail_pic_s03);
        }else{
            holder.thumbnail_pic_s.setVisibility(View.GONE);
            holder.thumbnail_pic_s02.setVisibility(View.GONE);
            holder.thumbnail_pic_s03.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClick(dataDTO,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void resetDatas(){
        dataList = new ArrayList<>();
    }


}
