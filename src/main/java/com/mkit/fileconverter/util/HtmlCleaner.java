package com.mkit.fileconverter.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.poi.hslf.blip.Bitmap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import com.mkit.fileconverter.converter.ConverterConstants;

public class HtmlCleaner {

    private static final String TAB_LIST_FILE_NAME = "tabstrip.htm";
    private static final String TAB_LIST_FILE_LOCATION = ConverterConstants.XLS_TEMP_FOLDER_LOCATION + ConverterConstants.BACKSLASH + TAB_LIST_FILE_NAME;

    private static final String FILE_LIST_FILE_NAME = "filelist.xml";
    private static final String FILE_LIST_FILE_LOCATION = ConverterConstants.XLS_TEMP_FOLDER_LOCATION + ConverterConstants.BACKSLASH + FILE_LIST_FILE_NAME;

    private static final String DOC_FIRST_PIC_FILE_NAME = ConverterConstants.DOC_TEMP_FILE_NAME_WITHOUT_EXTENSION + ".001" + ".png";
    private static final String DOC_FIRST_PIC_FILE_LOCATION = ConverterConstants.CONVERTED_DOC_FOLDER_LOCATION + ConverterConstants.BACKSLASH + DOC_FIRST_PIC_FILE_NAME;

    private static final Map<String, String> BASE_64_IMAGE_SOURCE_ROOT = new HashMap<>();

    private static final List<String> removableTexts = new ArrayList<>();
    private static final List<String> usableFiles = new ArrayList<>();
    private static final List<String> deletableFiles = new ArrayList<>();
    
    static
    {
        removableTexts.add("Evaluation Only. Created with Aspose.Words. Copyright 2003-2023 Aspose Pty Ltd.");
        removableTexts.add("Created with an evaluation copy of Aspose.Words. To discover the full versions of our APIs please visit: https://products.aspose.com/words/");
        removableTexts.add("Evaluation Warning");
        removableTexts.add("Variables");
        removableTexts.add("Evaluation Only. Created with Aspose.Cells for Java.Copyright 2003 - 2023 Aspose Pty Ltd.");

        BASE_64_IMAGE_SOURCE_ROOT.put("png", "data:image/png;base64,");
        BASE_64_IMAGE_SOURCE_ROOT.put("jpeg", "data:image/png;base64,");
        BASE_64_IMAGE_SOURCE_ROOT.put("svg", "data:image/svg+xml;base64,");
        BASE_64_IMAGE_SOURCE_ROOT.put("bmp", "data:image/bmp;base64,");
        BASE_64_IMAGE_SOURCE_ROOT.put("gif", "data:image/gif;base64,");
    }

    public static void replaceImgTags(String file, String fileType) throws IOException
    {
        Document doc = Jsoup.parse(new File(file));
        Iterator<Element> imgIterator = doc.getElementsByTag("img").iterator();


        while(imgIterator.hasNext())
        {
            Element imgElement = imgIterator.next();

            String currentImageSource = imgElement.attributes().get("src");

            String folderLocation = FileTypeUtils.getImagesFolderLocation(fileType);

            String imageSourceFileLocation = folderLocation + ConverterConstants.BACKSLASH + currentImageSource;

            //String imageSourceFileLocation = ConverterConstants.CONVERTED_DOC_FOLDER_LOCATION + ConverterConstants.BACKSLASH + currentImageSource;

            String base64 = getBase64OfImage(imageSourceFileLocation);
            //String base64Source = BASE_64_IMAGE_SOURCE_ROOT + base64;

            imgElement.attributes().remove("src");
            imgElement.attributes().put("src", base64);

        }

        Files.writeString(Path.of(file), doc.outerHtml());
    }
    
    public static void cleanDocHtml(String file) throws IOException
    {
        usableFiles.clear();
        deletableFiles.clear();

        Document doc = Jsoup.parse(new File(file));
        
        removeFirstPicture(doc);
        removeEvaluationMarksFromDoc(doc);
        deleteFirstPictureFile();

        Files.writeString(Path.of(file), doc.outerHtml());
    }

    public static void cleanExcelHtml() throws IOException
    {
        usableFiles.clear();
        deletableFiles.clear();

        findTabsToDeleteAndKeep();
        removeTabsFromFile();
        removeEvaluationMarksFromEachFile();
        replaceDefaultSheet();
        removeFromFileList(Jsoup.parse(new File(FILE_LIST_FILE_LOCATION), null, "", Parser.xmlParser()));
    }

    private static void deleteFirstPictureFile()
    {
        File fileToDel = new File(DOC_FIRST_PIC_FILE_LOCATION);
        fileToDel.delete();
    }

    private static void findTabsToDeleteAndKeep() throws IOException
    {
        Document doc = Jsoup.parse(new File(TAB_LIST_FILE_LOCATION));
        Iterator<Element> elementIterator = doc.select("td").iterator();

        while(elementIterator.hasNext())
        {
            Element tableData = elementIterator.next();
            if(removableTexts.contains(tableData.getElementsByTag("font").text()))
            {
                deletableFiles.add(tableData.getElementsByTag("a").first().attributes().get("href"));
                tableData.remove();
            }
            else
            {
                usableFiles.add(tableData.getElementsByTag("a").first().attributes().get("href"));
            }
        }

        Files.writeString(Path.of(TAB_LIST_FILE_LOCATION), doc.outerHtml());
    }

