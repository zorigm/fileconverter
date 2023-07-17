package com.mkit.fileconverter.excel;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.format.CellFormat;
import org.apache.poi.ss.format.CellFormatResult;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressBase.CellPosition;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.ResourceUtils;
/**
* This example shows how to display a spreadsheet in HTML using the classes for
* spreadsheet display.
*/
public final class ExcelToHtml {
    private final Workbook wb;
    private final Appendable output;
    private boolean completeHTML;
    private Formatter out;
    private boolean gotBounds;
    private int firstColumn;
    private int endColumn;
    private HtmlHelper helper;
    
    private static final String DEFAULTS_CLASS = "excelDefaults";
    private static final String COL_HEAD_CLASS = "colHeader";
    private static final String ROW_HEAD_CLASS = "rowHeader";
    
    private static final int IDX_TABLE_WIDTH = -2;
    private static final int IDX_HEADER_COL_WIDTH = -1;
    
    /**
    * Creates a new examples to HTML for the given workbook.
    *
    * @param wb     The workbook.
    * @param output Where the HTML output will be written.
    *
    * @return An object for converting the workbook to HTML.
    */
    public static ExcelToHtml create(Workbook wb, Appendable output) {
        return new ExcelToHtml(wb, output);
    }
    
    /**
    * Creates a new examples to HTML for the given workbook.  If the path ends
    * with "{@code .xlsx}" an {@link XSSFWorkbook} will be used; otherwise
    * this will use an {@link HSSFWorkbook}.
    *
    * @param path   The file that has the workbook.
    * @param output Where the HTML output will be written.
    *
    * @return An object for converting the workbook to HTML.
    */
    public static ExcelToHtml create(String path, Appendable output)
    throws IOException {
        return create(new FileInputStream(path), output);
    }
    
    /**
    * Creates a new examples to HTML for the given workbook.  This attempts to
    * detect whether the input is XML (so it should create an {@link
    * XSSFWorkbook} or not (so it should create an {@link HSSFWorkbook}).
    *
    * @param in     The input stream that has the workbook.
    * @param output Where the HTML output will be written.
    *
    * @return An object for converting the workbook to HTML.
    */
    public static ExcelToHtml create(InputStream in, Appendable output)
    throws IOException {
        Workbook wb = WorkbookFactory.create(in);
        return create(wb, output);
    }
    
    private ExcelToHtml(Workbook wb, Appendable output) {
        if (wb == null) {
            throw new NullPointerException("wb");
        }
        if (output == null) {
            throw new NullPointerException("output");
        }
        this.wb = wb;
        this.output = output;
        setupColorMap();
    }
    
    private void setupColorMap() {
        if (wb instanceof HSSFWorkbook) {
            helper = new HSSFHtmlHelper((HSSFWorkbook) wb);
        } else if (wb instanceof XSSFWorkbook) {
            helper = new XSSFHtmlHelper();
        } else {
            throw new IllegalArgumentException(
                    "unknown workbook type: " + wb.getClass().getSimpleName());
        }
    }
    
    /**
    * Run this class as a program
    *
    * @param args The command line arguments.
    *
    * @throws Exception Exception we don't recover from.
    */
    public static void main(String[] args) throws Exception {
        if(args.length < 2){
            System.err.println("usage: ExcelToHtml inputWorkbook outputHtmlFile");
            return;
        }
        
        try (PrintWriter pw = new PrintWriter(args[1], StandardCharsets.UTF_8.name())) {
            ExcelToHtml ExcelToHtml = create(args[0], pw);
            ExcelToHtml.setCompleteHTML(true);
            ExcelToHtml.printPage();
        }
    }
    
    public void setCompleteHTML(boolean completeHTML) {
        this.completeHTML = completeHTML;
    }
    
