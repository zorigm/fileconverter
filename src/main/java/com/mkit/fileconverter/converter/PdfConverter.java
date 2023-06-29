package com.mkit.fileconverter.converter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.fit.pdfdom.PDFDomTree;

import com.mkit.fileconverter.manager.FileVersionManager;
import com.mkit.fileconverter.util.FileTypeUtils;

public class PdfConverter implements Converter {

    @Override
    public String convertToHtml(String uploadedFileLocation, int fileIndex) throws IOException {
        String convertedFileLocation = FileTypeUtils.getConvertedFileLocation("pdf", fileIndex);
        String convertedPdfIndexedFolderLocation = FileTypeUtils.getIndexedFolderLocation("pdf", fileIndex);
        FileVersionManager.createDirectory(convertedPdfIndexedFolderLocation);
        
        PDDocument pdf = PDDocument.load(new File(uploadedFileLocation));
        Writer output = new PrintWriter(convertedFileLocation, "utf-8");
        
        PDFDomTree pdfDomTree = new PDFDomTree();
        pdfDomTree.writeText(pdf, output);
        pdf.close();
        output.close();

        return "DONE";
    }
}
