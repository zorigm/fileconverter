package com.mkit.fileconverter.converter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ConverterFactory {
    static Map<String, Converter> converters = new HashMap<>();

    static {
        converters.put("pdf", new PdfConverter());

        DocConverter docConverter = new DocConverter();
        converters.put("doc", docConverter);
        converters.put("docx", docConverter);

        converters.put("hwp", new HwpConverter());

        XlsConverter xlsConverter = new XlsConverter();
        converters.put("xls", xlsConverter);
        converters.put("xlsx", xlsConverter);
    }

    public static Optional<Converter> getConverter(String fileType)
    {
        return Optional.ofNullable(converters.get(fileType));
    }
}
