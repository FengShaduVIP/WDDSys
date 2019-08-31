package com.mars.modules.wxapp.service;

public interface ICommonService {

    String getQiniuCloudUploadToken(String bucketNm);

    String getXAccessToken(String token);

    String getWxAccessToken();
}
