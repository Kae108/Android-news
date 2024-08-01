package com.app.news.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.app.news.ItemTouchHelperAdapter;
import com.app.news.OnGridItemClickListener;
import com.app.news.R;
import com.app.news.TitleSortListener;
import com.app.news.pojo.Title;
import com.app.news.OnItemLongClickListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Grv1Adapter extends RecyclerView.Adapter<Grv1Adapter.Grv1ViewHolder> implements ItemTouchHelperAdapter {

    private List<Title> items;
    boolean editMode;
    private OnItemLongClickListener onItemLongClickListener;
    private OnGridItemClickListener onGridItemClickListener;
    private ItemTouchHelper itemTouchHelper;
    private List<Title> originalItems;
    private TitleSortListener titleSortListener;
    private ViewPagerAdapter viewPagerAdapter;

    public void setViewPagerAdapter(ViewPagerAdapter viewPagerAdapter) {
        this.viewPagerAdapter = viewPagerAdapter;
    }

    public void setTitleSortListener(TitleSortListener titleSortListener) {
        this.titleSortListener = titleSortListener;
    }

    public void setOnGridItemClickListener(OnGridItemClickListener onGridItemClickListener) {
        this.onGridItemClickListener = onGridItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public Grv1Adapter(List<Title> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public Grv1Adapter(List<Title> items, boolean editMode) {
        this.items = items;
        this.editMode = editMode;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Grv1ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        originalItems = new ArrayList<>(items);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item,parent,false);
        return new Grv1ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Grv1ViewHolder holder, int position) {
        Title title = items.get(position);
        String item = title.getCnTitle();
        holder.tv_name.setText(item);
        if(editMode){
            holder.iv_button.setImageResource(R.drawable.tv_stroke);
        }
        //为每个item设置长按监听
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(onItemLongClickListener!=null){
                    onItemLongClickListener.onLongClick(view,position);
                    return true;
                }
                return false;
            }
        });
        //为每个item设置短按监听
        if(editMode){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onGridItemClickListener.onItemClick(title,position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onItemMove(int oldPos, int newPos) {
        //交换位置
        Log.d("info","onItemMove");
        Collections.swap(items,oldPos,newPos);
        Log.d("info","交换成功");
        notifyItemMoved(oldPos,newPos);
        notifyDataSetChanged();
        System.out.println("g1Adapter:"+items.toString());
//        titleSortListener.titleSortChange(items);

    }

    @Override
    public void onItemDissmiss(int pos) {

    }

    public List<Title> getItems() {
        return items;
    }

    public boolean isChanged(){
        if(items.equals(originalItems)){
            return false;
        }else{
            return true;
        }
    }


    public static class Grv1ViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_name;
        private ImageView iv_button;

        public Grv1ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            iv_button = itemView.findViewById(R.id.iv_button);
        }
    }


}
