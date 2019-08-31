package com.mars.modules.system.controller;

import com.mars.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;

import java.util.UUID;

public class CommentController {

    /**
     * 获取当前登陆人信息
     * @return
     */
    public static LoginUser getLoginUser(){
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if(sysUser!=null){
            return sysUser;
        }
        return null;
    }

    /**
     * 获取当前登陆人ID
     * @return
     */
    public static String getLoginUserId(){
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if(sysUser!=null){
            return sysUser.getId();
        }
        return null;
    }

    public static String getLoginUserName(){
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if(sysUser!=null){
            return sysUser.getUsername();
        }
        return null;
    }

    /**
     * 获取uuid
     * @return
     */
    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}
