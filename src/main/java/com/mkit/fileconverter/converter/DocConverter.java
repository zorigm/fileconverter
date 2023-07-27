package com.mkit.fileconverter.converter;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.util.IOUtils;
import org.docx4j.Docx4J;
import org.docx4j.Docx4jProperties;
import org.docx4j.convert.out.ConversionFeatures;
import org.docx4j.convert.out.HTMLSettings;
import org.docx4j.convert.out.common.preprocess.Containerization;
import org.docx4j.convert.out.html.SdtToListSdtTagHandler;
import org.docx4j.convert.out.html.SdtWriter;
import org.docx4j.convert.out.html.TagClass;
import org.docx4j.convert.out.html.TagSingleBox;
import org.docx4j.fonts.BestMatchingMapper;
import org.docx4j.fonts.Mapper;
import org.docx4j.fonts.PhysicalFont;
import org.docx4j.fonts.PhysicalFonts;
import org.docx4j.model.fields.FieldUpdater;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.zwobble.mammoth.DocumentConverter;
import org.zwobble.mammoth.Result;

import org.w3c.dom.Document;
import com.mkit.fileconverter.util.FileTypeUtils;
import com.mkit.fileconverter.util.HtmlCleaner;


public class DocConverter implements Converter {

    @Override
    public String convertToHtml(String uploadedFileLocation, int fileIndex) throws IOException {
        try {
            String fileType = FileTypeUtils.getFileType(uploadedFileLocation);
            String convertedFileLocation = FileTypeUtils.getConvertedFileLocation(fileType, fileIndex);
            String convertedExcelIndexedFolderLocation = FileTypeUtils.getIndexedFolderLocation(fileType, fileIndex);

            Files.createDirectory(Path.of(convertedExcelIndexedFolderLocation));
            switch (fileType) {
                case "doc":
                    this.convertDoc(uploadedFileLocation,convertedFileLocation,convertedExcelIndexedFolderLocation);
                    break;
                case "docx":
                    this.convertDocx(uploadedFileLocation,convertedFileLocation,convertedExcelIndexedFolderLocation);
                    break;
                default:
                    break;
            }
            HtmlCleaner.replaceImgTags(convertedFileLocation, "doc", fileIndex);
            
        } catch (ParserConfigurationException | TransformerException e) {
            // TODO Auto-generated catch block
            
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "html";
    }

    private void convertDocx(String uploadedFileLocation, String convertedFileLocation,String convertedExcelIndexedFolderLocation) throws Exception {
        WordprocessingMLPackage wordMLPackage=Docx4J.load(new File(uploadedFileLocation));

    	HTMLSettings htmlSettings = Docx4J.createHTMLSettings();

    	htmlSettings.setImageDirPath(convertedExcelIndexedFolderLocation + "/imgs");
    	htmlSettings.setImageTargetUri("imgs");
    	htmlSettings.setOpcPackage(wordMLPackage);

    	String userCSS = "html, body, div, span, h1, h2, h3, h4, h5, h6, p, a, img,  table, caption, tbody, tfoot, thead, tr, th, td " +
    			"{ margin: 0; padding: 0; border: 0;}" +
    			"body {line-height: 1;} ";
    	htmlSettings.setUserCSS(userCSS);
        SdtWriter.registerTagHandler("@class", new TagClass() );
		SdtWriter.registerTagHandler(Containerization.TAG_BORDERS, new TagSingleBox() );
		SdtWriter.registerTagHandler(Containerization.TAG_SHADING, new TagSingleBox() );
    	
    	
    	// list numbering:  depending on whether you want list numbering hardcoded, or done using <li>.
    	SdtWriter.registerTagHandler("HTML_ELEMENT", new SdtToListSdtTagHandler());
    	
		// Refresh the values of DOCPROPERTY fields 
		FieldUpdater updater = null;
//		updater = new FieldUpdater(wordMLPackage);
//		updater.update(true);
		
		// Set up font mapper (optional)
		// We don't add CSS for a font which isn't physically present.
		// TODO: consider web fonts?
//		Mapper fontMapper = new IdentityPlusMapper(); // better for Windows
		Mapper fontMapper = new BestMatchingMapper(); // better for Linux
		wordMLPackage.setFontMapper(fontMapper);
		
		// .. example of mapping font Times New Roman which doesn't have certain Arabic glyphs
		// eg Glyph "ي" (0x64a, afii57450) not available in font "TimesNewRomanPS-ItalicMT".
		// eg Glyph "ج" (0x62c, afii57420) not available in font "TimesNewRomanPS-ItalicMT".
		// to a font which does
		PhysicalFont font 
				= PhysicalFonts.get("Arial Unicode MS"); 
			// make sure this is in your regex (if any)!!!
//		if (font!=null) {
//			fontMapper.put("Times New Roman", font);
//			fontMapper.put("Arial", font);
//		}
//		fontMapper.put("Libian SC Regular", PhysicalFonts.get("SimSun"));
    	
		
		// output to an OutputStream.		
		OutputStream os = new FileOutputStream(convertedFileLocation);

		// If you want XHTML output
    	Docx4jProperties.setProperty("docx4j.Convert.Out.HTML.OutputMethodXML", true);

		//Don't care what type of exporter you use
		Docx4J.toHTML(htmlSettings, os, Docx4J.FLAG_NONE);

		// Clean up, so any ObfuscatedFontPart temp files can be deleted 
		if (wordMLPackage.getMainDocumentPart().getFontTablePart()!=null) {
			wordMLPackage.getMainDocumentPart().getFontTablePart().deleteEmbeddedFontTempFiles();
		}	
    }

    public String convertDoc(String uploadedFileLocation, String  convertedFileLocation, String convertedExcelIndexedFolderLocation) throws TransformerFactoryConfigurationError, ParserConfigurationException, IOException, TransformerException{
        FileInputStream finStream=new FileInputStream(uploadedFileLocation); 
        HWPFDocument doc=new HWPFDocument(finStream);
        Document newDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(newDocument) ;
        wordToHtmlConverter.setPicturesManager(new PicturesManager() {
            @Override
            public String savePicture(byte[] bytes, PictureType pictureType, String s, float v, float v1) {
                File imageFile = new File(convertedExcelIndexedFolderLocation+"/imgs", s);
                imageFile.getParentFile().mkdirs();
                InputStream in = null;
                FileOutputStream out = null;
                try {
                    in = new ByteArrayInputStream(bytes);
                    out = new FileOutputStream(imageFile);
                    IOUtils.copy(in, out);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (in != null) {
                        IOUtils.closeQuietly(in);
                    }
                    if (out != null) {
                        IOUtils.closeQuietly(out);
                    }
                }
                return "imgs/" + imageFile.getName();
            }   
        });

        wordToHtmlConverter.processDocument(doc);

        StringWriter stringWriter = new StringWriter();
        Transformer transformer = TransformerFactory.newInstance().newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        transformer.setOutputProperty(OutputKeys.METHOD, "html");
        transformer.transform(new DOMSource( wordToHtmlConverter.getDocument()), new StreamResult( stringWriter ) );

        String html = stringWriter.toString();
        File file = new File(convertedFileLocation);
        BufferedWriter writer = null;
        writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8"));
        writer.append(html);
        writer.close();
        return html;
    }
        
}
