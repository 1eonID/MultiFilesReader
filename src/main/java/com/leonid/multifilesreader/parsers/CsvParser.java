package com.leonid.multifilesreader.parsers;

import com.leonid.multifilesreader.beans.CsvBean;
import com.leonid.multifilesreader.interfaces.CustomFileParser;
import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class CsvParser implements CustomFileParser {

    @Override
    public Set<CsvBean> parseInputCsvFile(File file) {
        int maxQuantityOfSameProductId = 20;
        int maxMapSize = 1000;
        Map<Integer, Set<CsvBean>> map = new HashMap<>();

        try (CSVReader reader = new CSVReader(new FileReader(file))){
            String[] line;
            reader.readNext();
            while ((line = reader.readNext()) != null) {
                Integer productId = Integer.parseInt(line[0]);
                String name = line[1];
                String condition = line[2];
                String state = line[3];
                Float price = Float.parseFloat(line[4]);

                if (!line[0].isEmpty() && !line[4].isEmpty()) {
                    CsvBean bean = new CsvBean(productId, name, condition, state, price);

                    if (map.containsKey(productId)) {
                        Set<CsvBean> existedInternalSet = map.get(productId);
                        existedInternalSet.add(bean);
                        map.put(productId, existedInternalSet);
                    } else {
                        Set<CsvBean> newInternalSet = new TreeSet<>(new Comparator<CsvBean>() {
                            @Override
                            public int compare(CsvBean o1, CsvBean o2) {
                                // new elements will add and sort automatically
                                return o1.getPrice().compareTo(o2.getPrice());
                            }
                        });
                        newInternalSet.add(bean);
                        map.put(productId, newInternalSet);
                    }
                    System.out.println("Row: " + bean.toString());
                } else {
                    System.out.println("Some fields are empty if line: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //cut sorted sets(no more than 20 objects with same productId)
        Map<Integer, Set<CsvBean>> cutMap = new HashMap<>();
        map.forEach((key, value) -> cutMap.put(key, value.stream()
                                                            .limit(maxQuantityOfSameProductId)
                                                            .collect(Collectors.toSet())));

        //add all left elements to set and limit to 1000 elements
        Set<CsvBean> resultSet = new TreeSet<>(new Comparator<CsvBean>() {
            @Override
            public int compare(CsvBean o1, CsvBean o2) {
                // new elements will add and sort automatically
                return o1.getPrice().compareTo(o2.getPrice());
            }
        });

        cutMap.forEach((key, value) -> resultSet.addAll(value));

        return resultSet.stream().limit(maxMapSize).collect(Collectors.toSet());
    }
}
