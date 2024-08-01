package com.app.news.utils;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {
    public static void writeNewsJsonToCache(Context context, String title, String newsJson) {
        synchronized (title){
            try {
                File file = new File(context.getFilesDir(), "news_cache_" + title + ".json");
//                System.out.println("-----path:"+file.getAbsoluteFile());
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(newsJson.getBytes());
                fos.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static String readNewsJsonCache(Context context,String title){
        String cacheJson = "";
        File file = new File(context.getFilesDir(), "news_cache_" + title + ".json");

        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[fis.available()];
            int len = fis.read(buffer);
            cacheJson = new String(buffer,0,len);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        System.out.println("----:"+cacheJson);
        return cacheJson;
    }
}
