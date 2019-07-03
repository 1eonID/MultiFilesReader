package com.leonid.multifilesreader.interfaces;

import com.leonid.multifilesreader.beans.CsvBean;

import java.util.Set;

public interface CustomFileWriter {
    void writeToCsvFile(Set<CsvBean> resultSet, String pathToOutputFile);
}
