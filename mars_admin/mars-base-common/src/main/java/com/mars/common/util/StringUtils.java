package com.mars.common.util;

public class StringUtils {

    /**
     * 检查字符串是否为空
     * @param value
     * @return
     */
    public static boolean checkIsNull(String value){
        if (value==null||value==""||value.length()<1){
            return true;
        }else {
            return false;
        }
    }
}
