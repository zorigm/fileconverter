package com.mkit.fileconverter.converter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.mkit.fileconverter.util.HtmlCleaner;

public class HwpConverter implements Converter {

    @Override
    public String convertToHtml(String uploadedFileLocation) throws IOException {

        //This needs to call a python script
        //This python script needs to be bundled together

    String hwpFileLocation = ConverterConstants.UPLOADED_HWP_FILE_LOCATION;
    String pythonScript = "C:\\Users\\Zorig\\Desktop\\Work\\hwp2html\\hwp_to_html2.py";
        
    ProcessBuilder processBuilder = new ProcessBuilder("python", pythonScript, hwpFileLocation);
    processBuilder.directory(new File(ConverterConstants.CONVERTED_HWP_FOLDER_LOCATION));
    //TODO: change back working directory
    
    processBuilder.redirectErrorStream(true);

    //TODO: redirect output file

    Process process = processBuilder.start();
    List<String> results = readProcessOutput(process.getInputStream());

    try {
        process.waitFor();

        HtmlCleaner.replaceImgTags(ConverterConstants.HWP_ROOT_CONVERTED_FILE_LOCATION, "hwp");
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
