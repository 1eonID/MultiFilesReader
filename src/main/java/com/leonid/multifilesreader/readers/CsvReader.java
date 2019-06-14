package com.leonid.multifilesreader.readers;

import com.leonid.multifilesreader.writers.Worker;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class CsvReader {
    private int numOfCores = Runtime.getRuntime().availableProcessors();
    private int threadPoolSize = numOfCores * 10;
    private String[] listWithFiles;

    public CsvReader(String[] args) {
        this.listWithFiles = args;
    }

    public void generateResult() {
        // Create executor - just 1 executor, not multiple.
        ThreadPoolExecutor readExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadPoolSize);

        // Submit each file to its own thread
        for (String listWithFile : listWithFiles) {
            if (listWithFile.isFile) {
                readExecutor.execute(new Worker(listWithFile));
            }
        }
    }
}
