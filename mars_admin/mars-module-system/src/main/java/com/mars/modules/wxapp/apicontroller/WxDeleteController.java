package com.mars.modules.wxapp.apicontroller;

import com.alibaba.fastjson.JSONObject;
import com.mars.common.api.vo.Result;
import com.mars.modules.system.controller.CommentController;
import com.mars.modules.wxapp.service.ILottoInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("wxapi/delete")
public class WxDeleteController extends CommentController {

    @Resource
    private ILottoInfoService lottoInfoService;


    /**
     * 根据ID 删除 抽奖活动
     * @param params
     * @return
     */
    @PostMapping("lotto")
    public Result removeLottoById(@RequestBody JSONObject params){
        try {
            String lottoId = params.getString("id");
            if(lottoId.equals("")){
                return Result.error("活动ID 不能为空");
            }
            boolean flag = lottoInfoService.deleteLottoById(lottoId);
            if(flag){
                return Result.ok("删除成功");
            }else {
                return Result.error("删除失败");
            }
        }catch (Exception e){
            log.info("删除抽奖活动失败");
            log.error(e.toString());
            e.printStackTrace();
            return Result.error("服务器出错");
        }
    }



}
