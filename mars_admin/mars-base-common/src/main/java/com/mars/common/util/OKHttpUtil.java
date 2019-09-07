package com.mars.common.util;

import java.io.*;

import com.alibaba.fastjson.JSONObject;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 利用okhttp进行get和post的访问
 *
 * @author cp
 *
 */
public class OKHttpUtil {

    /**
     * get请求
     * @param url
     * @return
     */
    public static String httpGet(String url) {
        String result = null;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            result = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * post请求
     * @param url
     * @param data  提交的参数为key=value&key1=value1的形式
     */
    public static byte[] httpPost(String url, JSONObject data) {
        try {
            byte result[] = null;
            OkHttpClient httpClient = new OkHttpClient();
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), data.toJSONString());
            Request request = new Request.Builder().url(url).post(requestBody).build();
            try {
                Response response = httpClient.newCall(request).execute();
                result = response.body().bytes();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=25_hgqWO4UNfWDYxCQixWJ5TxmOe7gHJpKUZsuSrb1xiCLYm-gs2aRBqfb9uE1nkfYg41jR930SMT9mZmiVwh2qjtcpYyKuDbX-enuUmH2_kJFQv4cvBKzZnTqCbNE0HvLajs9CREHLEbAVJFK8GFVfAFAKJZ";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("page","pages/index/index");
        jsonObject.put("scene","sssss");
        jsonObject.put("width",300);
        jsonObject.put("is_hyaline",false);
        byte response[] = OKHttpUtil.httpPost(url,jsonObject);
        InputStream is = new BufferedInputStream(new ByteArrayInputStream(response));
        QiniuCloudUtil.upload("wdd_01",is,"demo1.png");
    }
}