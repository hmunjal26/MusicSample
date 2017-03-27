package com.musicsample.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musicsample.BR;
import com.musicsample.R;
import com.musicsample.activity.MainActivity;
import com.musicsample.model.SongData;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by himanshu on 26-03-2017.
 */

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.BindingHolder> {

    ArrayList<String> songsList;
    public static class BindingHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;

        public BindingHolder(View rowView) {
            super(rowView);
            binding = DataBindingUtil.bind(rowView);
        }

        public ViewDataBinding getBinding() {
            return binding;
        }
    }
    public SongAdapter(ArrayList<String> list){

        songsList = list;
        MainActivity.log("Song adapter list size"+songsList.size());
    }

    @Override
    public SongAdapter.BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MainActivity.log("Song adapter onCreateView");
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_recyler_view_item, parent, false);
        SongAdapter.BindingHolder holder = new SongAdapter.BindingHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(SongAdapter.BindingHolder holder, int position) {
        MainActivity.log("Song adapter onBindView");
        String s = songsList.get(position);
        MainActivity.log("Song adapter name is"+s);
        SongData sd = new SongData(s);
        holder.getBinding().setVariable(BR.songData,sd);
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return songsList.size();
       // return MainActivity.noOfRows;
    }
}
