package com.hssy.hssy.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;



@SuppressWarnings("unused")
public class HttpUtils {
    /**
     * 获得服务器的数据
     * @param url
     * @return
     */
    public static String connect(URL url){
        InputStream inputStream=null;
        HttpURLConnection connection=null;
        StringBuffer sb=null;
        try {
            connection=(HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3000);
            connection.setRequestMethod("GET");
//设置请求头
//            connection.setRequestProperty("referer", "https://jstby.e-chinalife.com");
           /* connection.setRequestProperty("referer", "https://www.wuranyubao.cn/wryb_prev.php?movie=no");*/

            connection.setDoOutput(true);
            connection.setDoInput(true);
            if(connection.getResponseCode()==200){
                inputStream=connection.getInputStream();
//对应的字符编码转换
                Reader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(reader);
                String str = null;
                sb = new StringBuffer();
                while ((str = bufferedReader.readLine()) != null) {
                    sb.append(str);
                }

            }
        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            if(inputStream!=null){
                try {
                    inputStream.close();
                    inputStream=null;
                } catch (IOException e) {
// TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
            if(connection!=null){
                connection.disconnect();
                connection=null;
            }

        }

        return new String(sb);
    }
}