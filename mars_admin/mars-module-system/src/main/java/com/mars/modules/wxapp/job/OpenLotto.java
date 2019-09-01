package com.mars.modules.wxapp.job;

import com.mars.modules.wxapp.entity.LottoInfo;
import com.mars.modules.wxapp.entity.LottoPlayer;
import com.mars.modules.wxapp.entity.LottoPrize;
import com.mars.modules.wxapp.service.ILottoInfoService;
import com.mars.modules.wxapp.service.ILottoPlayerService;
import com.mars.modules.wxapp.service.ILottoPrizeService;
import com.mars.modules.wxapp.service.IWxUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class OpenLotto {

    @Resource
    private ILottoInfoService lottoInfoService;

    @Resource
    private ILottoPlayerService playerService;

    @Resource
    private ILottoPrizeService prizeService;

    @Resource
    private IWxUserService wxUserService;

    /**
     * 按时间 自动开奖
     */
    @Scheduled(fixedRate=60*1000*2)
    public void openLottoJob(){
        log.info("-----------------------------》执行 自动开奖 定时任务");
        List<LottoInfo> lottoInfos = lottoInfoService.queryNotOpenLotto(1);
        openLotto(lottoInfos);
        log.info("结束《----------------------------自动开奖 定时任务");
    }

    /**
     * 根据参与人员总数开奖
     */
    @Scheduled(fixedRate=60*1000*5)
    public void openLottoByPlayer(){
        log.info("-----------------------------》执行 人数开奖 定时任务");
        List<LottoInfo> lottoInfos = lottoInfoService.queryNotOpenLotto(2);
        openLotto(lottoInfos);
        log.info("结束《---------------------------- 人数开奖 定时任务");
    }

    private void openLotto(List<LottoInfo> lottoInfos){
        if(lottoInfos!=null){
            for (LottoInfo lottoInfo :lottoInfos){
                log.info("抽奖活动：" +lottoInfo.getNickName() +" 开始开奖。ID:"+lottoInfo.getId());
                //查询所有 参与人员
                List<LottoPlayer> players = playerService.queryListByLottoId(lottoInfo.getId());
                List<LottoPrize> prizes = prizeService.queryPrizesByLottoId(lottoInfo.getId());
                Collections.shuffle(prizes);//把list  数据打乱
                List<String> prizeIds = new ArrayList<>();
                for (LottoPrize prize : prizes){
                    for (int i = 0; i < prize.getNum(); i++) {
                        /**
                         * prizeId = {"1","1","2","3","4","4"}
                         */
                        prizeIds.add(prize.getId());
                    }
                }
                int winnerSumNum = 0;
                for (int i = 0; i < prizeIds.size(); i++) {
                    int playerNum = players.size();
                    while (playerNum>0){
                        int randomNum = (int) (Math.random() * playerNum);
                        LottoPlayer playerUser = players.get(randomNum);
                        playerUser.setPrizeId(prizeIds.get(i));
                        String prizeId = prizeIds.get(i);
                        playerService.savePrizeInfo(playerUser,prizeId);
                        winnerSumNum = winnerSumNum + 1;
                        players.remove(randomNum);
                        break;
                    }
                }
                wxUserService.addSumWinnerNum(lottoInfo.getWxNo(),winnerSumNum);
                lottoInfo.setIsEnd(1);
                lottoInfoService.updateById(lottoInfo);
                log.info("抽奖活动：" +lottoInfo.getNickName() +" 开奖结束 ");
            }
        }
    }
}
