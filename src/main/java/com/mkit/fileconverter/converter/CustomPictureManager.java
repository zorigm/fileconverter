package com.mkit.fileconverter.converter;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.util.IOUtils;

public class CustomPictureManager implements PicturesManager {
    @Override
    public String savePicture(byte[] bytes, PictureType pictureType, String s, float v, float v1) {
        File imageFile = new File("imgs", s);
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
}
