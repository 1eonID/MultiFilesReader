package com.leonid.multifilesreader.interfaces;

import com.leonid.multifilesreader.beans.CsvBean;

import java.util.concurrent.ConcurrentMap;

public interface CustomFileWriter {
    void writeToCsvFile(ConcurrentMap<CsvBean, Float> resultMap, String pathToOutputFile);
}
