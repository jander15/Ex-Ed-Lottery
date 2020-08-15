package net.aspenk12.exed.util;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import net.aspenk12.exed.ui.ErrorAlert;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSV {
    private List<String[]> values = new ArrayList<>();

    public CSV(File file) {
        try {
            FileReader fileReader = new FileReader(file);
            CSVReader csvReader = new CSVReader(fileReader);
            values = csvReader.readAll();
        } catch (IOException | CsvException e){
            ErrorAlert.throwErrorWindow("CSV Parsing Failed", e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public String get(int row, int col){
        return values.get(row)[col];
    }

    public String[] get(int row){
        return values.get(row);
    }

    public int rows(){
        return values.size();
    }

    public int cols(){
        return values.get(0).length;
    }
}
