package com.leonid.multifilesreader.writers;

import com.leonid.multifilesreader.beans.CsvBean;
import com.leonid.multifilesreader.interfaces.CustomFileParser;
import com.leonid.multifilesreader.parsers.CsvParser;

import java.io.*;
import java.util.*;

public class CsvWorker implements Runnable {
    private File file;
    private Set<CsvBean> resultSet;

    public CsvWorker(File file, Set<CsvBean> resultSet) {
        this.file = file;
        this.resultSet = resultSet;
    }

    @Override
    public void run() {
        try {
            CustomFileParser fileParser = new CsvParser();
            Set<CsvBean> parsedAndSortedSet = fileParser.parseInputCsvFile(file);
            System.out.println("File: " + file.getName() + ", parsed, sorted and limited to 1000 elements");

            resultSet.addAll(parsedAndSortedSet);
            System.out.println("File: " + file.getName() + "\nResult map size for now: " + resultSet.size());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Thread.currentThread().interrupt();
        }
    }
}
