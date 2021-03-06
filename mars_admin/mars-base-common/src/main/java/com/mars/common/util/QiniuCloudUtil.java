package com.mars.common.util;


import com.alibaba.fastjson.JSON;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
public class QiniuCloudUtil {

    private static final String accessKey = "BjC1LcBda5nIsd-f8jLjqNwhanJLjQJB--UM9jxL";
    private static final  String secretKey = "RzeLz9Dgpb4I_oGj5K5UMr_vxS9kjyvMuUYxbO6q";

    public static void main(String[] args) throws Exception {
        File file = new File("C:\\Users\\BaoMi花\\Pictures\\h5award.png");

        String bucketNm = "wdd_01";
        //通过文件来传递
        upload(bucketNm,file);

        //通过文件流来上传文件
        //InputStream in = new FileInputStream(file);
        //upload(bucketNm,in,"fdafaf.gif");

        //删除bucket
//      delete(bucketNm, "fdafaf.gif");

        //获取文件信息
        //String [] files = {"Fg2KGXu0vLjTQhGuOZhWIxWgVhy4"};
        //deletes(bucketNm,files);

        //获取文件信息
        //getBucketsInfo();

        //getFileInfo(bucketNm);
    }

    /**
     * 获取bucket里面所有文件的信息
     * @param bucketNm
     */
    public static void getFileInfo(String bucketNm) {
        try {
            BucketManager bucketManager = getBucketManager();

            //文件名前缀
            String prefix = "";
            //每次迭代的长度限制，最大1000，推荐值 1000
            int limit = 1000;
            //指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
            String delimiter = "";

            //列举空间文件列表
            BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(bucketNm, prefix, limit, delimiter);
            while (fileListIterator.hasNext()) {
                //处理获取的file list结果
                FileInfo[] items = fileListIterator.next();
                for (FileInfo item : items) {
                    System.out.println(item.key);
                    /*System.out.println(item.hash);
                    System.out.println(item.fsize);
                    System.out.println(item.mimeType);
                    System.out.println(item.putTime);
                    System.out.println(item.endUser);*/
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取所有的bucket
     */
    public static void getBucketsInfo() {
        try {
            BucketManager bucketManager = getBucketManager();
            //获取所有的bucket信息
            String[]  bucketNms = bucketManager.buckets();
            for(String bucket:bucketNms) {
                System.out.println(bucket);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 删除多个文件
     * @param bucketNm bucket的名称
     * @param keys     文件名称数组
     * //单次批量请求的文件数量不得超过1000 , 这个是七牛所规定的
     * @return
     */
    public static Result deletes(String bucketNm ,String [] keys) {
        Result result = null;
        try {
            //当文件大于1000的时候，就直接不处理
            if(keys.length >1000) {
                return new Result(false);
            }

            //设定删除的数据
            BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
            batchOperations.addDeleteOp(bucketNm, keys);
            BucketManager bucketManager = getBucketManager();
            //发送请求
            Response response = bucketManager.batch(batchOperations);

            //返回数据
            BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);
            for (int i = 0; i < keys.length; i++) {
                BatchStatus status = batchStatusList[i];
                String key = keys[i];
                System.out.print(key + "\t");
                if (status.code == 200) {
                    System.out.println("delete success");
                } else {
                    System.out.println(status.data.error);
                    return new Result(false);
                }
            }
            result = new Result(true);
        }catch (Exception e) {
            result = new Result(false);
        }
        return result;
    }
    /**
     * 删除bucket中的文件名称
     * @param bucketNm bucker名称
     * @param key 文件名称
     * @return
     */
    public static Result delete(String bucketNm ,String key) {
        Result result = null;
        try {
            BucketManager mg = getBucketManager();
            mg.delete(bucketNm, key);
            result = new Result(true);
        }catch (Exception e) {
            result = new Result(false);
        }
        return result;
    }

    /**
     * 上传输入流
     * @param bucketNm  bucket的名称
     * @param in        输入流
     * @return
     */
    public static Result upload(String bucketNm,InputStream in,String key) {
        Result result = null;
        try {
            UploadManager uploadManager = getUploadManager(bucketNm);

            //获取token
            String token = getToken(bucketNm);

            //上传输入流
            Response response = uploadManager.put(in,key,token, null,null);

            //解析上传成功的结果
            DefaultPutRet putRet = JSON.toJavaObject(JSON.parseObject(response.bodyString()), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);

            result = new Result(true,putRet.key);
        }catch (Exception e) {
            result = new Result(false);
        }
        return result;
    }
    /**
     * 上传输入流
     * @param bucketNm  bucket的名称
     * @param in        输入流
     * @return
     */
    public static String uploadQrCode(String bucketNm,InputStream in,String key) {
        try {
            UploadManager uploadManager = getUploadManager(bucketNm);
            //获取token
            String token = getToken(bucketNm);
            //上传输入流
            Response response = uploadManager.put(in,key,token, null,null);
            //解析上传成功的结果
            DefaultPutRet putRet = JSON.toJavaObject(JSON.parseObject(response.bodyString()), DefaultPutRet.class);
            log.info("返回上图片key::"+putRet.key);
            return putRet.key;
        }catch (Exception e) {
            log.error("七牛云 上传微信小程序二维码失败");
            return null;
        }
    }
    /**
     * 通过文件来传递数据
     * @param bucketNm
     * @param file
     * @return
     */
    public static Result upload(String bucketNm,File file) {
        Result result = null;
        try {

            UploadManager uploadManager = getUploadManager(bucketNm);

            String token = getToken(bucketNm);
            Response response = uploadManager.put(file.getAbsolutePath(),newName(file.getName()), token);

            //解析上传成功的结果
            DefaultPutRet putRet = JSON.toJavaObject(JSON.parseObject(response.bodyString()), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);

            result = new Result(true,putRet.key);
        } catch (QiniuException e) {
            e.printStackTrace();
            result = new Result(false);
        }
        return result;

    }

    /**
     * 通过老文件的名称自动生成新的文件
     *
     * @param oldName
     * @return
     */
    public static String newName(String oldName) {
        String[] datas = oldName.split("\\.");
        String type = datas[datas.length - 1];
        String newName = UUID.randomUUID().toString() + "." + type;
        return newName;
    }

    /**
     * 获取上传管理器
     * @param bucketNm
     * @return
     */
    public static UploadManager getUploadManager(String bucketNm) {
        //构造一个带指定Zone对象的配置类
        //区域要和自己的bucket对上，不然就上传不成功
        //华东    Zone.zone0()
        //华北    Zone.zone1()
        //华南    Zone.zone2()
        //北美    Zone.zoneNa0()
        Configuration cfg = new Configuration(Zone.zone0());
        UploadManager uploadManager = new UploadManager(cfg);
        return uploadManager;
    }
    /**
     * 获取Bucket的管理对象
     * @return
     */
    public static BucketManager getBucketManager() {
        //构造一个带指定Zone对象的配置类
        //区域要和自己的bucket对上，不然就上传不成功
        //华东    Zone.zone0()
        //华北    Zone.zone1()
        //华南    Zone.zone2()
        //北美    Zone.zoneNa0()
        Configuration cfg = new Configuration(Zone.zone0());
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        return bucketManager;
    }

    /**
     * 获取七牛云的上传Token
     * @param bucketNm
     * @return
     */
    public static String getToken(String bucketNm) {
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucketNm);
        //log.info("bucketNm:{},获取新的uptoken:{}",bucketNm,upToken);
        return upToken;
    }

    static class Result{
        private String url;
        private boolean error;

        public Result(boolean error) {
            super();
            this.error = error;
        }

        public Result( boolean error,String url) {
            super();
            this.url = url;
            this.error = error;
        }
        public String getUrl() {
            return url;
        }
        public void setUrl(String url) {
            this.url = url;
        }
        public boolean isError() {
            return error;
        }
        public void setError(boolean error) {
            this.error = error;
        }
    }
}

