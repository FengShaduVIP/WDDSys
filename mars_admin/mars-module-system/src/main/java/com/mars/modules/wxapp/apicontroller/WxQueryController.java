package com.mars.modules.wxapp.apicontroller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mars.common.api.vo.Result;
import com.mars.common.util.StringUtils;
import com.mars.modules.system.controller.CommentController;
import com.mars.modules.system.service.impl.SysBaseApiImpl;
import com.mars.modules.wxapp.entity.WxUser;
import com.mars.modules.wxapp.service.ILottoInfoService;
import com.mars.modules.wxapp.service.ILottoPlayerService;
import com.mars.modules.wxapp.service.IWxUserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信小程序首页接口
 * @Author baomihua
 * @Date 2019-07-17
 */
@Slf4j
@Api(tags="微信小程序首页活动")
@RestController
@RequestMapping("/wxapi/query")
public class WxQueryController extends CommentController{

    @Resource
    private SysBaseApiImpl sysBaseApi;

    @Resource
    private ILottoInfoService lottoInfoService;

    @Resource
    private ILottoPlayerService playerService;
    @Resource
    private IWxUserService wxUserService;

    /**
     * 查询 个人信息
     */
    @GetMapping("queryUserInfo")
    @ResponseBody
    public Result queryUserInfo(@RequestParam String wxNo){
        try {
            if(StringUtils.checkIsNull(wxNo)){
                return Result.error("wxNo 参数不能为空");
            }
            return Result.ok(wxUserService.getUserByWxNo(wxNo));
        }catch (Exception e){
            e.printStackTrace();
            return Result.error("查询个人信息失败，服务器出错");
        }
    }

    /**
     * 查询首页抽奖活动
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("queryIndex")
    public Result queryIndex(@RequestParam Integer page,@RequestParam Integer pageSize){
        try {
            IPage<Map<String,Object>> lottoList = lottoInfoService.queryLottoForIndex(page,pageSize);
            return Result.ok(lottoList);
        }catch (Exception e){
            log.info("查询首页活动失败");
            log.error(e.toString());
            return Result.error("服务器查询失败");
        }
    }

    /**
     * 查询个人创建活动历史
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("queryUserLottos")
    public Result queryUserLottos(@RequestParam String wxNo,@RequestParam Integer page,@RequestParam Integer pageSize){
        try {
            if(StringUtils.checkIsNull(wxNo)){
                wxNo = getLoginUserName();
            }
            IPage<Map<String,Object>> lottoList = lottoInfoService.queryUserLottoForIndex(wxNo,page,pageSize);
            return Result.ok(lottoList);
        }catch (Exception e){
            e.printStackTrace();
            log.info("查询个人创建活动历史失败");
            log.error(e.toString());
            return Result.error("服务器查询失败");
        }
    }

    /**
     * 查询抽奖活动详情
     * @param lottoNo 活动id  或  活动编号
     * @return
     */
    @GetMapping("queryLottoDetail")
    public Result queryLottoDetail(@RequestParam String lottoNo){
        try {
            String wxNo = getLoginUserName();
            String userId = getLoginUserId();
            Map<String,Object> lottoDetail = lottoInfoService.queryDetailByLottoNo(lottoNo);
            if(userId.equals(MapUtils.getString(lottoDetail,"createBy"))){
                lottoDetail.put("isOwer",1);
            }else{
                lottoDetail.put("isOwer",0);
            }
            lottoDetail.put("isJoin",playerService.checkIsHaveJoinLotto(wxNo,lottoNo));
            return Result.ok(lottoDetail);
        }catch (Exception e){
            e.printStackTrace();
            log.info("查询: {},活动详情失败",lottoNo);
            log.error(e.toString());
            return Result.error("服务器查询出错");
        }
    }

