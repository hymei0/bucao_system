package com.example.bucao_springboot.common;

import org.apache.poi.poifs.filesystem.FileMagic;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class excelconfig {

    public boolean getFileType(String fileName) {
        String[] strArray = fileName.split("\\.");
        int suffixIndex = strArray.length -1;
        System.out.println(strArray[suffixIndex]);
        if(strArray[suffixIndex].equals("xls")|| strArray[suffixIndex].equals("xlsx")||strArray[suffixIndex].equals("xlsm"))
        {
            return true;
        }else {
            return false;
        }
    }

}
