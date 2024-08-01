package com.app.news.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.util.concurrent.Callable;

public class Internet implements Callable<String> {
    private String url;

    public Internet(String url) {
        this.url = url;
    }

    public Internet() {

    }

    @Override
    public String call() throws Exception {

        return getStringData(url);
    }

    public static String getStringData(String url){
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        StringBuilder response = null;
        try{
            URL url1 = new URL(url);
            connection = (HttpURLConnection) url1.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            InputStream in = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in));
            response = new StringBuilder();
            String aLine;
            while ((aLine = reader.readLine()) != null){
                response.append(aLine);
            }
        }catch (SocketException e){
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }finally {
            if(reader != null){
                try{
                    reader.close();//io流关闭
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            if(connection != null){
                connection.disconnect();//关闭http连接
            }
        }

        return response.toString();
    }
}
