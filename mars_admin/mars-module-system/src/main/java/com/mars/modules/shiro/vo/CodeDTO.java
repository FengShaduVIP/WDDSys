package com.mars.modules.shiro.vo;

import javax.validation.constraints.NotEmpty;

/**
 * Created by baomihua on 2019/4/28.
 */
public class CodeDTO {
    @NotEmpty(message = "缺少参数code或code不合法")
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
