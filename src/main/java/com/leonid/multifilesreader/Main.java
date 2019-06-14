package com.leonid.multifilesreader;

import com.leonid.multifilesreader.readers.CsvReader;

public class Main {
    public static void main(String[] args) {
        CsvReader csvReader = new CsvReader(args);
        csvReader.generateResult();
    }
}
