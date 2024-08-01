package com.app.news.adapter;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.app.news.TabNewsFragment;
import com.app.news.pojo.Title;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewPagerAdapter extends FixedPagerAdapter<Title> {

    private List<Title> titles;

    public ViewPagerAdapter(FragmentManager fragmentManager, List<Title> titles) {
        super(fragmentManager);
        this.titles = titles;
    }

    @Override
    public Title getItemData(int position) {
        return titles.size() > position ? titles.get(position) : null;
    }

    @Override
    public int getDataPosition(Title title) {
        return titles.indexOf(title);
    }

    @Override
    public boolean equals(Title oldD, Title newD) {
        return oldD.equals(newD);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return TabNewsFragment.newInstance(titles.get(position).getPyTitle());
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    public void updateTitles(){
//        this.titles.clear();
//        this.titles.addAll(newTitles);
        notifyDataSetChanged();
//        System.out.println("-----------vpa----new"+newTitles);
        System.out.println("-----------vpa----"+titles);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position).getCnTitle();
    }
}
