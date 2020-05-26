package com.mars.modules.wxapp.apicontroller;


import com.alibaba.fastjson.JSONObject;
import com.mars.common.api.vo.Result;
import com.mars.common.constant.CommonConstant;
import com.mars.common.system.vo.LoginUser;
import com.mars.common.util.DateUtils;
import com.mars.common.util.GetUrlPic;
import com.mars.common.util.QiniuCloudUtil;
import com.mars.common.util.encryption.AesEncryptUtil;
import com.mars.modules.system.controller.CommentController;
import com.mars.modules.system.service.impl.SysBaseApiImpl;
import com.mars.modules.wxapp.entity.WxUser;
import com.mars.modules.wxapp.service.ICommonService;
import com.mars.modules.wxapp.service.ILottoPlayerService;
import com.mars.modules.wxapp.service.IWxFormIdService;
import com.mars.modules.wxapp.service.IWxUserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信小程序公共 controller
 */
@Slf4j
@Api(tags="微信小程序调用公共接口")
@RestController
@RequestMapping("wxapi/common")
public class WxCommonController extends CommentController {

    @Resource
    private IWxUserService wxUserService;
    @Resource
    private ILottoPlayerService playerService;
    @Resource
    private SysBaseApiImpl sysBaseApi;
    @Resource
    private ICommonService commonService;
    @Resource
    private IWxFormIdService formIdService;


    /**
     * 获取微信小程序 accessToken
     * @return
     */
    @PostMapping("getWxAccessToken")
    @ResponseBody
    public Result getWxAccessToken(){
        try {
            String accessToken = commonService.getWxAccessToken();
            Map<String,Object> resultMap = new HashMap<>();
            resultMap.put("accessToken",accessToken);
            return Result.ok(resultMap);
        }catch (Exception e){
            e.printStackTrace();
            log.error("获取微信小程序 accessToken信息失败--->"+e.getMessage());
            return Result.error("服务器出错，");
        }
    }

    /**
     * 获取微信小程序 二维码图片地址
     * @return
     */
    @PostMapping("getWxQrCodeUrl")
    @ResponseBody
    public Result getWxQrCodeUrl(@RequestBody JSONObject params){
        try {
            String page = params.getString("page");
            String scene = params.getString("scene");
            String width = params.getString("width");
            String imgName = params.getString("imgName");
            boolean is_hyaline = params.getBoolean("is_hyaline");
            return commonService.getWxQrCodeUrl("wdd_01",imgName,page,scene,width,is_hyaline);
        }catch (Exception e){
            e.printStackTrace();
            log.error("获取微信小程序 accessToken信息失败--->"+e.getMessage());
            return Result.error("服务器出错，");
        }
    }


    /**
     * 微信小程序 上传文件后记录日志
     * @return
     */
    @ResponseBody
    @PostMapping("getWxQRcodeData")
    public Result getWxQRcodeData(@RequestBody JSONObject params){
        try {
            String accessToken = commonService.getWxAccessToken();
            String cardId = params.getString("id");
            //String scene = AesEncryptUtil.encrypt(cardId);
            Map<String,Object> resultMap = new HashMap<>();
            resultMap.put("scene",cardId);
            resultMap.put("cardInfo",playerService.getById(cardId));
            resultMap.put("accessToken",accessToken);
            return Result.ok(resultMap);
        }catch (Exception e){
            e.printStackTrace();
            log.error("获取卡券二维码信息失败--->"+e.getMessage());
            return Result.error("服务器出错，");
        }
    }


    /**
     * 微信小程序 上传文件后记录日志
     * @return
     */
    @ResponseBody
    @PostMapping("uploadLog")
    public Result saveUplodLog(@RequestBody JSONObject jsonObject){
        try {
            String fileUrl = jsonObject.getString("fileUrl");
            String msg = jsonObject.getString("msg");
            String fileType = jsonObject.getString("fileType");
            sysBaseApi.addLog("微信小程序上传文件 "+msg+" fileUrl:"+fileUrl+", fileType:"+fileType,CommonConstant.LOG_TYPE_2,0);
            return Result.ok("保存成功");
        }catch (Exception e){
            log.error("记录微信小程序上传文件日志出错--->"+e.getMessage());
            return Result.error("服务器出错，");
        }
    }