    /**
     * 查询我的卡包列表内容
     * @param page
     * @param pageSize
     * @param type 1 实体卡片 2 优惠券
     * @return
     */
    @GetMapping("queryMyCard")
    public Result queryMyCard(@RequestParam Integer page,@RequestParam Integer pageSize,@RequestParam Integer type){
        try {
            String userName = getLoginUserName();
            Page<Map<String,Object>> cardList = playerService.queryMyCard(new Page<Object>(page,pageSize),type,userName);
            return Result.ok(cardList);
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            log.info("查询我的卡包列表失败");
            return Result.error("查询失败，服务器出错");
        }
    }

    /**
     * 查询二维码卡券内容
     * @return
     */
    @PostMapping("queryWxCardInfo")
    @ResponseBody
    public Result queryWxCardInfo(@RequestBody JSONObject params){
        try {
            String wxNo = getLoginUserName();
            String scene = params.getString("scene");
            if(StringUtils.checkIsNull(scene)){
                return Result.error("传入参数错误");
            }
            String str[] = scene.split("_");
            if(!wxNo.equals(str[2])){
                return Result.error("请使用活动发起者扫描该卡券");
            }
            Map<String,Object> resultMap = playerService.findPlayerInfo(str[0],str[1]);
            if(resultMap==null){
                return Result.error("该卡券已使用");
            }
            return Result.ok(resultMap);
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            log.info("查询我的卡包列表失败");
            return Result.error("查询失败，服务器出错");
        }
    }

    /**
     * 查询我创建的或者我中奖的 活动列表
     * @param page
     * @param pageSize
     * @param cardType
     * @return
     */
    @GetMapping("queryMyJoinOrWin")
    public Result queryMyJoinOrWin(@RequestParam Integer page,@RequestParam Integer pageSize,@RequestParam Integer cardType){
        try {
            String wxNo = getLoginUserName();
            Page<Map<String,Object>> cardList = playerService.queryMyJoinOrWin(new Page<Object>(page,pageSize),cardType,wxNo);
            return Result.ok(cardList);
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            log.info("查询我中奖的/我参与的列表失败");
            return Result.error("查询失败，服务器出错");
        }
    }

    /**
     * 查询我的地址
     * @return
     */
    @GetMapping("queryMyAddress")
    public Result queryMyAddress(){
        try {
            String wxNo = getLoginUserName();
            WxUser wxUser = wxUserService.getUserByWxNo(wxNo);
            Map<String,String> myAddress = new HashMap<>();
            if(wxUser!=null){
                myAddress.put("name",wxUser.getConsignee());
                myAddress.put("region",wxUser.getRegion());
                myAddress.put("tel",wxUser.getTel());
                myAddress.put("address",wxUser.getDetailAddress());
            }
            return Result.ok(myAddress);
        }catch (Exception e){
            log.info("查询我的地址出错");
            log.error(e.getMessage());
            return Result.error("服务器查询失败");
        }
    }

    /**
     * 检查卡券是否保存地址
     */
    @PostMapping("checkMyCardAddress")
    public Result checkMyCardAddress(@RequestBody JSONObject params){
        try {
            String id = params.getString("id");
            if(StringUtils.checkIsNull(id)){
                return Result.error("卡券无效");
            }
            if(playerService.checkCardHaveAddress(id)){
                return Result.ok();
            }else {
                return Result.error("请先关联奖品收货地址");
            }
        }catch (Exception e){
            log.info("查询我的地址出错");
            log.error(e.getMessage());
            return Result.error("服务器查询失败");
        }
    }

    /**
     * 查询排行统计
     */
    @GetMapping("queryRankList")
    @ResponseBody
    public Result queryRankList(@RequestParam Integer page,@RequestParam Integer pageSize){
        try {
            Page<Map<String,Object>> rankList = wxUserService.queryRankList(new Page<Object>(page,pageSize));
            return Result.ok(rankList);
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            log.info("查询排行榜列表失败");
            return Result.error("查询失败，服务器出错");
        }
    }



}
