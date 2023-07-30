package com.mkit.fileconverter.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.utils.IOUtils;
import org.checkerframework.checker.units.qual.h;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.mkit.fileconverter.converter.ConverterConstants;
import com.mkit.fileconverter.util.FileTypeUtils;
import com.mkit.fileconverter.util.ZipUtility;

import jakarta.servlet.ServletOutputStream;

@Service
public class FileCompressionService {

    public String retrieveRootHtml(String file, int index) {
        String fileType = FileTypeUtils.getFileType(file).toString();
        String rootHtmlLocation = FileTypeUtils.getConvertedFileLocation(fileType, index);
        String rootStyleLocation = FileTypeUtils.getRootStylesLocation(fileType, index);
        String rootHtml = getStringFromHtmlFile(rootHtmlLocation);

        if (fileType.toLowerCase().equals("pdf")) {
            rootHtml = pdfStyleAndRootHtml(rootHtml);
        }
        if (fileType.toLowerCase().equals("docx")) {
            rootHtml = docxStyleAndRootHtml(rootHtml);
        }
        if (fileType.toLowerCase().equals("doc")) {
            rootHtml = docStyleAndRootHtml(rootHtml);
        }
        if (fileType.toLowerCase().equals("hwp")) {
            rootHtml = hwpStyleAndRootHtml(rootHtml);
        }
        if (rootStyleLocation != null) {
            String rootStyle = getRootStyle(rootStyleLocation);
            rootHtml = combineRootStyleAndRootHtml(rootHtml, rootStyle);
        }

        return rootHtml;
    }

    public String getCompressedFile(String file, int index, ServletOutputStream servletOutputStream)
            throws IOException {
        String fileType = FileTypeUtils.getFileType(file);
        String zipFileName = FileTypeUtils.getIndexedZipFileName(fileType, index);
        String zippedFolderLocation = ConverterConstants.ZIP_FOLDER_LOCATION + ConverterConstants.BACKSLASH
                + zipFileName + ConverterConstants.ZIP_EXTENSION;

        ZipOutputStream zipOutputStream = new ZipOutputStream(servletOutputStream);
        zipOutputStream.putNextEntry(new ZipEntry(zippedFolderLocation));
        FileInputStream fileInputStream = new FileInputStream(new File(zippedFolderLocation));

        IOUtils.copy(fileInputStream, zipOutputStream);

        fileInputStream.close();
        zipOutputStream.closeEntry();

        return zipFileName;
    }

    public String compressFile(String file, int index) throws IOException {
        String fileType = FileTypeUtils.getFileType(file);
        String sourceFile = FileTypeUtils.getDirectoryToZip(fileType, index);
        String zipFileName = FileTypeUtils.getIndexedZipFileName(fileType, index);

        // FileOutputStream fos = new
        // FileOutputStream(ConverterConstants.ZIP_FOLDER_LOCATION +
        // ConverterConstants.BACKSLASH + zipFileName +
        // ConverterConstants.ZIP_EXTENSION);
        // ZipOutputStream zipOut = new ZipOutputStream(fos);

        // File fileToZip = new File(sourceFile);
        // zipFile(fileToZip, fileToZip.getName(), zipOut);
        // zipOut.close();
        // fos.close();

        // String zippedUrl = zipDirectory(sourceFile, zipFileName);
        ZipUtility zipUtility = new ZipUtility();
        List<File> files = new ArrayList<>();
        files.add(new File(sourceFile + "/placeholder-doc.html"));
        File img = new File(sourceFile + "/imgs");
        if (img.exists())
            files.add(new File(sourceFile + "/imgs"));

        zipUtility.zip(files, ConverterConstants.ZIP_FOLDER_LOCATION
                + ConverterConstants.BACKSLASH + zipFileName + ConverterConstants.ZIP_EXTENSION);
        System.out.println(ConverterConstants.ZIP_FOLDER_LOCATION
                + ConverterConstants.BACKSLASH + zipFileName + ConverterConstants.ZIP_EXTENSION);
        return ConverterConstants.ZIP_FOLDER_LOCATION + ConverterConstants.BACKSLASH + zipFileName
                + ConverterConstants.ZIP_EXTENSION;
        // return zippedUrl;
    }

    private String zipDirectory(String fileToZip, String zipFileName) throws FileNotFoundException, IOException {
        ZipUtility zipUtility = new ZipUtility();

        zipUtility.zip(Collections.singletonList(new File(fileToZip)), ConverterConstants.ZIP_FOLDER_LOCATION
                + ConverterConstants.BACKSLASH + zipFileName + ConverterConstants.ZIP_EXTENSION);
        return ConverterConstants.ZIP_FOLDER_LOCATION + ConverterConstants.BACKSLASH + zipFileName
                + ConverterConstants.ZIP_EXTENSION;
    }

    private void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }

    private String getStringFromHtmlFile(String file) {
        StringBuilder contentBuilder = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
            String str;
            while ((str = in.readLine()) != null) {
                contentBuilder.append(str);
            }
            in.close();
        } catch (IOException e) {
        }
        String content = contentBuilder.toString();

        return content;
    }

    private String getRootStyle(String file) {
        StringBuilder contentBuilder = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
            String str;
            while ((str = in.readLine()) != null) {
                contentBuilder.append(str);
            }
            in.close();
        } catch (IOException e) {
        }
        String content = contentBuilder.toString();

        return content;
    }

    private String combineRootStyleAndRootHtml(String html, String style) {
        return html.split("</head>")[0] + "</head><style>" + style + "</style>" + html.split("</head>")[1];
    }

    private String cssFileRead(String type) {
        String style = "";
        try (BufferedReader in = new BufferedReader(
                new FileReader(ResourceUtils.getFile("classpath:" + type + "Style.css")))) {
            String line;
            while ((line = in.readLine()) != null) {
                style = style + line + "\n";
            }
        } catch (IOException e) {
            throw new IllegalStateException("Reading standard css", e);
        }
        return style;
    }

    private String pdfStyleAndRootHtml(String html) {
        String style = cssFileRead("pdf");
        html = html.replaceAll("}#page-container", "}#page-remove_container");
        html = html.replaceAll("#sidebar", "#sidebar-remove");
        html = html.replaceAll("}#sidebar-remove", "}#sidebar{display:none;}#sidebar-remove");
        html = combineRootStyleAndRootHtml(html, style);
        return html;
    }

    private String docxStyleAndRootHtml(String html) {
        String style = cssFileRead("docx");
        html = html.replaceAll("class=\"1 a", "class=\"font-1 a");
        html = html.replaceAll("class=\"2 a", "class=\"font-2 a");
        html = html.replaceAll("class=\"3 a", "class=\"font-3 a");
        html = html.replaceAll("class=\"10 a", "class=\"padding-10 a");
        html = html.replaceAll("class=\"20 a", "class=\"padding-20 a");
        html = html.replaceAll("class=\"30 a", "class=\"padding-30 a");
        html = combineRootStyleAndRootHtml(html, style);
        return html;
    }

    private String docStyleAndRootHtml(String html) {
        html = html.replace("<span> TOC \\o \"1-3\" \\h \\z \\u </span>", "");
        String style = cssFileRead("doc");
        html = combineRootStyleAndRootHtml(html, style);
        return html;
    }

    private String hwpStyleAndRootHtml(String html) {
        String style = cssFileRead("hwp");
        html = combineRootStyleAndRootHtml(html, style);
        return html;
    }

}