    public void printPage() throws IOException {
        try {
            ensureOut();
            if (completeHTML) {
                out.format("<!DOCTYPE html>%n");
                out.format("<?xml version=\"1.0\" encoding=\"iso-8859-1\" ?>%n");
                out.format("<html lang=\"en\">%n");
                out.format("<head>%n");
                out.format("<meta charset=\"UTF-8\" />");
                out.format("</head>%n");
                out.format("<body>%n");
            }
            
            print();
            
            if (completeHTML) {
                out.format("</body>%n");
                out.format("</html>%n");
            }
        } finally {
            IOUtils.closeQuietly(out);
            if (output instanceof Closeable) {
                IOUtils.closeQuietly((Closeable) output);
            }
        }
    }
    
    public void print() {
        printInlineStyle();
        printSheets();
        printTabs();
        printScripts();
    }
    
    private void printInlineStyle() {
        out.format("<style type=\"text/css\">%n");
        printStyles();
        out.format("</style>%n");
    }
    
    private void ensureOut() {
        if (out == null) {
            out = new Formatter(output, Locale.ROOT);
        }
    }
    
    public void printStyles() {
        ensureOut();
        
        // First, copy the base css
        try (BufferedReader in = new BufferedReader(new FileReader(ResourceUtils.getFile("classpath:excelStyle.css")))){
            String line;
            while ((line = in.readLine()) != null) {
                out.format("%s%n", line);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Reading standard css", e);
        }
        
        // now add css for each used style
        Set<CellStyle> seen = new HashSet<>();
        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            Sheet sheet = wb.getSheetAt(i);
            Iterator<Row> rows = sheet.rowIterator();
            while (rows.hasNext()) {
                Row row = rows.next();
                for (Cell cell : row) {
                    CellStyle style = cell.getCellStyle();
                    if (!seen.contains(style)) {
                        printStyle(style);
                        seen.add(style);
                    }
                }
            }
        }
    }

