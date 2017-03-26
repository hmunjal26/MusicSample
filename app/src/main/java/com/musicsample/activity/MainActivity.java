package com.musicsample.activity;

import android.content.SharedPreferences;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.musicsample.R;
import com.musicsample.adapter.MusicAdapter;
import com.musicsample.model.Song;
import com.musicsample.utility.CSVReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    SharedPreferences prefs = null;
     HashMap<String,ArrayList<String>> artistSongMap;
    HashMap<String,ArrayList<String>> albumSongMap;
    ArrayList<Song> songDetailList;
    ArrayList<String> artistList;
    ArrayList<String> albumList;
    static boolean debug = true;
    public static int noOfRows = 1;
    // mainSpinnerSelection = 0 artist, 1 album
    static  int mainSpinnerSelection = 0;
    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerViewAdapter;
    RecyclerView.LayoutManager recyclerViewLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.custom_action_bar, null);
        final Spinner mainSpinner = (Spinner) mCustomView.findViewById(R.id.main_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_list_item_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mainSpinner.setAdapter(adapter);
        mainSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(position == 0)
                {
                    mainSpinnerSelection = 0;
                    recyclerViewAdapter = new MusicAdapter(artistList,artistSongMap);
                    recyclerView.setAdapter(recyclerViewAdapter);
                }
                else if(position == 1){
                    mainSpinnerSelection = 1;
                    recyclerViewAdapter = new MusicAdapter(albumList,albumSongMap);
                    recyclerView.setAdapter(recyclerViewAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                MainActivity.log("Nothing selected");
            }

        });
        Spinner countSpinner = (Spinner) mCustomView.findViewById(R.id.count_spinner);
        countSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                switch (position){
                    case 0:
                        noOfRows = 1;
                        break;
                    case 1:
                        noOfRows = 2;
                        break;
                    case 2:
                        noOfRows = 3;
                        break;
                    case 3:
                        noOfRows = 4;
                        break;
                    case 4:
                        noOfRows = 5;
                        break;
                    default:
                        noOfRows = 1;
                                break;

                }
                if(mainSpinnerSelection == 0){
                    recyclerViewAdapter = new MusicAdapter(artistList,artistSongMap);
                    recyclerView.setAdapter(recyclerViewAdapter);
                }
                else{
                    recyclerViewAdapter = new MusicAdapter(albumList,albumSongMap);
                    recyclerView.setAdapter(recyclerViewAdapter);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                MainActivity.log("Nothing selected");
            }

        });

        ArrayAdapter<CharSequence> counAdapter = ArrayAdapter.createFromResource(this,
                R.array.count_spinner_list_item_array, android.R.layout.simple_spinner_item);
        counAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        countSpinner.setAdapter(counAdapter);

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        prefs =  getSharedPreferences("com.musicSample", MODE_PRIVATE);
        artistSongMap = new HashMap<>();
        albumSongMap = new HashMap<>();
        songDetailList = new ArrayList<Song>();
        artistList = new ArrayList<String>();
        albumList = new ArrayList<String>();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerViewLayoutManager = new LinearLayoutManager(this);
        recyclerViewLayoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (prefs.getBoolean("firstrun", true)) {
            MainActivity.log("in first run");
            prefs.edit().putBoolean("firstrun", false).commit();
            try {
                CSVReader reader = new CSVReader(getAssets().open("sample_music_data.csv"));
               ArrayList<String[]> content =  reader.read();
                MainActivity.log("count is"+content.size());
                parseCsvData(content);
                    MainActivity.log("artist map"+artistSongMap.toString());
                MainActivity.log("album map"+albumSongMap.toString());
                MainActivity.log("artist songs"+artistSongMap.get("Amit Trivedi"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        recyclerViewAdapter = new MusicAdapter(artistList,artistSongMap);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    void parseCsvData(ArrayList<String[]> content){

        if(content!= null && content.size()>0){

            for(int i=1;i<content.size();i++){
                String[] tempData = content.get(i);
                MainActivity.log("parse data"+tempData);
                String sname = tempData[0];
                String sartist = tempData[1];
                String salbum = tempData[2];

                Song s = new Song(sname,sartist,salbum);
                songDetailList.add(s);
                if(artistSongMap.containsKey(sartist)){
                    artistSongMap.get(sartist).add(sname);
                }else{
                    ArrayList<String> songs = new ArrayList<>();
                    songs.add(sname);
                    artistSongMap.put(sartist,songs);
                    artistList.add(sartist);
                }
                if(albumSongMap.containsKey(salbum)){
                    albumSongMap.get(salbum).add(sname);
                }else{
                    ArrayList<String> songs = new ArrayList<>();
                    songs.add(sname);
                    albumSongMap.put(salbum,songs);
                    albumList.add(salbum);
                }

            }
        }
    }



    public static void log(String message){

        if(MainActivity.debug){
              Log.i("MS:",""+message);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        MainActivity.log("in Stop");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainActivity.log("in destroy");
        prefs.edit().putBoolean("firstrun", true).commit();
    }
}
