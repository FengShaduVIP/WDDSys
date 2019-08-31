package com.mars.common.util.encryption;


import lombok.Data;

@Data
public class  EncryptedString {

    public static  String key = "WDD123456789_KEY";//长度为16个字符

    public static  String iv  = "WDD123456789_KEY";//长度为16个字符
}
