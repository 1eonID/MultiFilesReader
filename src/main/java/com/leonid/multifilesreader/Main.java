package com.leonid.multifilesreader;

import com.leonid.multifilesreader.beans.CsvBean;
import com.leonid.multifilesreader.interfaces.CustomFileReader;
import com.leonid.multifilesreader.readers.CsvReader;

import java.util.concurrent.ConcurrentMap;

public class Main {
    public static void main(String[] args) {
        CustomFileReader csvReader = new CsvReader(args);
        csvReader.generateResult();
        ConcurrentMap<CsvBean, Float> resultMap = ((CsvReader) csvReader).getResultMap();
        System.out.println("Final result map size: " + resultMap.size() + "\n Result map: " + resultMap.toString()); //ToDo write
    }
}
