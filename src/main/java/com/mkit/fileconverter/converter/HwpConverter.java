package com.mkit.fileconverter.converter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.mkit.fileconverter.util.FileTypeUtils;
import com.mkit.fileconverter.util.HtmlCleaner;

public class HwpConverter implements Converter {

    @Override
    public String convertToHtml(String uploadedFileLocation, int fileIndex) throws IOException {

    String fileType = FileTypeUtils.getFileType(uploadedFileLocation);
    String convertedFileLocation = FileTypeUtils.getConvertedFileLocation(fileType, fileIndex);
    String convertedHwpIndexedFolderLocation = FileTypeUtils.getIndexedFolderLocation(fileType, fileIndex);
    
    String pythonScript = "/var/www/hwp_to_html2.py";
    //pythonScript = "D:\\SDK\\hwp_to_html2.py";
    
    //This needs to call a python script
    //This python script needs to be bundled together
    ProcessBuilder processBuilder = new ProcessBuilder("python3", pythonScript, uploadedFileLocation);
    processBuilder.directory(new File(ConverterConstants.CONVERTED_HWP_FOLDER_LOCATION));
    //TODO: change back working directory
    
    processBuilder.redirectErrorStream(true);

    //TODO: redirect output file

    Process process = processBuilder.start();
    List<String> results = readProcessOutput(process.getInputStream());

    try {
        process.waitFor();

        HtmlCleaner.replaceImgTags(convertedFileLocation, "hwp", fileIndex);
    } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    return "DONE";
    }

    private List<String> readProcessOutput(InputStream inputStream) {
        String result;
        try {
            result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            return Collections.singletonList(result);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return Collections.emptyList();
    }
}
