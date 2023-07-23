package com.mkit.fileconverter.excel;
import java.util.Formatter;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

/**
 * Implementation of {@link HtmlHelper} for XSSF files.
 */
public class XSSFHtmlHelper implements HtmlHelper {
    @Override
    public void colorStyles(CellStyle style, Formatter out) {
        XSSFCellStyle cs = (XSSFCellStyle) style;
        styleColor(out, "background-color", cs.getFillForegroundXSSFColor());
        styleColor(out, "color", cs.getFont().getXSSFColor());
    }

    public void styleColor(Formatter out, String attr, XSSFColor color) {
        if (color == null || color.isAuto()) {
            return;
        }
        byte[] rgb = color.getARGB();
        String hex = color.getARGBHex().substring(2);
        // out.format("  %s: #%02x%02x%02x;%n", attr, rgb[1], rgb[2], rgb[3]);
        out.format("  %s: #%s;%n", attr, hex);

    }
}
