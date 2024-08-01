package com.app.news;

import com.app.news.pojo.Title;

import java.util.List;

public interface TitleSortListener {
    void titleSortChange(List<Title> titleList);
}
