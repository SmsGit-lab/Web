package com.example.hellocarts;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {
    private List<View>mViewList;
    private List<String>mTitleList;
    public ViewPagerAdapter(List<View>mViewList,List<String>mTitleList){
        this.mViewList=mViewList;
        this.mTitleList=mTitleList;
    }

    @Override
    public int getCount() {
        return mViewList.size();        //页卡数
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;        //官方推荐写法
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(mViewList.get(position));
        return mViewList.get(position);     //添加页卡
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(mViewList.get(position));      //删除页卡
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList.get(position);        //页卡标题
    }
}
