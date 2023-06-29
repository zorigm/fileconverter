package com.mkit.fileconverter.converter;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.aspose.words.Document;
import com.mkit.fileconverter.util.HtmlCleaner;


public class DocConverter implements Converter {

    @Override
    public String convertToHtml(String uploadedFileLocation, int fileIndex) throws IOException {
        try {
            FileUtils.cleanDirectory(new File(ConverterConstants.CONVERTED_DOC_FOLDER_LOCATION));
            Document doc = new Document(uploadedFileLocation);
            doc.save(ConverterConstants.DOC_CONVERTED_FILE_LOCATION);

            HtmlCleaner.cleanDocHtml(ConverterConstants.DOC_CONVERTED_FILE_LOCATION);
            HtmlCleaner.replaceImgTags(ConverterConstants.DOC_CONVERTED_FILE_LOCATION, "doc");

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "DONE";
    }
    
}
