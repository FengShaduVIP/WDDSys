package com.mars.modules.wxapp.apicontroller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mars.common.api.vo.Result;
import com.mars.common.util.StringUtils;
import com.mars.common.util.UUIDGenerator;
import com.mars.modules.system.controller.CommentController;
import com.mars.modules.system.service.impl.SysBaseApiImpl;
import com.mars.modules.wxapp.entity.LottoInfo;
import com.mars.modules.wxapp.entity.WxUser;
import com.mars.modules.wxapp.service.*;
import com.mars.modules.wxapp.util.WxDateUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(tags = "微信小程序创建/编辑活动接口")
@RestController
@RequestMapping("/wxapi/create")
public class WxCreateController extends CommentController {
    @Resource
    private SysBaseApiImpl sysBaseApi;

    @Resource
    private IWxUserService wxUserService;

    @Resource
    private ILottoInfoService lottoInfoService;
    @Resource
    private ILottoPlayerService playerService;
    @Resource
    private ILottoPrizeService lottoPrizeService;
    @Resource
    private ILottoContentService contentService;


    /**
     * @param lottoJson
     * @return
     */
    @PostMapping("createNormal")
    public Result createNormal(@RequestBody JSONObject lottoJson) {
        try {
            String wxNo = getLoginUserName();
            LottoInfo lottoInfo = new LottoInfo();
            String lottoId = UUIDGenerator.generate();
            lottoInfo.setId(lottoId);
            Integer openType = lottoJson.getIntValue("openType");
            lottoInfo.setOpenType(openType);
            //赞助名称
            String nickName = lottoJson.getString("nickName");
            lottoInfo.setNickName(nickName);
            if (openType == 2) { //按人数抽奖
                Integer num = lottoJson.getIntValue("requirNum");
                lottoInfo.setOpenNum(num);
            }
            if (openType == 1) { //按时间自动开奖
                String openTime = lottoJson.getString("openTime");
                lottoInfo.setOpenTime(WxDateUtil.wxDateToStrDate(openTime));
            }

            String banner = lottoJson.getString("banner");
            lottoInfo.setBanner(banner);
            //是否口令
            boolean isUseCommand = lottoJson.getBoolean("isUseCommand");
            lottoInfo.setIsCommand(isUseCommand == true ? 1 : 0);
            if (isUseCommand) {
                String command = lottoJson.getString("command");
                lottoInfo.setCommand(command);
            }
            lottoInfo = lottoInfoService.saveLottoInfo(wxNo, lottoInfo);
            JSONArray prizes = lottoJson.getJSONArray("prizes");
            lottoPrizeService.savePrizes(lottoId, lottoInfo.getLottoNo(), prizes);
            JSONArray contents = lottoJson.getJSONArray("contents");
            contentService.saveContents(lottoId, lottoInfo.getLottoNo(), contents);
            Result result = new Result();
            Map<String, String> data = new HashMap<>();
            data.put("lottoId", lottoId);
            result.setResult(data);
            sysBaseApi.addLog(getLoginUserId() + " 创建高级抽奖活动(" + lottoId + ")成功", 2, 0);
            return result;
        } catch (Exception e) {
            log.info("创建高级抽奖活动失败");
            log.error(e.toString());
            sysBaseApi.addLog(getLoginUserId() + " 创建高级抽奖活动失败：" + e.toString(), 2, 0);
            return Result.error("服务器出错");
        }
    }


    /**
     * 保存我的地址
     *
     * @param params
     * @return
     */
    @PostMapping("saveMyAddress")
    @ResponseBody
    public Result saveMyAddress(@RequestBody JSONObject params) {
        try {
            String wxNo = getLoginUserName();
            WxUser wxUser = wxUserService.getUserByWxNo(wxNo);
            if (wxUser == null) {
                return Result.error("请重新登录");
            }
            String tel = params.getString("tel");
            String region = params.getString("region");
            String name = params.getString("name");
            String address = params.getString("address");
            boolean isCard = params.getBoolean("isCard");
            if (StringUtils.checkIsNull(tel) || StringUtils.checkIsNull(region) ||
                    StringUtils.checkIsNull(name) || StringUtils.checkIsNull(address)) {
                return Result.error("有必填项为空，不能保存");
            }
            if(isCard){
                String id = params.getString("cardId");
                JSONArray array = JSON.parseArray(region);
                region = array.getString(0)+" "+array.getString(1)+" "+array.getString(2)+" ";
                playerService.saveCardAddress(id,name,tel,region+address);
            }else {
                wxUser.setConsignee(name);
                wxUser.setTel(tel);
                wxUser.setDetailAddress(address);
                wxUser.setRegion(region);
                wxUserService.updateById(wxUser);
            }
            return Result.ok("保存成功");
        } catch (Exception e) {
            log.info("保存我的地址失败");
            log.error(e.getMessage());
            return Result.error("保存我的地址失败，服务器出错");
        }
    }


}
