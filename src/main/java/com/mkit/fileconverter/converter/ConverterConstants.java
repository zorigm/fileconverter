package com.mkit.fileconverter.converter;

import com.mkit.fileconverter.util.OsUtils;

public final class ConverterConstants {
    //TODO: Initialize temp folder
    public static final String BACKSLASH = "/";
    public static final Enum OS_ENUM = OsUtils.getOS();
    public static final String TEMP_LOCATION = OS_ENUM.equals(OsUtils.OS.WINDOWS) ? System.getProperty("java.io.tmpdir") : System.getProperty("java.io.tmpdir") + BACKSLASH;

    public static final String HTML_EXTENSION = ".html";
    public static final String XHTML_EXTENSION = ".xhtml";
    public static final String PDF_EXTENSION = ".pdf";
    public static final String DOC_EXTENSION = ".doc";
    public static final String DOCX_EXTENSION = ".docx";
    public static final String XLS_EXTENSION = ".xls";
    public static final String XLSX_EXTENSION = ".xlsx";
    public static final String HWP_EXTENSION = ".hwp";
    public static final String HWPX_EXTENSION = ".hwpx";
    public static final String ZIP_EXTENSION = ".zip";
    public static final String CSS_EXTENSION = ".css";

    public static final String CONVERTER_FOLDER_NAME = "converter-files";
    public static final String CONVERTER_FOLDER_LOCATION = TEMP_LOCATION + CONVERTER_FOLDER_NAME;

    public static final String ZIP_FOLDER_NAME = "zipped-files";
    public static final String ZIP_FOLDER_LOCATION = CONVERTER_FOLDER_LOCATION + BACKSLASH + ZIP_FOLDER_NAME;

    public static final String TEMP_UPLOADED_FOLDER_NAME = "uploaded";
    public static final String TEMP_UPLOADED_FILE_LOCATION = CONVERTER_FOLDER_LOCATION + BACKSLASH + TEMP_UPLOADED_FOLDER_NAME;

    public static final String UPLOADED_PDF_FILE_NAME_WITHOUT_EXTENSION = "uploaded-pdf";
    public static final String UPLOADED_PDF_FILE_NAME = UPLOADED_PDF_FILE_NAME_WITHOUT_EXTENSION + PDF_EXTENSION;
    public static final String UPLOADED_PDF_FILE_LOCATION = TEMP_UPLOADED_FILE_LOCATION + BACKSLASH + UPLOADED_PDF_FILE_NAME;

    public static final String UPLOADED_DOC_FILE_NAME_WITHOUT_EXTENSION = "uploaded-doc";
    public static final String UPLOADED_DOC_FILE_NAME = UPLOADED_DOC_FILE_NAME_WITHOUT_EXTENSION + DOC_EXTENSION;
    public static final String UPLOADED_DOC_FILE_LOCATION = TEMP_UPLOADED_FILE_LOCATION + BACKSLASH + UPLOADED_DOC_FILE_NAME;
    public static final String UPLOADED_DOCX_FILE_NAME = UPLOADED_DOC_FILE_NAME_WITHOUT_EXTENSION + DOCX_EXTENSION;
    public static final String UPLOADED_DOCX_FILE_LOCATION = TEMP_UPLOADED_FILE_LOCATION + BACKSLASH + UPLOADED_DOCX_FILE_NAME;

    public static final String UPLOADED_XLS_FILE_NAME_WITHOUT_EXTENSION = "uploaded-xls";
    public static final String UPLOADED_XLS_FILE_NAME = UPLOADED_XLS_FILE_NAME_WITHOUT_EXTENSION + XLS_EXTENSION;
    public static final String UPLOADED_XLS_FILE_LOCATION = TEMP_UPLOADED_FILE_LOCATION + BACKSLASH + UPLOADED_XLS_FILE_NAME;
    public static final String UPLOADED_XLSX_FILE_NAME = UPLOADED_XLS_FILE_NAME_WITHOUT_EXTENSION + XLSX_EXTENSION;
    public static final String UPLOADED_XLSX_FILE_LOCATION = TEMP_UPLOADED_FILE_LOCATION + BACKSLASH + UPLOADED_XLSX_FILE_NAME;

    public static final String UPLOADED_HWP_FILE_NAME_WITHOUT_EXTENSION = "uploaded-hwp";
    public static final String UPLOADED_HWP_FILE_NAME = UPLOADED_HWP_FILE_NAME_WITHOUT_EXTENSION + HWP_EXTENSION;
    public static final String UPLOADED_HWP_FILE_LOCATION = TEMP_UPLOADED_FILE_LOCATION + BACKSLASH +  UPLOADED_HWP_FILE_NAME;