    private void printScripts(){
        out.format("<script>%n");   
        try (BufferedReader in = new BufferedReader(new FileReader(ResourceUtils.getFile("classpath:script.js")))){
            String line;
            while ((line = in.readLine()) != null) {
                out.format("%s%n", line);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Reading standard css", e);
        }
        out.format("</script> %n");
    }
    
    private void printStyle(CellStyle style) {
        out.format(".%s .%s {%n", DEFAULTS_CLASS, styleName(style));
        styleContents(style);
        out.format("}%n");
    }
    
    private void styleContents(CellStyle style) {
        styleOut("text-align", style.getAlignment(), StyleHelper.HALIGN);
        styleOut("vertical-align", style.getVerticalAlignment(), StyleHelper.VALIGN);
        fontStyle(style);
        borderStyles(style);
        helper.colorStyles(style, out);
    }
    
    private void borderStyles(CellStyle style) {
        styleOut("border-left", style.getBorderLeft(), StyleHelper.BORDER);
        styleOut("border-right", style.getBorderRight(), StyleHelper.BORDER);
        styleOut("border-top", style.getBorderTop(), StyleHelper.BORDER);
        styleOut("border-bottom", style.getBorderBottom(), StyleHelper.BORDER);
    }
    
    private void fontStyle(CellStyle style) {
        Font font = wb.getFontAt(style.getFontIndex());
        
        if (font.getBold()) {
            out.format("  font-weight: bold;%n");
        }
        if (font.getItalic()) {
            out.format("  font-style: italic;%n");
        }
        
        int fontheight = font.getFontHeightInPoints();
        if (fontheight == 9) {
            //fix for stupid ol Windows
            fontheight = 10;
        }
        out.format("  font-size: %dpt;%n", fontheight);
        
        // Font color is handled with the other colors
    }
    
    private String styleName(CellStyle style) {
        if (style == null) {
            style = wb.getCellStyleAt(0);
        }
        StringBuilder sb = new StringBuilder();
        try (Formatter fmt = new Formatter(sb, Locale.ROOT)) {
            fmt.format("style_%02x", style.getIndex());
            return fmt.toString();
        }
    }

    private String inlineStyle(Font font){
        StringBuilder sb = new StringBuilder();
        try (Formatter fmt = new Formatter(sb, Locale.ROOT)) {
            fmt.format("font-family: %s;",font.getFontName());
            if(font.getBold()){
                fmt.format("font-weight: %s;",font.getBold()); 
            }
            fmt.format("font-size: %spx;",font.getFontHeightInPoints());
            return fmt.toString();
        }
    }
    private String inlineStyle(XSSFFont font){
        StringBuilder sb = new StringBuilder();
        try (Formatter fmt = new Formatter(sb, Locale.ROOT)) {
            fmt.format("font-family: %s;",font.getFontName());
            if(font.getBold()){
                fmt.format("font-weight: %s;",font.getBold()); 
            }
            fmt.format("font-size: %spx;",font.getFontHeightInPoints());
            return fmt.toString();
        }
    }

    private String handleContent(HSSFCell cell) {
        HSSFRichTextString val = cell.getRichStringCellValue();
        int formatrun = val.numFormattingRuns();
        String cont = val.getString();
        StringBuilder sb = new StringBuilder();
        if(formatrun > 0 ){
            try (Formatter fmt = new Formatter(sb, Locale.ROOT)) {
                for (int j = 0; j < formatrun; j++) {
                    int startsat = val.getIndexOfFormattingRun(j);
                    int total = val.length();
                    int length = 0;
                    // 0-5,6-15 total 15
                    // current = 0
                    // next = 6
                    // len =5
                    // current = 6
                    // next = 9
                    if (j+1 < formatrun) {
                        length = val.getIndexOfFormattingRun(j+1)-startsat-1;
                    }else{
                        length = total -startsat;
                    }
                    
                    String splitted = cont.substring(startsat,startsat+length);
                    short fnt = val.getFontAtIndex(startsat);
                    Font font = wb.getFontAt(fnt);
                    String style = font != null? inlineStyle(font):"";
                    fmt.format("<span style=\"%s\">%s</span>",style,splitted);
                }      
                return fmt.toString();          
            }
        }
        return "";
    }
    
    private String handleContent(XSSFCell cell) {
        XSSFRichTextString val = cell.getRichStringCellValue();
        int formatrun = val.numFormattingRuns();
        String cont = val.getString();
        StringBuilder sb = new StringBuilder();
        if(formatrun > 0 ){
            try (Formatter fmt = new Formatter(sb, Locale.ROOT)) {
                for (int j = 0; j < formatrun; j++) {
                    int startsat = val.getIndexOfFormattingRun(j);
                    int length = val.getLengthOfFormattingRun(j);
                    String splitted = cont.substring(startsat,startsat+length);
                    XSSFFont font = val.getFontAtIndex(startsat);
                    String style = font != null? inlineStyle(font):"";
                    fmt.format("<span style=\"%s\">%s</span>",style,splitted);
                }      
                return fmt.toString();          
            }
        }
        return "";
    }

    private <K> void styleOut(String attr, K key, Map<K, String> mapping) {
        String value = mapping.get(key);
        if (value != null) {
            out.format("  %s: %s;%n", attr, value);
        }
    }
    
    private static CellType ultimateCellType(Cell c) {
        CellType type = c.getCellType();
        if (type == CellType.FORMULA) {
            type = c.getCachedFormulaResultType();
        }
        return type;
    }
    
    private void printSheets() {
        ensureOut();
        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            Sheet sheet = wb.getSheetAt(i);
            printSheet(sheet,i);
        }
        
    }

