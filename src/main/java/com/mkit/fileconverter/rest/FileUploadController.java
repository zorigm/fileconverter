package com.mkit.fileconverter.rest;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.mkit.fileconverter.converter.ConverterConstants;
import com.mkit.fileconverter.service.FileCompressionService;
import com.mkit.fileconverter.service.FileConverterService;
import com.mkit.fileconverter.service.FileUploaderService;

import ch.qos.logback.classic.Logger;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/file-convert")
public class FileUploadController {

    @Autowired
    private FileConverterService fileConverterService;

    @Autowired
    private FileUploaderService fileUploaderService;

    @Autowired
    private FileCompressionService fileCompressionService;

    @GetMapping(value = "/compress", produces = "application/zip")
    public void getCompressedFiles(@RequestParam("file") String file, HttpServletResponse response) throws IOException
    {
        //setting headers
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Content-Disposition", "attachment; filename=\"test.zip\"");

        fileCompressionService.getCompressedFile(file, response.getOutputStream());
    }

    
    @PostMapping(value = "/upload")
    public ResponseEntity<String> convertUploadedFile(@RequestPart("file") MultipartFile file) throws IOException
    {
        String originalFileName = file.getOriginalFilename();
        if (null == originalFileName)
        {
		    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		try {
            fileUploaderService.uploadFile(originalFileName, file.getBytes());

            fileConverterService.convertUploadedFileUsingFactory(originalFileName);

            fileCompressionService.compressFile(originalFileName);

            String html = fileCompressionService.retreiveRootHmtl(originalFileName);

            //html = ConverterConstants.TEMP_LOCATION + "   " + ConverterConstants.CONVERTED_DOC_FOLDER_LOCATION;

            HttpHeaders headers = new HttpHeaders();
            headers.add("Access-Control-Allow-Private-Network", "false");
            headers.add("Access-Control-Allow-Origin", "*");

            return new ResponseEntity<String>(html, headers, 200);
		} catch (Exception e) {
            return new ResponseEntity<String>("NONE", null, 500);
			//e.printStackTrace();
		}

        //return new ResponseEntity<String>("WRONG", null, 200);
    }

    @PostMapping
    public ResponseEntity<String> convertFileByPath(@RequestBody FileDetailsRest fileDetailsRest) throws IOException
    {
        //TODO: validation of file details
        //TODO: Exception handling
        //TODO: add loggers
        //TODO: application properties

        String html = fileConverterService.convertUsingFactory(fileDetailsRest.getFilePath(), fileDetailsRest.getFileName());

        return new ResponseEntity<String>(html, null, 200);
    }
}
