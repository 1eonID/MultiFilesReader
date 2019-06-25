package com.leonid.multifilesreader.parsers;

import com.leonid.multifilesreader.beans.CsvBean;
import com.leonid.multifilesreader.interfaces.CustomFileParser;
import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

public class CsvParser implements CustomFileParser {

    @Override
    public Map<CsvBean, Float> parseInputCsvFile(File file) {
        int maxQuantityOfSameProductId = 20;
        Map<CsvBean, Float> map = new HashMap<>();
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(file));
            String[] line;
            Map<Integer, Map<Integer, Float>> idsCounter = new HashMap<>();
            reader.readNext();
            while ((line = reader.readNext()) != null) {
                Integer productId = Integer.parseInt(line[0]);
                String name = line[1];
                String condition = line[2];
                String state = line[3];
                Float price = Float.parseFloat(line[4]);

                if (idsCounter.containsKey(productId)) {
                    Map<Integer, Float> internalMap = idsCounter.get(productId);
                    // if we found more than 20 same productId, then we sort internal map and put new item, if it cheapest
                    if (internalMap.size() >= maxQuantityOfSameProductId) {
                        Map<Integer, Float> sortedInternalMap = internalMap
                                .entrySet()
                                .stream()
                                .sorted(comparingByValue())
                                .collect(
                                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                                LinkedHashMap::new));
                        if (sortedInternalMap.get(maxQuantityOfSameProductId) > price) {
                            sortedInternalMap.put(maxQuantityOfSameProductId, price);
                            idsCounter.put(productId, sortedInternalMap);
                        } else {
                            continue;
                        }
                    } else {
                        internalMap.put(internalMap.size() + 1, price);
                        idsCounter.put(productId, internalMap);
                    }
                } else {
                    Map<Integer, Float> internalMap = new HashMap<>();
                    internalMap.put(1, price);
                    idsCounter.put(productId, internalMap);
                }

                CsvBean bean = new CsvBean(productId, name, condition, state, price);
                map.put(bean, price);
                System.out.println("Row: " + bean.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }
}
