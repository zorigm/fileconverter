package com.mkit.fileconverter.converter;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.aspose.cells.Workbook;
import com.mkit.fileconverter.util.HtmlCleaner;

public class XlsConverter implements Converter {

    @Override
    public String convertToHtml(String uploadedFileLocation) throws IOException {

        try {
            FileUtils.deleteDirectory(new File(ConverterConstants.XLS_TEMP_FOLDER_LOCATION));

            Workbook workbook = new Workbook(uploadedFileLocation);

            workbook.save(ConverterConstants.XLS_CONVERTED_ROOT_HTML_FILE_LOCATION);

            HtmlCleaner.cleanExcelHtml();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "DONE";
    }
    
}
