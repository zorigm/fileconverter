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
         Set<PosixFilePermission> permissions = new HashSet<>();
            permissions.add(PosixFilePermission.OWNER_READ);
            permissions.add(PosixFilePermission.OWNER_WRITE);
            permissions.add(PosixFilePermission.OWNER_EXECUTE);
            permissions.add(PosixFilePermission.GROUP_READ);
            permissions.add(PosixFilePermission.GROUP_WRITE);
            permissions.add(PosixFilePermission.GROUP_EXECUTE);
            permissions.add(PosixFilePermission.OTHERS_READ);
            permissions.add(PosixFilePermission.OTHERS_WRITE);
            permissions.add(PosixFilePermission.OTHERS_EXECUTE);

            Files.setPosixFilePermissions(Path.of(convertedPdfIndexedFolderLocation), permissions);

         String pythonScript = "/var/www/pdf_to_html.py";
    
        //This needs to call a python script
        //This python script needs to be bundled together
         
        System.err.println(pythonScript);
        System.err.println(uploadedFileLocation);
        System.err.println(convertedPdfIndexedFolderLocation);
        System.err.println(convertedFileName);
        ProcessBuilder processBuilder = new ProcessBuilder("python3", pythonScript, uploadedFileLocation, convertedFileName, convertedPdfIndexedFolderLocation);
        processBuilder.directory(new File(convertedPdfIndexedFolderLocation));
        //TODO: change back working directory

        processBuilder.redirectErrorStream(true);

        //TODO: redirect output file

        Process process = processBuilder.start();
        List<String> results = readProcessOutput(process.getInputStream());
        System.out.println(results);

        try {
            process.waitFor();
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
            System.out.println(result);
            return Collections.singletonList(result);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return Collections.emptyList();
    }
}