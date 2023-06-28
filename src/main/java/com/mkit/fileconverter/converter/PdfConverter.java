package com.mkit.fileconverter.converter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.fit.pdfdom.PDFDomTree;

public class PdfConverter implements Converter {

    @Override
    public String convertToHtml(String uploadedFileLocation) throws IOException {
        
        PDDocument pdf = PDDocument.load(new File(uploadedFileLocation));
        Writer output = new PrintWriter(ConverterConstants.PDF_CONVERTED_FILE_LOCATION, "utf-8");
        
        PDFDomTree pdfDomTree = new PDFDomTree();
        pdfDomTree.writeText(pdf, output);
        output.close();

        return "DONE";
    }
}
