package com.mkit.fileconverter.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

import com.mkit.fileconverter.converter.ConverterConstants;

public class FileTypeUtils {
    private static final Map<String, String> uploadedFileLocations = new HashMap<>();
    private static final Map<String, String> zipLocations = new HashMap<>();
    private static final Map<String, String> directoryToCompress = new HashMap<>();
    private static final Map<String, String> rootHtmlLocations = new HashMap<>();
    private static final Map<String, String> rootStylesLocations = new HashMap<>();
    private static final Map<String, String> imageFolderLocations = new HashMap<>();

    static
    {
        uploadedFileLocations.put("pdf", ConverterConstants.UPLOADED_PDF_FILE_LOCATION);

        uploadedFileLocations.put("doc", ConverterConstants.UPLOADED_DOC_FILE_LOCATION);
        uploadedFileLocations.put("docx", ConverterConstants.UPLOADED_DOCX_FILE_LOCATION);

        uploadedFileLocations.put("hwp", ConverterConstants.UPLOADED_HWP_FILE_LOCATION);

        uploadedFileLocations.put("xls", ConverterConstants.UPLOADED_XLS_FILE_LOCATION);
        uploadedFileLocations.put("xlsx", ConverterConstants.UPLOADED_XLSX_FILE_LOCATION);

        zipLocations.put("xls", ConverterConstants.XLS_TEMP_FILE_NAME_WITHOUT_EXTENSTION);
        zipLocations.put("xlsx", ConverterConstants.XLS_TEMP_FILE_NAME_WITHOUT_EXTENSTION);

        zipLocations.put("doc", ConverterConstants.DOC_TEMP_FILE_NAME_WITHOUT_EXTENSION);
        zipLocations.put("docx", ConverterConstants.DOC_TEMP_FILE_NAME_WITHOUT_EXTENSION);

        zipLocations.put("pdf", ConverterConstants.PDF_TEMP_FILE_NAME_WITHOUT_EXTENSION);

        zipLocations.put("hwp", ConverterConstants.HWP_TEMP_FOLDER_NAME);
        zipLocations.put("hwpx", ConverterConstants.HWP_TEMP_FOLDER_NAME);


        directoryToCompress.put("xls", ConverterConstants.CONVERTED_EXCEL_LOCATION);
        directoryToCompress.put("xlsx", ConverterConstants.CONVERTED_EXCEL_LOCATION);

        directoryToCompress.put("doc", ConverterConstants.CONVERTED_DOC_FOLDER_LOCATION);
        directoryToCompress.put("docx", ConverterConstants.CONVERTED_DOC_FOLDER_LOCATION);

        directoryToCompress.put("pdf", ConverterConstants.CONVERTED_PDF_FOLDER_LOCATION);

        directoryToCompress.put("hwp", ConverterConstants.CONVERTED_HWP_FOLDER_LOCATION);
        directoryToCompress.put("hwpx", ConverterConstants.CONVERTED_HWP_FOLDER_LOCATION);


        rootHtmlLocations.put("pdf", ConverterConstants.PDF_CONVERTED_FILE_LOCATION);

        rootHtmlLocations.put("xls", ConverterConstants.XLS_CONVERTED_ROOT_HTML_FILE_LOCATION);
        rootHtmlLocations.put("xlsx", ConverterConstants.XLS_CONVERTED_ROOT_HTML_FILE_LOCATION);

        rootHtmlLocations.put("doc", ConverterConstants.DOC_CONVERTED_FILE_LOCATION);
        rootHtmlLocations.put("docx", ConverterConstants.DOC_CONVERTED_FILE_LOCATION);

        rootHtmlLocations.put("hwp", ConverterConstants.HWP_ROOT_CONVERTED_FILE_LOCATION);
        rootHtmlLocations.put("hwpx", ConverterConstants.HWP_ROOT_CONVERTED_FILE_LOCATION);


        imageFolderLocations.put("doc", ConverterConstants.CONVERTED_DOC_FOLDER_LOCATION);
        imageFolderLocations.put("doc", ConverterConstants.CONVERTED_DOC_FOLDER_LOCATION);

        imageFolderLocations.put("hwp", ConverterConstants.HWP_IMAGE_FOLDER_LOCATION);


        rootStylesLocations.put("hwp", ConverterConstants.HWP_ROOT_STYLES_FILE_LOCATION);
        rootStylesLocations.put("hwpx", ConverterConstants.HWP_ROOT_STYLES_FILE_LOCATION);
    }
    
    public static String getFileType(String fileName)
    {
        //TODO: validate fileName

        String fileType = FilenameUtils.getExtension(fileName);

        //TODO: validate filetype and exception handling

        return fileType;
    }

    public static String getUploadedFileLocation(String fileType)
    {
        //TODO: validate fileType
        String fileLocation = uploadedFileLocations.get(fileType);

        //TODO: throw exception if not found
        return fileLocation;
    }

    public static String getZipLocation(String fileType)
    {
        //TODO: validate fileType

        String zipLocation = zipLocations.get(fileType);

        //TODO: validate ziplocation and exception handling
        return zipLocation;
    }

    public static String getDirectoryToZip(String fileType)
    {
        //TODO: validate file type

         String directoryToZip = directoryToCompress.get(fileType);

         //TODO: validate directoryToZip and exception handling

         return directoryToZip;
    }

    public static String getRootHtml(String fileType)
    {
        //TODO: validate file type

        String rootHtmlLocation = rootHtmlLocations.get(fileType);

        //TODO: validate rootHtmlLocation

        return rootHtmlLocation;
    }

    public static String getRootStylesLocation(String fileType)
    {
        //TODO: validate file type

        String rootStyleLocation = rootStylesLocations.get(fileType);

        //TODO: validate rootStyleLocation

        return rootStyleLocation;
    }

    public static String getImagesFolderLocation(String fileType) {
        //TODO: validate file type

        String imageFolderLocation = imageFolderLocations.get(fileType);

        //TODO: validate imageFolderLocation

        return imageFolderLocation;
    }
}
