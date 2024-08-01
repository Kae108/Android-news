package com.app.news;

import android.view.View;

import com.app.news.pojo.News;
import com.app.news.pojo.Title;

public interface OnGridItemClickListener {
    void onItemClick(Title title, int position);
}
