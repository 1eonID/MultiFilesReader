package com.leonid.multifilesreader.workers;

import com.leonid.multifilesreader.beans.CsvBean;
import com.leonid.multifilesreader.interfaces.CustomFileWriter;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

public class CsvWriter implements CustomFileWriter {
    private int maxColumnumber = 1000;
    private int maxSameIDnumber = 20;

    @Override
    public void writeToCsvFile(ConcurrentMap<CsvBean, Float> resultMap, String pathToOutputFile) {
        Map<CsvBean, Float> sortedMap = resultMap
                .entrySet()
                .stream()
                .sorted(comparingByValue())
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));

        CSVWriter csvWriter = null;
        try {
            csvWriter = new CSVWriter(new FileWriter(pathToOutputFile));
            String[] entries = {"Product ID", "Name", "Condition", "State", "Price"};
            csvWriter.writeNext(entries);
            int count = 0;
            for (Map.Entry<CsvBean, Float> entry: sortedMap.entrySet()) {
                if (count >= maxColumnumber) break;
                String[] rowEntry = {entry.getKey().getProductId().toString(),
                        entry.getKey().getName(),
                        entry.getKey().getCondition(),
                        entry.getKey().getState(),
                        entry.getKey().getPrice().toString()};
                csvWriter.writeNext(rowEntry);
                count++;
            }
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
