package com.app.news.utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.app.news.ItemTouchHelperAdapter;

//recyclerView的滑动拖动处理类
public class TouchHelperCallBack extends ItemTouchHelper.Callback {

    private ItemTouchHelperAdapter touchHelperAdapter;

    public TouchHelperCallBack(ItemTouchHelperAdapter touchHelperAdapter) {
        this.touchHelperAdapter = touchHelperAdapter;

    }

    @Override
    //方向设置
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragDir = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        return makeMovementFlags(dragDir,0);//第一个参数是拖动事件，第二个参数是滑动事件。
    }

    //当元素移动时回调，用于捕获元素移动事件
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        //一旦移动，实现了自定义ItemTouchHelperAdapter接口的RecyclerViewAdapter就去调用实例重写的onItemMove方法，做数据处理。
        touchHelperAdapter.onItemMove(viewHolder.getAdapterPosition(),target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }
}
