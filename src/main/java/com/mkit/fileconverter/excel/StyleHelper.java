package com.mkit.fileconverter.excel;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

public class StyleHelper {
    
    static final Map<HorizontalAlignment, String> HALIGN = mapFor(
        HorizontalAlignment.LEFT, "left",
        HorizontalAlignment.CENTER, "center",
        HorizontalAlignment.RIGHT, "right",
        HorizontalAlignment.FILL, "left",
        HorizontalAlignment.JUSTIFY, "left",
        HorizontalAlignment.CENTER_SELECTION, "center"
    );
    
    static final Map<VerticalAlignment, String> VALIGN = mapFor(
        VerticalAlignment.BOTTOM, "bottom",
        VerticalAlignment.CENTER, "middle",
        VerticalAlignment.TOP, "top"
    );
    
    static final Map<BorderStyle, String> BORDER = mapFor(
        BorderStyle.DASH_DOT, "dashed 1pt",
        BorderStyle.DASH_DOT_DOT, "dashed 1pt",
        BorderStyle.DASHED, "dashed 1pt",
        BorderStyle.DOTTED, "dotted 1pt",
        BorderStyle.DOUBLE, "double 3pt",
        BorderStyle.HAIR, "solid 1px",
        BorderStyle.MEDIUM, "solid 2pt",
        BorderStyle.MEDIUM_DASH_DOT, "dashed 2pt",
        BorderStyle.MEDIUM_DASH_DOT_DOT, "dashed 2pt",
        BorderStyle.MEDIUM_DASHED, "dashed 2pt",
        BorderStyle.NONE, "none",
        BorderStyle.SLANTED_DASH_DOT, "dashed 2pt",
        BorderStyle.THICK, "solid 3pt",
        BorderStyle.THIN, "dashed 1pt"
    );

    @SuppressWarnings({"unchecked"})
    private static <K, V> Map<K, V> mapFor(Object... mapping) {
        Map<K, V> map = new HashMap<>();
        for (int i = 0; i < mapping.length; i += 2) {
            map.put((K) mapping[i], (V) mapping[i + 1]);
        }
        return map;
    }
}
