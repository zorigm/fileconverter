package com.mkit.fileconverter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.mkit.fileconverter.converter.ConverterConstants;

@SpringBootApplication
public class FileconverterApplication {

	public static void main(String[] args) {
		deleteDirectories();
		createDirectories();
		SpringApplication.run(FileconverterApplication.class, args);
	}

	private static void createDirectories()
	{
		try {
				Files.createDirectory(Paths.get(ConverterConstants.CONVERTER_FOLDER_LOCATION));
				Files.createDirectory(Paths.get(ConverterConstants.TEMP_UPLOADED_FILE_LOCATION));
				Files.createDirectory(Paths.get(ConverterConstants.CONVERTED_EXCEL_LOCATION));
				Files.createDirectory(Paths.get(ConverterConstants.CONVERTED_DOC_FOLDER_LOCATION));
				Files.createDirectory(Paths.get(ConverterConstants.CONVERTED_PDF_FOLDER_LOCATION));
				Files.createDirectory(Paths.get(ConverterConstants.CONVERTED_HWP_FOLDER_LOCATION));
				Files.createDirectory(Paths.get(ConverterConstants.ZIP_FOLDER_LOCATION));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public static void deleteDirectories()
	{
		if(Files.exists(Paths.get(ConverterConstants.CONVERTER_FOLDER_LOCATION)))
		{
			try {
				FileUtils.deleteDirectory(new File(ConverterConstants.CONVERTER_FOLDER_LOCATION));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
