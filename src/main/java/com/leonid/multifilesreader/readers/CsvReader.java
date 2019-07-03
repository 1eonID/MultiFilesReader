package com.leonid.multifilesreader.readers;

import com.google.common.io.Files;
import com.leonid.multifilesreader.beans.CsvBean;
import com.leonid.multifilesreader.interfaces.CustomFileReader;
import com.leonid.multifilesreader.interfaces.CustomFileWriter;
import com.leonid.multifilesreader.workers.CsvWriter;
import com.leonid.multifilesreader.writers.CsvWorker;

import java.io.File;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

public class CsvReader implements CustomFileReader {
    private int numOfCores = Runtime.getRuntime().availableProcessors();
    private int threadPoolSize = numOfCores * 10;
    private String[] listWithFiles;
    private Set<CsvBean> resultSet = new TreeSet<>(new Comparator<CsvBean>() {
        @Override
        public int compare(CsvBean o1, CsvBean o2) {
            // new elements will add and sort automatically
            return o1.getPrice().compareTo(o2.getPrice());
        }
    });

    public CsvReader(String[] args) {
        this.listWithFiles = args;
    }

    public Set<CsvBean> getResultSet() {
        return resultSet;
    }

    @Override
    public void generateResult() {
        // Create executor
        ThreadPoolExecutor readExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadPoolSize);

        // Submit each file to its own thread
        for (String pathToFile : listWithFiles) {
            File file = new File(pathToFile);
            if (file.isFile()) {
                if (getExtensionByGuava(pathToFile).equals("csv")) {
                    readExecutor.execute(new CsvWorker(file, resultSet));
                } else {
                    System.out.println("File: " + pathToFile + ", is not 'csv' file!");
                }
            } else {
                System.out.println(pathToFile + " - is not a file!");
            }
        }
        readExecutor.shutdown();
        while (!readExecutor.isTerminated()) {
        }
        CustomFileWriter writer = new CsvWriter();
        //Write resulted Map to output file
        writer.writeToCsvFile(cutAndLimitResultedSet(resultSet), "output.csv");
    }

    private Set<CsvBean> cutAndLimitResultedSet(Set<CsvBean> resultSet) {
        int maxQuantityOfSameProductId = 20;
        int maxMapSize = 1000;
        Map<Integer, Set<CsvBean>> map = new HashMap<>();

        for (CsvBean csvBean: resultSet) {
            if (map.containsKey(csvBean.getProductId())) {
                Set<CsvBean> existedInternalSet = map.get(csvBean.getProductId());
                existedInternalSet.add(csvBean);
                map.put(csvBean.getProductId(), existedInternalSet);
            } else {
                Set<CsvBean> newInternalSet = new TreeSet<>(new Comparator<CsvBean>() {
                    @Override
                    public int compare(CsvBean o1, CsvBean o2) {
                        // new elements will add and sort automatically
                        return o1.getPrice().compareTo(o2.getPrice());
                    }
                });
                newInternalSet.add(csvBean);
                map.put(csvBean.getProductId(), newInternalSet);
            }
            System.out.println("Row: " + csvBean.toString());
        }

        //cut sorted sets(no more than 20 objects with same productId)
        Map<Integer, Set<CsvBean>> cutMap = new HashMap<>();
        map.forEach((key, value) -> cutMap.put(key, value.stream()
                .limit(maxQuantityOfSameProductId)
                .collect(Collectors.toSet())));

        //add all left elements to set and limit to 1000 elements
        Set<CsvBean> result = new TreeSet<>(new Comparator<CsvBean>() {
            @Override
            public int compare(CsvBean o1, CsvBean o2) {
                // new elements will add and sort automatically
                return o1.getPrice().compareTo(o2.getPrice());
            }
        });

        cutMap.forEach((key, value) -> result.addAll(value));

        return result.stream().limit(maxMapSize).collect(Collectors.toSet());
    }

    private String getExtensionByGuava(String filename) {
        return Files.getFileExtension(filename);
    }
}
