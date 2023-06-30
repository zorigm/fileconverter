package com.mkit.fileconverter.converter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.aspose.words.Document;
import com.mkit.fileconverter.util.FileTypeUtils;
import com.mkit.fileconverter.util.HtmlCleaner;


public class DocConverter implements Converter {

    @Override
    public String convertToHtml(String uploadedFileLocation, int fileIndex) throws IOException {
        try {
            String fileType = FileTypeUtils.getFileType(uploadedFileLocation);
            String convertedFileLocation = FileTypeUtils.getConvertedFileLocation(fileType, fileIndex);
            String convertedDocIndexedFolderLocation = FileTypeUtils.getIndexedFolderLocation(fileType, fileIndex);

            Files.createDirectory(Path.of(convertedDocIndexedFolderLocation));

            Document doc = new Document(uploadedFileLocation);
            doc.save(convertedFileLocation);

            HtmlCleaner.cleanDocHtml(convertedFileLocation);
            HtmlCleaner.replaceImgTags(convertedFileLocation, "doc", fileIndex);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "DONE";
    }
    
}
