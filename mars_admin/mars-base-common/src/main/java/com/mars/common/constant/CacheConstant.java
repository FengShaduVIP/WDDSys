package com.mars.common.constant;

/**
 * @author: huangxutao
 * @date: 2019-06-14
 * @description: 缓存常量
 */
public interface CacheConstant {

	public static final String X_ACCESS_TOKEN = "X-Access-Token";
	/**
	 * 字典信息缓存
	 */
    public static final String DICT_CACHE = "dictCache";

	/**
	 * 权限信息缓存
	 */
    public static final String PERMISSION_CACHE = "permission";

	/**
	 * 登录用户规则缓存
	 */
    public static final String LOGIN_USER_RULES_CACHE = "loginUser_cacheRules";

	/**
	 * 七牛云 token 缓存
	 */
	public static final String QINIU_UPTOKEN_CACHE = "qiniu_upToken";

	public static final String WXAPP_ACCESS_TOKEN = "access_token";

	public static final String WXAPP_QRCODE_URL = "wxapp_qrcode_url";

}
