package com.app.news;

import com.app.news.pojo.News;

public interface OnItemClickListener{
        void onItemClick(News.ResultDTO.DataDTO dataDTO, int position);
    }