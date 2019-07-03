package com.leonid.multifilesreader.workers;

import com.leonid.multifilesreader.beans.CsvBean;
import com.leonid.multifilesreader.interfaces.CustomFileWriter;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

public class CsvWriter implements CustomFileWriter {
    private int maxColumnumber = 1000;
    private int maxSameIDnumber = 20;

    @Override
    public void writeToCsvFile(Set<CsvBean> resultSet, String pathToOutputFile) {

        CSVWriter csvWriter = null;
        try {
            csvWriter = new CSVWriter(new FileWriter(pathToOutputFile));
            String[] entries = {"Product ID", "Name", "Condition", "State", "Price"};
            csvWriter.writeNext(entries);
            int count = 0;
            for (CsvBean csvBean : resultSet) {
                if (count >= maxColumnumber) break;
                String[] rowEntry = {csvBean.getProductId().toString(),
                        csvBean.getName(),
                        csvBean.getCondition(),
                        csvBean.getState(),
                        csvBean.getPrice().toString()};
                csvWriter.writeNext(rowEntry);
                count++;
            }
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
