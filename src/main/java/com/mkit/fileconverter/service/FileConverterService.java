package com.mkit.fileconverter.service;

import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import com.mkit.fileconverter.converter.Converter;
import com.mkit.fileconverter.converter.ConverterFactory;
import com.mkit.fileconverter.manager.FileVersionManager;
import com.mkit.fileconverter.util.FileTypeUtils;


@Service
public class FileConverterService {

    public String convertUsingFactory(String filePath, String fileName) throws IOException
    {
        //validation
        String fileType = FilenameUtils.getExtension(fileName);

        Converter chosenConverter = ConverterFactory.getConverter(fileType)
        .orElseThrow(() -> new IllegalArgumentException("Invalid file type!"));

        int fileIndex = FileVersionManager.getNextAvailableIndex(fileType);

        return chosenConverter.convertToHtml(filePath + fileName, fileIndex);
    }

    public String convertUploadedFileUsingFactory(String fileName, int fileIndex) throws IOException
    {
        //validations
        //get fileType
        //where should the dummy files be kept?

        String fileType = FileTypeUtils.getFileType(fileName);

        Converter chosenConverter = ConverterFactory.getConverter(fileType)
        .orElseThrow(() -> new IllegalArgumentException("Invalid file type!"));
        
        String uploadedFileLocation = FileTypeUtils.getUploadedFileLocation(fileType, fileIndex);

        return chosenConverter.convertToHtml(uploadedFileLocation, fileIndex);
    }

}