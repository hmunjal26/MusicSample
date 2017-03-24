package com.musicsample.activity;

import android.content.SharedPreferences;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.musicsample.R;
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

    static boolean debug = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.custom_action_bar, null);
        Spinner mainSpinner = (Spinner) mCustomView.findViewById(R.id.main_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_list_item_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mainSpinner.setAdapter(adapter);

        Spinner countSpinner = (Spinner) mCustomView.findViewById(R.id.count_spinner);
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

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//
//        MenuItem item = menu.findItem(R.id.spinner);
//        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);
//
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.spinner_list_item_array, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        spinner.setAdapter(adapter);
//
//        getMenuInflater().inflate(R.menu.count_menu, menu);
//
//        MenuItem countItem = menu.findItem(R.id.count_spinner);
//        Spinner countSpinner = (Spinner) MenuItemCompat.getActionView(item);
//
//        ArrayAdapter<CharSequence> counAdapter = ArrayAdapter.createFromResource(this,
//                R.array.count_spinner_list_item_array, android.R.layout.simple_spinner_item);
//        counAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        spinner.setAdapter(counAdapter);
//
//
//        return true;
//    }
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
                }
                if(albumSongMap.containsKey(salbum)){
                    albumSongMap.get(salbum).add(sname);
                }else{
                    ArrayList<String> songs = new ArrayList<>();
                    songs.add(sname);
                    albumSongMap.put(salbum,songs);
                }

            }
        }
    }

    /**
     * function create a mapping for songs with artist and albums
     */
    void createArtistAndSongMaps(){

        for (Song song: songDetailList){


        }

    }

    public static void log(String message){

        if(MainActivity.debug){
              Log.i("MS:",""+message);
        }
    }

}
