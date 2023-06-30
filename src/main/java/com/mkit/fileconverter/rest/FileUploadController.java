package com.mkit.fileconverter.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
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
import com.mkit.fileconverter.manager.FileVersionManager;
import com.mkit.fileconverter.service.FileCompressionService;
import com.mkit.fileconverter.service.FileConverterService;
import com.mkit.fileconverter.service.FileUploaderService;
import com.mkit.fileconverter.util.FileTypeUtils;

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
    public ResponseEntity<Object> convertUploadedFile(@RequestPart("file") MultipartFile file) throws IOException
    {
        String originalFileName = file.getOriginalFilename();
        if (null == originalFileName)
        {
		    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		try {
            int fileIndex = FileVersionManager.getNextAvailableIndex(FileTypeUtils.getFileType(originalFileName));

            fileUploaderService.uploadFile(originalFileName, file.getBytes(), fileIndex);

            fileConverterService.convertUploadedFileUsingFactory(originalFileName, fileIndex);

            //fileCompressionService.compressFile(originalFileName);

            String html = fileCompressionService.retrieveRootHtml(originalFileName, fileIndex);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Access-Control-Allow-Private-Network", "false");
            headers.add("Access-Control-Allow-Origin", "*");
            
            String convertedString = StringEscapeUtils.escapeHtml4(html);
            String fileType = originalFileName.substring(originalFileName.lastIndexOf(".")).replace(".","");
            String fileName = originalFileName.substring(0,originalFileName.lastIndexOf(".")).replace(".","");

             Map<String, Object> map = new HashMap<String, Object>();
            map.put("index", fileName);
            map.put("fileType", fileType);
            map.put("html", convertedString);
        
            return new ResponseEntity<Object>(map, headers, 200);
		} catch (Exception e) {
            return new ResponseEntity<Object>("{'error':'NONE'}", null, 500);
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
