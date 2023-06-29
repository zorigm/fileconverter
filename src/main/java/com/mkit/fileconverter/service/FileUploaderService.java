package com.mkit.fileconverter.service;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import com.mkit.fileconverter.util.FileTypeUtils;

@Service
public class FileUploaderService {
    
    public void uploadFile(String fileName, byte[] bytes, int index) throws IOException
    {
        //validation
        String locationToUploadFile = FileTypeUtils.getUploadedFileLocation(FileTypeUtils.getFileType(fileName), index);
        writeBytesToFile(locationToUploadFile, bytes);
        //TODO: upload file based on  file type, use correct path
        
    }

    private void writeBytesToFile(String fileOutput, byte[] bytes)
        throws IOException {

        FileUtils.writeByteArrayToFile(new File(fileOutput), bytes);
    }
}
