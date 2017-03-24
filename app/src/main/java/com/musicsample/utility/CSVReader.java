package com.musicsample.utility;

/**
 * Created by himanshu on 24-03-2017.
 */
import android.util.Log;

import com.musicsample.activity.MainActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {

    InputStream inputStream;

    public CSVReader(InputStream inputStream){
        this.inputStream = inputStream;
    }

    public ArrayList<String[]> read(){
        ArrayList resultList = new ArrayList();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                MainActivity.log("CSV content "+csvLine);
                String[] row = csvLine.split(",");
                resultList.add(row);
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }
        finally {
            try {
                inputStream.close();
            }
            catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: "+e);
            }
        }
        return resultList;
    }
}