    public static final String CONVERTED_EXCEL_FOLDER_NAME = "excel-folder";
    public static final String CONVERTED_EXCEL_LOCATION = CONVERTER_FOLDER_LOCATION + BACKSLASH + CONVERTED_EXCEL_FOLDER_NAME;
    public static final String XLS_TEMP_FILE_NAME_WITHOUT_EXTENSTION = "placeholder-excel";
    public static final String XLS_TEMP_FILE_NAME = XLS_TEMP_FILE_NAME_WITHOUT_EXTENSTION + HTML_EXTENSION;
    public static final String XLS_TEMP_FOLDER_NAME = XLS_TEMP_FILE_NAME_WITHOUT_EXTENSTION + "_files";
    public static final String XLS_TEMP_FOLDER_LOCATION = CONVERTED_EXCEL_LOCATION + BACKSLASH + XLS_TEMP_FOLDER_NAME;
    public static final String XLS_CONVERTED_ROOT_HTML_FILE_LOCATION = CONVERTED_EXCEL_LOCATION + BACKSLASH + XLS_TEMP_FILE_NAME;

    public static final String CONVERTED_DOC_FOLDER_NAME = "doc-folder";
    public static final String CONVERTED_DOC_FOLDER_LOCATION = CONVERTER_FOLDER_LOCATION + BACKSLASH + CONVERTED_DOC_FOLDER_NAME;
    public static final String DOC_TEMP_FILE_NAME_WITHOUT_EXTENSION= "placeholder-doc";
    public static final String DOC_TEMP_FILE_NAME = DOC_TEMP_FILE_NAME_WITHOUT_EXTENSION + HTML_EXTENSION;
    public static final String DOC_CONVERTED_FILE_LOCATION = CONVERTED_DOC_FOLDER_LOCATION + BACKSLASH + DOC_TEMP_FILE_NAME;

    public static final String CONVERTED_PDF_FOLDER_NAME = "pdf-folder";
    public static final String CONVERTED_PDF_FOLDER_LOCATION = CONVERTER_FOLDER_LOCATION + BACKSLASH + CONVERTED_PDF_FOLDER_NAME;
    public static final String PDF_TEMP_FILE_NAME_WITHOUT_EXTENSION = "placeholder-pdf";
    public static final String PDF_TEMP_FILE_NAME = PDF_TEMP_FILE_NAME_WITHOUT_EXTENSION + HTML_EXTENSION;
    public static final String PDF_CONVERTED_FILE_LOCATION = CONVERTED_PDF_FOLDER_LOCATION + BACKSLASH + PDF_TEMP_FILE_NAME;

    public static final String CONVERTED_HWP_FOLDER_NAME = "hwp-folder";
    public static final String CONVERTED_HWP_FOLDER_LOCATION = CONVERTER_FOLDER_LOCATION + BACKSLASH + CONVERTED_HWP_FOLDER_NAME;
    public static final String HWP_TEMP_FOLDER_NAME = "uploaded-hwp";
    public static final String HWP_TEMP_FOLDER_LOCATION = CONVERTED_HWP_FOLDER_LOCATION + BACKSLASH + HWP_TEMP_FOLDER_NAME;
    public static final String HWP_ROOT_CONVERTED_FILE_NAME_WITHOUT_EXTENSION = "index";
    public static final String HWP_ROOT_CONVERTED_FILE_NAME = HWP_ROOT_CONVERTED_FILE_NAME_WITHOUT_EXTENSION + XHTML_EXTENSION;
    public static final String HWP_ROOT_CONVERTED_FILE_LOCATION = HWP_TEMP_FOLDER_LOCATION + BACKSLASH + HWP_ROOT_CONVERTED_FILE_NAME;
    public static final String HWP_ROOT_STYLES_FILE_NAME_WITHOUT_EXTENSION = "styles";
    public static final String HWP_ROOT_STYLES_FILE_NAME = HWP_ROOT_STYLES_FILE_NAME_WITHOUT_EXTENSION + CSS_EXTENSION;
    public static final String HWP_ROOT_STYLES_FILE_LOCATION = HWP_TEMP_FOLDER_LOCATION + BACKSLASH + HWP_ROOT_STYLES_FILE_NAME;
    public static final String HWP_IMAGE_FOLDER_NAME = "bindata";
    public static final String HWP_IMAGE_FOLDER_LOCATION = HWP_TEMP_FOLDER_LOCATION + BACKSLASH;
}
