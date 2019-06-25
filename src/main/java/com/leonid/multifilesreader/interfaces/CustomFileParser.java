package com.leonid.multifilesreader.interfaces;

import com.leonid.multifilesreader.beans.CsvBean;

import java.io.File;
import java.util.Map;

public interface CustomFileParser {

    Map<CsvBean, Float> parseInputCsvFile(File file);
}
