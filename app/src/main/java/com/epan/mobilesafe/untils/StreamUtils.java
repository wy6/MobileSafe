package com.epan.mobilesafe.untils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

/**
 * Created by WY on 2017/4/7.
 */

public class StreamUtils {
    /**
     * 把字节流转换成String
     */
    public static String parserStream(InputStream is) throws IOException {
        BufferedReader br=new BufferedReader(new InputStreamReader(is));
        //写入流
        StringWriter sw=new StringWriter();
        String str=null;
        while((str=br.readLine())!=null){
            sw.write(str);
        }

        br.close();
        return sw.toString();

    }
}
