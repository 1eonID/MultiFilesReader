package com.leonid.multifilesreader.interfaces;

import com.leonid.multifilesreader.beans.CsvBean;

import java.io.File;
import java.util.Set;

public interface CustomFileParser {

    Set<CsvBean> parseInputCsvFile(File file);
}
