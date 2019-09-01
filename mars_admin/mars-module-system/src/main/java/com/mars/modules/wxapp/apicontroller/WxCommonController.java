package com.mars.modules.wxapp.apicontroller;


import com.alibaba.fastjson.JSONObject;
import com.mars.common.api.vo.Result;
import com.mars.common.constant.CommonConstant;
import com.mars.common.system.vo.LoginUser;
import com.mars.common.util.encryption.AesEncryptUtil;
import com.mars.modules.system.controller.CommentController;
import com.mars.modules.system.service.impl.SysBaseApiImpl;
import com.mars.modules.wxapp.entity.WxUser;
import com.mars.modules.wxapp.service.ICommonService;
import com.mars.modules.wxapp.service.ILottoPlayerService;
import com.mars.modules.wxapp.service.IWxUserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
            if(newObj==null){
                wxUser.setOpenId(sysUser.getUsername());
                wxUser.setLevel(CommonConstant.WXAPP_USER_LEVEL_0);
                wxUserService.saveOrUpdate(wxUser);
                sysBaseApi.addLog(loginUser.getUsername()+"添加微信用户信息成功",CommonConstant.LOG_TYPE_2,0);
                result.setResult(newObj);
                result.success("添加成功！");
            }else{
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
