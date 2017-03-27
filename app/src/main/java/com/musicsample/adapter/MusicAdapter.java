package com.musicsample.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musicsample.BR;
import com.musicsample.R;
import com.musicsample.activity.MainActivity;
import com.musicsample.model.SongData;
import com.musicsample.utility.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by himanshu on 25-03-2017.
 */

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.BindingHolder> {


    ArrayList<String> list;
    HashMap<String, ArrayList<String>> adapterMap;
    RecyclerView songsRecycler;
    RecyclerView.Adapter recyclerViewAdapter;
    Context con;

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
    public MusicAdapter(ArrayList<String> list, HashMap<String, ArrayList<String>> map){
        this.list = list;
        adapterMap = map;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyler_view_item, parent, false);
        songsRecycler = (RecyclerView) v.findViewById(R.id.songs_recycler_view);
        con = parent.getContext();
        RecyclerView.LayoutManager recyclerViewLayoutManager = new GridLayoutManager(con,MainActivity.noOfRows, GridLayoutManager.HORIZONTAL, false);
        songsRecycler.setLayoutManager(recyclerViewLayoutManager);
        int spacingInPixels = 10;
        songsRecycler.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        BindingHolder holder = new BindingHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        String s = list.get(position);
        SongData sd = new SongData(s);
        recyclerViewAdapter = new SongAdapter(adapterMap.get(s));

        int noOfCoulums = (int)(Math.ceil(adapterMap.get(s).size()/(double)(MainActivity.noOfRows)));
       MainActivity.log("no of colum"+noOfCoulums +" artist name"+s +"songs list sie");

        songsRecycler.setAdapter(recyclerViewAdapter);
        holder.getBinding().setVariable(BR.songData,sd);
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
