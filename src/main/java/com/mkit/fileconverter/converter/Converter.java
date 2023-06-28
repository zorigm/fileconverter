package com.mkit.fileconverter.converter;

import java.io.IOException;

public interface Converter {

    String convertToHtml(String uploadedFileLocation) throws IOException;
}
