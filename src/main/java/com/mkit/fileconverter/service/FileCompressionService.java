package com.mkit.fileconverter.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.utils.IOUtils;
import org.springframework.stereotype.Service;

import com.mkit.fileconverter.converter.ConverterConstants;
import com.mkit.fileconverter.util.FileTypeUtils;

import jakarta.servlet.ServletOutputStream;

@Service
public class FileCompressionService {

    public String retrieveRootHtml(String file, int index)
    {
        String fileType = FileTypeUtils.getFileType(file);
        String rootHtmlLocation = FileTypeUtils.getConvertedFileLocation(fileType, index);
        String rootStyleLocation = FileTypeUtils.getRootStylesLocation(fileType, index);
        String rootHtml = getStringFromHtmlFile(rootHtmlLocation);

        if(rootStyleLocation == null)
        {
            return rootHtml;
        }
        
        String rootStyle = getRootStyle(rootStyleLocation);

        return combineRootStyleAndRootHtml(rootHtml, rootStyle);
    }

    public String getCompressedFile(String file, ServletOutputStream servletOutputStream) throws IOException
    {
        String fileType = FileTypeUtils.getFileType(file);
        String zipLocation = FileTypeUtils.getZipLocation(fileType);
        String zippedFolderLocation = ConverterConstants.ZIP_FOLDER_LOCATION + ConverterConstants.BACKSLASH + zipLocation + ConverterConstants.ZIP_EXTENSION;
        
        ZipOutputStream zipOutputStream = new ZipOutputStream(servletOutputStream);
        zipOutputStream.putNextEntry(new ZipEntry(zippedFolderLocation));
        FileInputStream fileInputStream = new FileInputStream(new File(zippedFolderLocation));

        IOUtils.copy(fileInputStream, zipOutputStream);

        fileInputStream.close();
        zipOutputStream.closeEntry();

        return zipLocation;
    }
    
    public String compressFile(String file) throws IOException
    {
        String fileType = FileTypeUtils.getFileType(file);
        String sourceFile = FileTypeUtils.getDirectoryToZip(fileType);
        String zipLocation = FileTypeUtils.getZipLocation(fileType);

        FileOutputStream fos = new FileOutputStream(ConverterConstants.ZIP_FOLDER_LOCATION + ConverterConstants.BACKSLASH + zipLocation + ConverterConstants.ZIP_EXTENSION);
        ZipOutputStream zipOut = new ZipOutputStream(fos);

        File fileToZip = new File(sourceFile);
        zipFile(fileToZip, fileToZip.getName(), zipOut);
        zipOut.close();
        fos.close();
        return zipLocation;
    }

    private void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException
    {
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

    private String getStringFromHtmlFile(String file)
    {
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

    private String getRootStyle(String file)
    {
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

    private String combineRootStyleAndRootHtml(String html, String style)
    {
        return html.split("</head>")[0] + "</head><style>" + style + "</style>" + html.split("</head>")[1];
    }
}


