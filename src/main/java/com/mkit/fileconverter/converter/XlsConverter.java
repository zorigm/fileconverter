package com.mkit.fileconverter.converter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;

import com.aspose.cells.Workbook;
import com.mkit.fileconverter.util.FileTypeUtils;
import com.mkit.fileconverter.util.HtmlCleaner;

public class XlsConverter implements Converter {

    @Override
    public String convertToHtml(String uploadedFileLocation, int fileIndex) throws IOException {

        try {
            //FileUtils.deleteDirectory(new File(ConverterConstants.XLS_TEMP_FOLDER_LOCATION));

            String fileType = FileTypeUtils.getFileType(uploadedFileLocation);
            String convertedFileLocation = FileTypeUtils.getConvertedFileLocation(fileType, fileIndex);
            String convertedExcelIndexedFolderLocation = FileTypeUtils.getIndexedFolderLocation(fileType, fileIndex);

            Files.createDirectory(Path.of(convertedExcelIndexedFolderLocation));

            Workbook workbook = new Workbook(uploadedFileLocation);

            workbook.save(convertedFileLocation);

            HtmlCleaner.cleanExcelHtml(fileType, fileIndex);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "DONE";
    }
    
}
