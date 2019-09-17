package com.mars.common.util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetUrlPic {

    public static InputStream readInputStream(String picUrl) throws Exception{
        URL url = new URL(picUrl);
        //打开链接
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //设置请求方式为"GET"
        conn.setRequestMethod("GET");
        //超时响应时间为5秒
        conn.setConnectTimeout(5 * 1000);
        InputStream inStream = conn.getInputStream();
        return inStream;
    }

}