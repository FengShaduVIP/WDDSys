package com.mars.modules.wxapp.apicontroller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mars.common.api.vo.Result;
import com.mars.modules.system.controller.CommentController;
import com.mars.modules.wxapp.service.ILottoInfoService;
import com.mars.modules.wxapp.service.ILottoPlayerService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 微信小程序首页接口
 * @Author baomihua
 * @Date 2019-07-17
 */
@Slf4j
@Api(tags="微信小程序首页活动")
@RestController
@RequestMapping("/wxapi/index")
public class WxIndexController extends CommentController {

    @Resource
    private ILottoPlayerService playerService;
    @Resource
    private ILottoInfoService lottoInfoService;

    @PostMapping("joinLotto")
    public Result queryUserLottos(@RequestBody JSONObject params){
        try {
            String wxNo = getLoginUserName();
            String lottoId = params.getString("id");
            if(StringUtils.isEmpty(lottoId)){
                return Result.error("活动ID不能为空");
            }
            boolean flag = lottoInfoService.joinLotto(wxNo,lottoId);
            if(flag){
                return Result.ok("参与成功");
            }else{
                return Result.error("参与失败，请联系客服");
            }
        }catch (Exception e){
            e.printStackTrace();
            log.info("查询个人创建活动历史失败");
            log.error(e.toString());
            return Result.error("服务器查询失败");
        }
    }


}
