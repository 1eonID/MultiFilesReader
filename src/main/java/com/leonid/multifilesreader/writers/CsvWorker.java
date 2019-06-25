package com.leonid.multifilesreader.writers;

import com.leonid.multifilesreader.beans.CsvBean;
import com.leonid.multifilesreader.interfaces.CustomFileParser;
import com.leonid.multifilesreader.parsers.CsvParser;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentMap;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.*;

public class CsvWorker implements Runnable {
    private File file;
    private ConcurrentMap<CsvBean, Float> resultMap;

    public CsvWorker(File file, ConcurrentMap<CsvBean, Float> resultMap) {
        this.file = file;
        this.resultMap = resultMap;
    }

    @Override
    public void run() {
        //0. parse file and create Map with rows(objects)
        CustomFileParser fileParser = new CsvParser();
        Map<CsvBean, Float> parsedMap = fileParser.parseInputCsvFile(file);
        //1. sort by price
        Map<CsvBean, Float> sortedMap = parsedMap
                .entrySet()
                .stream()
                .sorted(comparingByValue())
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));
        System.out.println("File: " + file.getName() + ", parsed and sorted");
        //2. if in first 1000 rows we found more then 20 same id's, then delete row with 21st same id
        if (sortedMap.size() > 1000) {
            Map<CsvBean, Float> limitedMap = sortedMap
                    .entrySet()
                    .stream()
                    .limit(1000)
                    .collect(
                            toMap(Map.Entry::getKey, Map.Entry::getValue));
            //3. add current results to resultMap ('putIfAbsent' method is thread-safe)
            limitedMap.forEach((key, value) -> resultMap.putIfAbsent(key, value));
        } else {
            sortedMap.forEach((key, value) -> resultMap.putIfAbsent(key, value));
        }

        System.out.println("File: " + file.getName() + "\nResult map size for now: " + resultMap.size());
        Thread.currentThread().interrupt();
        //4. add 1000 values from current input file to current output file and then compare and sort like in p.2 (sync?)
    }
}
