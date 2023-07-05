package com.mkit.fileconverter.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.FileSystemResource;

import com.mkit.fileconverter.converter.ConverterConstants;
import com.mkit.fileconverter.util.FileTypeUtils;

public class DeleteAfterReadeFileSystemResource extends FileSystemResource {
    private int index;
    private String fileType;

    public DeleteAfterReadeFileSystemResource(File file) {
        super(file);
    }

    public DeleteAfterReadeFileSystemResource(File file, int index, String fileType)
    {
        super(file);
        this.index = index;
        this.fileType = fileType;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new DeleteOnCloseFileInputStream(super.getFile(), index, fileType);
    }

    private static final class DeleteOnCloseFileInputStream extends FileInputStream {

        private File file;
        private int index;
        private String fileType;
        DeleteOnCloseFileInputStream(File file, int index, String fileType) throws FileNotFoundException    {
            super(file);
            this.file = file;
            this.index = index;
            this.fileType = fileType;
        }

        @Override
        public void close() throws IOException {
            super.close();
            // String zipFileName = FileTypeUtils.getIndexedZipFileName(fileType, index);
            // String zipFileToDelete = ConverterConstants.ZIP_FOLDER_LOCATION + ConverterConstants.BACKSLASH + zipFileName + ConverterConstants.ZIP_EXTENSION;
            // deleteFile(zipFileToDelete);
            // file.delete();

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
    }

    private static void deleteFile(String file)
    {
        File fileToDel = new File(file);
        fileToDel.delete();
    }
}
