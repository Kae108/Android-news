package com.app.news.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.news.OnGridItemClickListener;
import com.app.news.R;
import com.app.news.pojo.Title;
import com.app.news.OnItemLongClickListener;

import java.util.List;

public class Grv2Adapter extends RecyclerView.Adapter<Grv2Adapter.Grv2ViewHolder> {

    private List<Title> items;
    boolean editMode;
    private OnItemLongClickListener onItemLongClickListener;
    private OnGridItemClickListener onGridItemClickListener;

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public Grv2Adapter(List<Title> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public Grv2Adapter(List<Title> items, boolean editMode) {
        this.items = items;
        this.editMode = editMode;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Grv2ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item,parent,false);
        return new Grv2ViewHolder(view);
    }

    public void setOnGridItemClickListener(OnGridItemClickListener onGridItemClickListener) {
        this.onGridItemClickListener = onGridItemClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull Grv2ViewHolder holder, int position) {
        Title title = items.get(position);
        String item = title.getCnTitle();
        holder.tv_name.setText(item);
        if(editMode){
            holder.iv_button.setImageResource(R.drawable.tv_plus);
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

    public static class Grv2ViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_name;
        private ImageView iv_button;

        public Grv2ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            iv_button = itemView.findViewById(R.id.iv_button);
        }
    }
}
