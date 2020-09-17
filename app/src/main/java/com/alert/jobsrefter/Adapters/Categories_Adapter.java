package com.alert.jobsrefter.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alert.jobsrefter.R;

import java.util.List;

public class Categories_Adapter extends BaseAdapter {
    Context context;
    List<String> list;

    public Categories_Adapter(Activity activity, List<String> list) {
        this.context=activity;
        this.list=list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).
                    inflate(R.layout.subcat_itemview, viewGroup, false);
        }



        TextView txt_title = (TextView)
                view.findViewById(R.id.txt_title);

        txt_title.setText(list.get(position));


        // returns the view for the current row
        return view;
    }
}