    private static void removeTabsFromFile()
    {
        File fileToDel;

        for(String tabFileName: deletableFiles)
        {
            fileToDel = new File(ConverterConstants.XLS_TEMP_FOLDER_LOCATION + ConverterConstants.BACKSLASH + tabFileName);
            fileToDel.delete();
        }
    }

    private static void removeEvaluationMarksFromEachFile() throws IOException
    {
        for(String tabFileName: usableFiles)
        {
            File fileToTraverse = new File(ConverterConstants.XLS_TEMP_FOLDER_LOCATION + ConverterConstants.BACKSLASH + tabFileName);

            Document doc = Jsoup.parse(fileToTraverse);

            removeEvaluationFromSheet(doc);

            Files.writeString(Path.of(ConverterConstants.XLS_TEMP_FOLDER_LOCATION + ConverterConstants.BACKSLASH + tabFileName), doc.outerHtml());
        }
    }

    private static void removeEvaluationFromSheet(Document doc)
    {
        Iterator<Element> elementIterator = doc.select("div").iterator();

        while(elementIterator.hasNext())
        {
            Element divElement = elementIterator.next();

            if(removableTexts.contains(divElement.text()))
            {
                divElement.remove();
            }
        }
    }

    private static void replaceDefaultSheet() throws IOException
    {
        Document doc = Jsoup.parse(new File(ConverterConstants.XLS_CONVERTED_ROOT_HTML_FILE_LOCATION));
        Iterator<Element> elementIterator = doc.select("frame").iterator();

        String firstSheet = usableFiles.get(0);
        if(firstSheet != null)
        {
            while(elementIterator.hasNext())
            {
                Element element = elementIterator.next();
                
                if(element.attributes().get("name").equals("frSheet"))
                {
                    element.attributes().put("src", ConverterConstants.XLS_TEMP_FOLDER_NAME + ConverterConstants.BACKSLASH + firstSheet);
                }
            }
        }

        Files.writeString(Path.of(ConverterConstants.XLS_CONVERTED_ROOT_HTML_FILE_LOCATION), doc.outerHtml());
    }

    private static void removeFirstPicture(Document doc)
    {
        Iterator<Element> paragraphElementIterator = doc.select("p").iterator();
        int index = 0;

        while(paragraphElementIterator.hasNext())
        {
            Element paragraphElement = paragraphElementIterator.next();

            if(!paragraphElement.select("img").isEmpty())
            {
                Element imageElement = paragraphElement.select("img").first();
                if(index == 0)
                {
                    paragraphElement.remove();
                }
                imageElement.removeAttr("style");
                index++;
            }
        }
    }

    private static void removeEvaluationMarksFromDoc(Document doc)
    {
        Iterator<Element> spanIterator = doc.select("span").iterator();

        while(spanIterator.hasNext())
        {
            Element element = spanIterator.next();
            if(removableTexts.contains(element.ownText()))
            {
                element.remove();
            }
        }

    }

    private static void removeFromFileList(Document doc) throws IOException
    {
        Iterator<Element> fileListIterator = doc.getElementsByAttribute("HRef").iterator();

        while(fileListIterator.hasNext())
        {
            Element element = fileListIterator.next();

            if(deletableFiles.contains(element.attributes().get("HRef")))
            {
                element.remove();
            }
        }

        Files.writeString(Path.of(FILE_LIST_FILE_LOCATION), doc.outerHtml());
    }

    private static String getBase64OfImage(String fileLocation) throws IOException
    {
        String fileType = FileTypeUtils.getFileType(fileLocation);
        if(fileType.equals("jpg"))
        {
            fileType = "jpeg";
        }
        String getRootSrc = BASE_64_IMAGE_SOURCE_ROOT.get(fileType);

        if(fileType.equals("svg"))
        {
            return getRootSrc + convertSvgToBase64(fileLocation);
        }
        else if(fileType.equals("bmp"))
        {
            return getRootSrc + convertBMPToBase64(fileLocation);
        }
        else if(fileType.equals("gif"))
        {
            return getRootSrc + convertBMPToBase64(fileLocation);
        }

        File file = new File(fileLocation);
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] bytes = new byte[(int) file.length()];
        fileInputStream.read(bytes);
        fileInputStream.close();
        
        return getRootSrc + Base64.getEncoder().encodeToString(bytes);
    }

    public static String convertBMPToBase64(String filePath) {
        try {
            // Read the BMP file into a byte array
            File file = new File(filePath);
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fileInputStream.read(bytes);
            fileInputStream.close();

            // Convert the byte array to a Base64 string
            String base64String = Base64.getEncoder().encodeToString(bytes);
            return base64String;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String convertSvgToBase64(String filePath) {
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }

            byte[] svgBytes = bos.toByteArray();
            byte[] base64Bytes = Base64.getEncoder().encode(svgBytes);
            String svgBase64 = new String(base64Bytes);
            return svgBase64.substring(0, svgBase64.length() - 2);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
