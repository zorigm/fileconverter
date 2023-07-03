package com.mkit.fileconverter.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

import com.mkit.fileconverter.manager.FileVersionManager;
import com.mkit.fileconverter.service.FileCompressionService;
import com.mkit.fileconverter.service.FileConverterService;
import com.mkit.fileconverter.service.FileUploaderService;
import com.mkit.fileconverter.util.FileTypeUtils;

import jakarta.servlet.http.HttpServletResponse;
import java.io.File;

@Controller
@RequestMapping(value = "/file-convert")
public class FileUploadController {

    @Autowired
    private FileConverterService fileConverterService;

    @Autowired
    private FileUploaderService fileUploaderService;

    @Autowired
    private FileCompressionService fileCompressionService;

    @GetMapping(value = "/zip")
    public void testZipFile(HttpServletResponse response) throws Exception {
        String originalFileName = "placeholder-excel.xlsx";
        if (null == originalFileName) {
            throw new Exception();
        }

        fileCompressionService.compressFile(originalFileName, 30);

        // setting headers
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Content-Disposition", "attachment; filename=\"test.zip\"");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Private-Network", "false");

        fileCompressionService.getCompressedFile(originalFileName, 30, response.getOutputStream());
    }

    @PostMapping(value = "/upload/compressed", produces = "application/zip")
    public ResponseEntity<FileSystemResource> convertAndReturnCompressedFiles(@RequestPart("file") MultipartFile file,
            HttpServletResponse response)
            throws Exception {
        String originalFileName = file.getOriginalFilename();
        if (null == originalFileName) {
            throw new Exception();
        }

        int fileIndex = FileVersionManager.getNextAvailableIndex(FileTypeUtils.getFileType(originalFileName));

        fileUploaderService.uploadFile(originalFileName, file.getBytes(), fileIndex);

        fileConverterService.convertUploadedFileUsingFactory(originalFileName, fileIndex);

        String zipFileUrl = fileCompressionService.compressFile(originalFileName, fileIndex);

        // setting headers
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "test.zip");
        headers.add("Access-Control-Allow-Private-Network", "false");
        headers.add("Access-Control-Allow-Origin", "*");

        File zipfile = new File(zipFileUrl);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new FileSystemResource(zipfile));
        // FileVersionManager.releaseIndex(FileTypeUtils.getFileType(originalFileName),
        // fileIndex);
    }

    @PostMapping(value = "/upload")
    public ResponseEntity<Object> convertUploadedFile(@RequestPart("file") MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();
        if (null == originalFileName) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            int fileIndex = FileVersionManager.getNextAvailableIndex(FileTypeUtils.getFileType(originalFileName));

            fileUploaderService.uploadFile(originalFileName, file.getBytes(), fileIndex);

            fileConverterService.convertUploadedFileUsingFactory(originalFileName, fileIndex);

            String html = fileCompressionService.retrieveRootHtml(originalFileName, fileIndex);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Access-Control-Allow-Private-Network", "false");
            headers.add("Access-Control-Allow-Origin", "*");

            String convertedString = StringEscapeUtils.escapeHtml4(html);
            String fileType = originalFileName.substring(originalFileName.lastIndexOf(".")).replace(".", "");

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("index", fileIndex);
            map.put("fileType", fileType);
            map.put("html", convertedString);

            FileVersionManager.releaseIndex(fileType, fileIndex);
            return new ResponseEntity<Object>(map, headers, 200);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Object>("{'error':'NONE'}", null, 500);
        }

    }

    @PostMapping
    public ResponseEntity<String> convertFileByPath(@RequestBody FileDetailsRest fileDetailsRest) throws IOException {
        // TODO: validation of file details
        // TODO: Exception handling
        // TODO: add loggers
        // TODO: application properties

        String html = fileConverterService.convertUsingFactory(fileDetailsRest.getFilePath(),
                fileDetailsRest.getFileName());

        return new ResponseEntity<String>(html, null, 200);
    }
}
