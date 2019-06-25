package com.leonid.multifilesreader.readers;

import com.google.common.io.Files;
import com.leonid.multifilesreader.beans.CsvBean;
import com.leonid.multifilesreader.interfaces.CustomFileReader;
import com.leonid.multifilesreader.interfaces.CustomFileWriter;
import com.leonid.multifilesreader.workers.CsvWriter;
import com.leonid.multifilesreader.writers.CsvWorker;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class CsvReader implements CustomFileReader {
    private int numOfCores = Runtime.getRuntime().availableProcessors();
    private int threadPoolSize = numOfCores * 10;
    private String[] listWithFiles;
    private ConcurrentMap<CsvBean, Float> resultMap = new ConcurrentHashMap<>();

    public CsvReader(String[] args) {
        this.listWithFiles = args;
    }

    public ConcurrentMap<CsvBean, Float> getResultMap() {
        return resultMap;
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
                    readExecutor.execute(new CsvWorker(file, resultMap));
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
        writer.writeToCsvFile(resultMap, "output.csv");
    }

    private String getExtensionByGuava(String filename) {
        return Files.getFileExtension(filename);
    }
}
