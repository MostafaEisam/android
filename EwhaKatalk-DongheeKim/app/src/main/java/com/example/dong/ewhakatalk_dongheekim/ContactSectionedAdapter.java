package com.example.dong.ewhakatalk_dongheekim;

/**
 * Created by User on 2015-06-03.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dong.example.pinnedheaderlistview.SectionedBaseAdapter;

import java.util.ArrayList;

public class ContactSectionedAdapter extends SectionedBaseAdapter {
    Context context;
    ArrayList lists;
    boolean isBookMark;
    int bookMarkCount;

    public ContactSectionedAdapter(Context context, ArrayList lists, boolean isBookMark) {
        this.context = context;
        this.lists = lists;
        this.isBookMark = isBookMark;
    }

    public int getBookMarkCount() {
        return bookMarkCount;
    }

    public void setBookMarkCount(int bookMarkCount) {
        this.bookMarkCount = bookMarkCount;
    }

    public boolean isBookMark() {
        return isBookMark;
    }

    public void setBookMark(boolean isBookMark) {
        this.isBookMark = isBookMark;
    }

    @Override
    public Object getItem(int section, int position) {
        // TODO Auto-generated method stub
        return null;  //수정요망
    }

    @Override
    public long getItemId(int section, int position) {
        // TODO Auto-generated method stub
        return 0;  //수정요망
    }

    @Override
    public int getSectionCount() {
        if(isBookMark){
            return 3;  //내프로필, 즐겨찾기, 친구
        }else{
            return 2;  //내프로필, 친구
        }
    }

    @Override
    public int getCountForSection(int section) {  //우선 10개짜리 리스트

        return 20;
    }

    @Override
    public View getItemView(int section, int position, View convertView, ViewGroup parent) {
        RelativeLayout layout = null;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (RelativeLayout) inflator.inflate(R.layout.contact_list_item, null);
        } else {
            layout = (RelativeLayout) convertView;
        }
        ((TextView) layout.findViewById(R.id.textItem)).setText("Section " + section + " Item " + position);
        return layout;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        RelativeLayout layout = null;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (RelativeLayout) inflator.inflate(R.layout.section_item, null);
        } else {
            layout = (RelativeLayout) convertView;
        }
        ((TextView) layout.findViewById(R.id.textItem)).setText("Header for section " + section);
        return layout;
    }

}
