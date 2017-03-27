package com.musicsample.model;

import java.util.ArrayList;

/**
 * Created by himanshu on 25-03-2017.
 */

public class SongData {
    String artistOrAlbumOrSongName;


    public SongData(String info){


        artistOrAlbumOrSongName = info;

    }

    public String getArtistOrAlbumOrSongName(){

        return artistOrAlbumOrSongName;
    }

}
