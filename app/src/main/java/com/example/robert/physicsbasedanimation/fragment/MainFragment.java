package com.example.robert.physicsbasedanimation.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.robert.physicsbasedanimation.R;

/**
 * Created by Robert on 2017/7/7.
 */

public class MainFragment extends ListFragment {
    private OnListItemClickListener onListItemClickListener;

    public interface OnListItemClickListener{
        void onListItemClick(int position);
    }

    public MainFragment(){}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onListItemClickListener = (OnListItemClickListener) context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String listData[] = getResources().getStringArray(R.array.list_name);
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, listData);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        onListItemClickListener.onListItemClick(position);
    }
}
