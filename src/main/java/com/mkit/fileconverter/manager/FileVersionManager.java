package com.mkit.fileconverter.manager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Stack;

public class FileVersionManager {

    private final static Stack<Integer> hwpStack = new Stack<Integer>();
    private final static Stack<Integer> doctack = new Stack<Integer>();
    private final static Stack<Integer> xlstack = new Stack<Integer>();
    private final static Stack<Integer> pdfStack = new Stack<Integer>();

    static {
        initializeStack(hwpStack, 30);
        initializeStack(doctack, 30);
        initializeStack(xlstack, 30);
        initializeStack(pdfStack, 30);
    }

    public static void createDirectory(String filePath) throws IOException
    {
        Files.createDirectory(Paths.get(filePath));
    }

    public static void deleteProcessedFile(String fileType, int index)
    {
        //delete in here
        //free up index in queue
        throw new UnsupportedOperationException();
    }

    public static int getNextAvailableIndex(String fileType)
    {
        Stack<Integer> stack = getStackAccordingToFileType(fileType);
        return stack.pop();
    }

    public void releaseIndex(String fileType, int index)
    {
        Stack<Integer> stack = getStackAccordingToFileType(fileType);

        if(stack.empty())
        {
            throw new UnsupportedOperationException();
        }

        stack.push(Integer.valueOf(index));
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
