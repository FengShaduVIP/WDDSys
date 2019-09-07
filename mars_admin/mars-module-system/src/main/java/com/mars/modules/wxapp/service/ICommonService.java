package com.mars.modules.wxapp.service;

import com.mars.common.api.vo.Result;

public interface ICommonService {

    String getQiniuCloudUploadToken(String bucketNm);

    String getXAccessToken(String token);

    String getWxAccessToken();

    Result getWxQrCodeUrl(String bucketNm, String imgName, String page, String scene, String width, boolean is_hyaline);
}