    /**
     * 微信小程序 上传文件后记录日志
     * @return
     */
    @ResponseBody
    @PostMapping("saveFormID")
    public Result saveFormID(@RequestBody JSONObject jsonObject){
        try {
            String formId = jsonObject.getString("formId");
            if(StringUtils.isEmpty(formId)||formId.indexOf("mock")>0){
                return Result.error("formid不能为空");
            }
            String wxNo = getLoginUserName();
            int count = formIdService.saveFormId(wxNo,formId);
            if(count>0){
                return Result.ok("保存成功");
            }
            return Result.error("保存formId失败");
        }catch (Exception e){
            log.error("保存formId失败--->"+e.getMessage());
            return Result.error("服务器出错，");
        }
    }



    /**
     * 添加或者更新微信小程序用户信息
     * @param wxUser
     * @return
     */
    @PostMapping(value = "/updateInfo")
    public Result<WxUser> saveOrUpdate(@RequestBody WxUser wxUser) {
        LoginUser loginUser = getLoginUser();
        Result<WxUser> result = new Result<WxUser>();
        try {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            WxUser newObj = wxUserService.getUserByWxNo(sysUser.getUsername());
            String avatarUrl = wxUser.getAvatarUrl();
            if(newObj==null){
                wxUser.setLevel(CommonConstant.WXAPP_USER_LEVEL_0);
                String fileName = "wxapp/wxUser/avatarUrl/"+ DateUtils.getMillis()+".png";
                String resultUrl =  QiniuCloudUtil.uploadQrCode("wdd_01", GetUrlPic.readInputStream(avatarUrl),fileName);
                wxUser.setAvatarUrl("https://img.cdn.sweetcat.wang/"+resultUrl);
                wxUser.setRegion("[\"北京市\",\"北京市\",\"东城区\"]");
                wxUserService.saveOrUpdate(wxUser);
                sysBaseApi.addLog(loginUser.getUsername()+"添加微信用户信息成功",CommonConstant.LOG_TYPE_2,0);
                result.setResult(newObj);
                result.success("添加成功！");
            }else{
                if(StringUtils.isEmpty(newObj.getRegion())){
                    wxUser.setRegion("[\"北京市\",\"北京市\",\"东城区\"]");
                }
                String fileName = "wxapp/wxUser/avatarUrl/"+newObj.getWxNo()+".png";
                String resultUrl =  QiniuCloudUtil.uploadQrCode("wdd_01", GetUrlPic.readInputStream(avatarUrl),fileName);
                wxUser.setAvatarUrl("https://img.cdn.sweetcat.wang/"+resultUrl);
                wxUser.setId(newObj.getId());
                wxUserService.updateById(wxUser);
                sysBaseApi.addLog(loginUser.getUsername()+" 更新微信用户信息成功",CommonConstant.LOG_TYPE_2,0);
                result.setResult(newObj);
                result.success("更新成功！");
            }
        } catch (Exception e) {
            sysBaseApi.addLog(loginUser.getUsername()+"更新微信用户信息失败",CommonConstant.LOG_TYPE_2,0);
            log.error("------->"+e.toString());
            log.info("------->"+loginUser.getUsername()+"更新微信用户信息失败");
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 核销卡券
     * @param jsonObject
     * @return
     */
    @ResponseBody
    @PostMapping("confirmCard")
    public Result confirmCard(@RequestBody JSONObject jsonObject){
        try {
            String wxNo = getLoginUserName();
            String id = jsonObject.getString("id");
            if(StringUtils.isEmpty(id)){
                return Result.error("请稍后重试");
            }
            String msg = playerService.confirmCard(id,wxNo);
            return Result.ok(msg);
        }catch (Exception e){
            log.error("核销卡券出错"+e.getMessage());
            return Result.error("服务器出错，请联系客服");
        }
    }


    /**
     * 生成文件 活动参与人员记录 以及中奖信息
     * @param cardId 活动ID
     * @param fileType 生成文件类型 1 参与人员记录 以及 中奖用户 2 购买用户信息
     * @return
     */
    @GetMapping("createFile")
    @ResponseBody
    public Result exportCardPlayersInfo(@RequestParam String cardId,@RequestParam String fileType){

        if(StringUtils.isEmpty(cardId)){
            return Result.error("活动ID 不能为空");
        }
        if(StringUtils.isEmpty(fileType)){
            return Result.error("文件类型不能为空");
        }

        return Result.ok();
    }

    //TODO 导出用户购买信息




}
