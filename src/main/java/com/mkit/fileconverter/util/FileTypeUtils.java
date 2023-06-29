package com.mkit.fileconverter.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

import com.mkit.fileconverter.converter.ConverterConstants;

public class FileTypeUtils {
    //private static final Map<String, String> uploadedFileLocations = new HashMap<>();
    private static final Map<String, String> zipLocations = new HashMap<>();
    private static final Map<String, String> directoryToCompress = new HashMap<>();
    private static final Map<String, String> rootHtmlLocations = new HashMap<>();
    private static final Map<String, String> rootStylesLocations = new HashMap<>();
    private static final Map<String, String> imageFolderLocations = new HashMap<>();
    private static final Map<String, String> indexedFolderLocations = new HashMap<>();
    private static final Map<String, String> convertedFileNames = new HashMap<>();
    private static final Map<String, String> uploadedFileNames = new HashMap<>();
    private static final Map<String, String> rootHtmlLocationAfterIndexedPath = new HashMap<>();

    static
    {
        // uploadedFileLocations.put("pdf", ConverterConstants.UPLOADED_PDF_FILE_LOCATION);

        // uploadedFileLocations.put("doc", ConverterConstants.UPLOADED_DOC_FILE_LOCATION);
        // uploadedFileLocations.put("docx", ConverterConstants.UPLOADED_DOCX_FILE_LOCATION);

        // uploadedFileLocations.put("hwp", ConverterConstants.UPLOADED_HWP_FILE_LOCATION);

        // uploadedFileLocations.put("xls", ConverterConstants.UPLOADED_XLS_FILE_LOCATION);
        // uploadedFileLocations.put("xlsx", ConverterConstants.UPLOADED_XLSX_FILE_LOCATION);

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


        indexedFolderLocations.put("pdf", ConverterConstants.CONVERTED_PDF_INDEXED_FOLDER_LOCATION);

        indexedFolderLocations.put("xls", null);
        indexedFolderLocations.put("xlsx", null);

        indexedFolderLocations.put("doc", null);
        indexedFolderLocations.put("docx", null);
        
        indexedFolderLocations.put("hwp", null);
        indexedFolderLocations.put("hwpx", null);


        convertedFileNames.put("pdf", ConverterConstants.PDF_TEMP_FILE_NAME);

        convertedFileNames.put("doc", null);
        convertedFileNames.put("docx", null);

        convertedFileNames.put("xls", null);
        convertedFileNames.put("xlsx", null);

        convertedFileNames.put("hwp", null);
        convertedFileNames.put("hwpx", null);


        uploadedFileNames.put("pdf", ConverterConstants.UPLOADED_PDF_FILE_NAME_WITHOUT_EXTENSION);

        uploadedFileNames.put("doc", null);
        uploadedFileNames.put("docx", null);

        uploadedFileNames.put("xls", null);
        uploadedFileNames.put("xlsx", null);

        uploadedFileNames.put("hwp", null);
        uploadedFileNames.put("hwpx", null);


        rootHtmlLocationAfterIndexedPath.put("pdf", null);

        rootHtmlLocationAfterIndexedPath.put("doc", null);
        rootHtmlLocationAfterIndexedPath.put("docx", null);

        rootHtmlLocationAfterIndexedPath.put("xls", null);
        rootHtmlLocationAfterIndexedPath.put("xlsx", null);

        rootHtmlLocationAfterIndexedPath.put("hwp", null);
        rootHtmlLocationAfterIndexedPath.put("hwpx", null);
    }
    
    public static String getFileType(String fileName)
    {
        //TODO: validate fileName

        String fileType = FilenameUtils.getExtension(fileName);

        //TODO: validate filetype and exception handling

        return fileType;
    }

    public static String getUploadedFileLocation(String fileType, int index)
    {
        //TODO: validate fileType
        String fileName = uploadedFileNames.get(fileType);
        String fileExtension = "." + fileType;

        //TODO: throw exception if not found
        return ConverterConstants.TEMP_UPLOADED_FILE_LOCATION + ConverterConstants.BACKSLASH + fileName + ConverterConstants.DASH + index + fileExtension;
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

    public static String getRootStylesLocation(String fileType, int index)
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

    public static String getConvertedFileLocation(String fileType, int index)
    {
        String indexedFolderLocation = getIndexedFolderLocation(fileType, index);
        String convertedFileName = getConvertedFileName(fileType);

        return indexedFolderLocation + ConverterConstants.BACKSLASH + convertedFileName;
    }

    public static String getIndexedFolderLocation(String fileType, int index)
    {
        //TODO: validate file type

        String indexedFolderLocation = indexedFolderLocations.get(fileType);

        //TODO: validate imageFolderLocation

        return indexedFolderLocation + ConverterConstants.DASH + index;
    }

    public static String getConvertedFileName(String fileType)
    {
        //TODO: validate file type

        String convertedFileName = convertedFileNames.get(fileType);

        //TODO: validate convertedFileName

        return convertedFileName;
    }
}