    private void printTabs(){
        ensureOut();
        out.format("<div class=tabs id=tabs>%n");
        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            Sheet sheet = wb.getSheetAt(i);
            printTab(sheet,i);
        }
        out.format("</div>%n");
    }
    
    private void printTab(Sheet sheet,int index){
        ensureOut();
        String name = sheet.getSheetName();
        out.format("<button onClick='showTab(\"table_%s\",event)'>%s</button>%n",index,name);
    }


    public void printSheet(Sheet sheet,int index) {
        ensureOut();
        Map<Integer, Integer> widths = computeWidths(sheet);
        int tableWidth = widths.get(IDX_TABLE_WIDTH);
        out.format("<table class=%s id=table_%s style=\"width:%dpx;display:none\">%n", DEFAULTS_CLASS,index, tableWidth);
        printCols(widths);
        printSheetContent(sheet);
        out.format("</table>%n");
    }
    
    /**
    * computes the column widths, defined by the sheet.
    *
    * @param sheet The sheet for which to compute widths
    * @return Map with key: column index; value: column width in pixels
    *     <br>special keys:
    *     <br>{@link #IDX_HEADER_COL_WIDTH} - width of the header column
    *     <br>{@link #IDX_TABLE_WIDTH} - width of the entire table
    */
    private Map<Integer, Integer> computeWidths(Sheet sheet) {
        Map<Integer, Integer> ret = new TreeMap<>();
        int tableWidth = 0;
        
        ensureColumnBounds(sheet);
        
        // compute width of the header column
        int lastRowNum = sheet.getLastRowNum();
        int headerCharCount = String.valueOf(lastRowNum).length();
        int headerColWidth = widthToPixels((headerCharCount + 1) * 256.0);
        ret.put(IDX_HEADER_COL_WIDTH, headerColWidth);
        tableWidth += headerColWidth;
        
        for (int i = firstColumn; i < endColumn; i++) {
            int colWidth = widthToPixels(sheet.getColumnWidth(i));
            ret.put(i, colWidth);
            tableWidth += colWidth;
        }
        
        ret.put(IDX_TABLE_WIDTH, tableWidth);
        return ret ;
    }
    
    /**
    * Probably platform-specific, but appears to be a close approximation on some systems
    * @param widthUnits POI's native width unit (twips)
    * @return the approximate number of pixels for a typical display
    */
    protected int widthToPixels(final double widthUnits) {
        return Math.toIntExact(Math.round(widthUnits * 9 / 256));
    }
    
    private void printCols(Map<Integer, Integer> widths) {
        int headerColWidth = widths.get(IDX_HEADER_COL_WIDTH);
        out.format("<col style=\"width:%dpx\"/>%n", headerColWidth);
        for (int i = firstColumn; i < endColumn; i++) {
            int colWidth = widths.get(i);
            out.format("<col style=\"width:%dpx;\"/>%n", colWidth);
        }
    }
    
    private void ensureColumnBounds(Sheet sheet) {
        if (gotBounds) {
            return;
        }
        
        Iterator<Row> iter = sheet.rowIterator();
        firstColumn = (iter.hasNext() ? Integer.MAX_VALUE : 0);
        endColumn = 0;
        while (iter.hasNext()) {
            Row row = iter.next();
            short firstCell = row.getFirstCellNum();
            if (firstCell >= 0) {
                firstColumn = Math.min(firstColumn, firstCell);
                endColumn = Math.max(endColumn, row.getLastCellNum());
            }
        }
        gotBounds = true;
    }
    
    private void printColumnHeads() {
        out.format("<thead>%n");
        out.format("  <tr class=%s>%n", COL_HEAD_CLASS);
        out.format("    <th class=%s>&#x25CA;</th>%n", COL_HEAD_CLASS);
        //noinspection UnusedDeclaration
        StringBuilder colName = new StringBuilder();
        for (int i = firstColumn; i < endColumn; i++) {
            colName.setLength(0);
            int cnum = i;
            do {
                colName.insert(0, (char) ('A' + cnum % 26));
                cnum /= 26;
            } while (cnum > 0);
            out.format("    <th class=%s>%s</th>%n", COL_HEAD_CLASS, colName);
        }
        out.format("  </tr>%n");
        out.format("</thead>%n");
    }
    
    private void printSheetContent(Sheet sheet) {
        printColumnHeads();
        
        out.format("<tbody>%n");
        Iterator<Row> rows = sheet.rowIterator();
        while (rows.hasNext()) {
            Row row = rows.next();
            int rowNum = row.getRowNum();
            out.format("  <tr>%n");
            out.format("    <td class=%s>%d</td>%n", ROW_HEAD_CLASS,
            row.getRowNum() + 1);
            for (int i = firstColumn; i < endColumn; i++) {
                String content = "&nbsp;";
                String attrs = "";
                String rowspan = "";
                String cellspan = "";
                CellStyle style = null;
                Map<String,Integer> rowCellSpan = new TreeMap<>();
                if (i >= row.getFirstCellNum() && i < row.getLastCellNum()) {
                    
                    
                    Cell cell = row.getCell(i);
                    if (cell != null) {
                        style = cell.getCellStyle();
                        attrs = tagStyle(cell, style);
                        rowCellSpan = checkMerged(sheet, rowNum, i);
                        //Set the value that is rendered for the cell
                        //also applies the format
                        CellFormat cf = CellFormat.getInstance(style.getDataFormatString());
                        CellFormatResult result = cf.apply(cell);
                        content = result.text; //never null
                        if (content.isEmpty()) {
                            content = "&nbsp;";
                        }
                        if (wb instanceof XSSFWorkbook){
                            XSSFCell xssfCell = (XSSFCell) row.getCell(i);
                            if(xssfCell.getCellType().equals(CellType.STRING)){
                                String subcontent = handleContent(xssfCell);
                                if (!subcontent.isEmpty()) {
                                    content = subcontent;
                                }
                            }
                        }else if(wb instanceof HSSFWorkbook){
                            HSSFCell xssfCell = (HSSFCell) row.getCell(i);
                            if(xssfCell.getCellType().equals(CellType.STRING)){
                                String subcontent = handleContent(xssfCell);
                                if (!subcontent.isEmpty()) {
                                    content = subcontent;
                                }
                            }
                        }
                        

                    }
                }
                if (rowCellSpan.size()>0) { 
                    rowspan = "rowspan="+rowCellSpan.get("rowspan");
                    cellspan = "colspan="+rowCellSpan.get("cellspan");
                    out.format("    <td class=%s %s %s %s>%s</td>%n", styleName(style),attrs,rowspan,cellspan, content);
                }

            }
            out.format("  </tr>%n");
        }
        out.format("</tbody>%n");
    }

    private Map<String,Integer> checkMerged(Sheet sheet,int rownum,int colnum){
        Map<String,Integer> rowcellSpan = new TreeMap<>();
        int rowspan = 1;
        int cellspan = 1;
        for(int i = 0; i < sheet.getNumMergedRegions(); ++i)
        {
            CellRangeAddress range = sheet.getMergedRegion(i);
            if(range.containsColumn(colnum) && range.containsRow(rownum)){
                Set<CellPosition> positions = range.getPosition(rownum, colnum);
                if(positions.containsAll(Arrays.asList(CellPosition.TOP,CellPosition.LEFT))){
                    rowspan = range.getLastRow() - range.getFirstRow()+1;
                    cellspan = range.getLastColumn() - range.getFirstColumn()+1;
                    break;
                }else{
                    return rowcellSpan;
                }
            }
        }
        rowcellSpan.put("rowspan", rowspan);
        rowcellSpan.put("cellspan", cellspan);
        return rowcellSpan;
    }
    
    private String tagStyle(Cell cell, CellStyle style) {
        if (style.getAlignment() == HorizontalAlignment.GENERAL) {
            switch (ultimateCellType(cell)) {
                case STRING:
                return "style=\"text-align: left;\"";
                case BOOLEAN:
                case ERROR:
                return "style=\"text-align: center;\"";
                case NUMERIC:
                default:
                // "right" is the default
                break;
            }
        }
        return "";
    }
}