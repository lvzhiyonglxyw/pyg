package com.pinyougou.promotion.task;

import com.pyg_dao.mapper.TbPromotionMapper;
import com.pyg_pojo.pojo.TbPromotion;
import com.pyg_pojo.pojo.TbPromotionExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class PromotionTask {

    @Autowired
    private TbPromotionMapper tbPromotionMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    //一分钟执行一次
    @Scheduled(cron = "0/1 * * * * ?")
    public void CronTask() {
        TbPromotionExample example = new TbPromotionExample();
        //查询条件
        //开始时间小于等于当前时间
        example.createCriteria()/*.andStartDateLessThanOrEqualTo(new Date())*/
                //结束时间大于等于当前时间
                .andEndDateLessThanOrEqualTo(new Date())
                //状态等于1
                .andStatusEqualTo("1");

        List<TbPromotion> tbPromotions = tbPromotionMapper.selectByExample(example);
        for (TbPromotion tbPromotion : tbPromotions) {
            System.out.println(tbPromotion.getId());
            tbPromotion.setStatus("0");
            tbPromotionMapper.updateByPrimaryKey(tbPromotion);
        }
    }
}
