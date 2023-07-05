package com.mkit.fileconverter.manager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Stack;

import org.apache.commons.io.FileUtils;

import com.mkit.fileconverter.converter.ConverterConstants;
import com.mkit.fileconverter.util.FileTypeUtils;

public class FileVersionManager {

    private final static Stack<Integer> hwpStack = new Stack<Integer>();
    private final static Stack<Integer> doctack = new Stack<Integer>();
    private final static Stack<Integer> xlstack = new Stack<Integer>();
    private final static Stack<Integer> pdfStack = new Stack<Integer>();

    static {
        initializeStack(hwpStack, 30);
        initializeStack(doctack, 30);
        initializeStack(xlstack, 1000);
        initializeStack(pdfStack, 30);
    }

    public static int getNextAvailableIndex(String fileType)
    {
        Stack<Integer> stack = getStackAccordingToFileType(fileType);
        return stack.pop();
    }

    public static void releaseIndex(String fileType, int index) throws IOException
    {
        Stack<Integer> stack = getStackAccordingToFileType(fileType);

        if(stack.empty())
        {
            throw new UnsupportedOperationException();
        }
        deleteRelatedFiles(fileType, index);
        stack.push(Integer.valueOf(index));
    }

    private static void deleteRelatedFiles(String fileType, int index) throws IOException
    {
        String uploadedFileToDelete = FileTypeUtils.getUploadedFileLocation(fileType, index);
        String convertedFolderToDelete = FileTypeUtils.getIndexedFolderLocation(fileType, index);

        if(fileType.equals("xls") || fileType.equals("xlsx"))
        {
            String zipFileName = FileTypeUtils.getIndexedZipFileName(fileType, index);
            String zipFileToDelete = ConverterConstants.ZIP_FOLDER_LOCATION + ConverterConstants.BACKSLASH + zipFileName + ConverterConstants.ZIP_EXTENSION;
            deleteFile(zipFileToDelete);
        }

        deleteFile(uploadedFileToDelete);
        FileUtils.deleteDirectory(new File(convertedFolderToDelete));
    }

    private static void deleteFile(String file)
    {
        File fileToDel = new File(file);
        fileToDel.delete();
    }

    private static void initializeStack(Stack<Integer> stack, int stackLimit)
    {
        for(int i = 1; i <= stackLimit; i++)
        {
            stack.push(i);
        }
    }

    private static Stack<Integer> getStackAccordingToFileType(String fileType)
    {
        switch (fileType) {
            case "hwp":
                return hwpStack;
            case "hwpx":
                return hwpStack;    
            case "doc":
                return doctack;
            case "docx":
                return doctack;
            case "xls":
                return xlstack;
            case "xlsx":
                return xlstack;
            case "pdf":
                return pdfStack;
            default:
                throw new IllegalArgumentException("File type is not supported");
        }
    }
}
