package com.mkit.fileconverter.converter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import com.mkit.fileconverter.util.FileTypeUtils;

public class PdfConverter implements Converter {

    @Override
    public String convertToHtml(String uploadedFileLocation, int fileIndex) throws IOException {
        String convertedFileLocation = FileTypeUtils.getConvertedFileLocation("pdf", fileIndex);
        String convertedPdfIndexedFolderLocation = FileTypeUtils.getIndexedFolderLocation("pdf", fileIndex);
        String convertedFileName = FileTypeUtils.getConvertedFileName("pdf");

        Files.createDirectory(Path.of(convertedPdfIndexedFolderLocation));

        String pythonScript = "/var/www/pdf_to_html.py";
        ProcessBuilder processBuilder = new ProcessBuilder("python3", pythonScript, uploadedFileLocation,
                convertedFileName, convertedPdfIndexedFolderLocation);
        processBuilder.directory(new File(convertedPdfIndexedFolderLocation));

        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        List<String> results = readProcessOutput(process.getInputStream());
        System.out.println(results);

        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "DONE";
    }

    private List<String> readProcessOutput(InputStream inputStream) {
        String result;
        try {
            result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            System.out.println(result);
            return Collections.singletonList(result);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return Collections.emptyList();
    }
}