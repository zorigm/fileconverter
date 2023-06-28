package com.mkit.fileconverter.service;

import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import com.mkit.fileconverter.converter.Converter;
import com.mkit.fileconverter.converter.ConverterFactory;
import com.mkit.fileconverter.util.FileTypeUtils;


@Service
public class FileConverterService {

    public String convertUsingFactory(String filePath, String fileName) throws IOException
    {
        //validation
        String fileType = FilenameUtils.getExtension(fileName);

        Converter chosenConverter = ConverterFactory.getConverter(fileType)
        .orElseThrow(() -> new IllegalArgumentException("Invalid file type!"));

        return chosenConverter.convertToHtml(filePath + fileName);
    }

    public String convertUploadedFileUsingFactory(String fileName) throws IOException
    {
        //validations
        //get fileType
        //where should the dummy files be kept?

        String fileType = FileTypeUtils.getFileType(fileName);

        Converter chosenConverter = ConverterFactory.getConverter(fileType)
        .orElseThrow(() -> new IllegalArgumentException("Invalid file type!"));

        String uploadedFileLocation = FileTypeUtils.getUploadedFileLocation(fileType); 

        return chosenConverter.convertToHtml(uploadedFileLocation);
    }

}