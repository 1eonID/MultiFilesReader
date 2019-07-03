package com.leonid.multifilesreader;

import com.leonid.multifilesreader.beans.CsvBean;
import com.leonid.multifilesreader.interfaces.CustomFileReader;
import com.leonid.multifilesreader.readers.CsvReader;

import java.util.Set;

public class Main {
    public static void main(String[] args) {
        CustomFileReader csvReader = new CsvReader(args);
        csvReader.generateResult();
        Set<CsvBean> resultSet = ((CsvReader) csvReader).getResultSet();
        System.out.println("Final result map size: " + resultSet.size() + "\n Result map: " + resultSet.toString());
    }
}
