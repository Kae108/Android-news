package com.app.news;

public interface ItemTouchHelperAdapter {
    //数据交换
    void onItemMove(int oldPos,int newPos);
    //数据删除
    void onItemDissmiss(int pos);
}
