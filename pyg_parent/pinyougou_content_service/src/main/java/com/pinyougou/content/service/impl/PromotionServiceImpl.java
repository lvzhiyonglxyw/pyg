package com.pinyougou.content.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.content.service.PromotionService;
import com.pinyougou.utils.IdWorker;
import com.pyg_pojo.entity.PageResult;
import com.pyg_pojo.pojo.TbPromotion;
import com.pyg_pojo.pojo.TbPromotionExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PromotionServiceImpl implements PromotionService {

    @Autowired
    private com.pyg_dao.mapper.TbPromotionMapper tbPromotionMapper;

    @Autowired
    private IdWorker idWorker;

    /**
     * 修改活动内容
     *
     * @param promotion
     */
    @Override
    public void update(TbPromotion promotion) {
        tbPromotionMapper.updateByPrimaryKey(promotion);
    }

    /**
     * 删除活动内容
     *
     * @param ids
     */
    @Override
    public void delete(String[] ids) {
        for (String id : ids) {
            tbPromotionMapper.deleteByPrimaryKey(id);
        }
    }


    /**
     * 条件分页查询
     *
     * @param
     * @return
     */
    @Override
    public PageResult findPage(Integer pageNum, Integer pageSize, TbPromotion promotion) {
        PageHelper.startPage(pageNum, pageSize);

        TbPromotionExample example = new TbPromotionExample();
        TbPromotionExample.Criteria criteria = example.createCriteria();

        if (promotion != null) {
            if (promotion.getId() != null && promotion.getId().length() > 0) {
                criteria.andIdLike("%" + promotion.getId() + "%");
            }
            if (promotion.getActiveScope() != null && promotion.getActiveScope().length() > 0) {
                criteria.andActiveScopeLike("%" + promotion.getActiveScope() + "%");
            }
            if (promotion.getDescription() != null && promotion.getDescription().length() > 0) {
                criteria.andDescriptionLike("%" + promotion.getDescription() + "%");
            }
            if (promotion.getStatus() != null && promotion.getStatus().length() > 0) {
                criteria.andStatusLike("%" + promotion.getStatus() + "%");
            }
            if (promotion.getTitle() != null && promotion.getTitle().length() > 0) {
                criteria.andTitleLike("%" + promotion.getTitle() + "%");
            }
            if (promotion.getTitleImg() != null && promotion.getTitleImg().length() > 0) {
                criteria.andTitleImgLike("%" + promotion.getTitleImg() + "%");
            }

        }

        Page<TbPromotion> page = (Page<TbPromotion>) tbPromotionMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 添加活动信息
     *
     * @param promotion
     */
    @Override
    public void add(TbPromotion promotion) {
        promotion.setId(String.valueOf(idWorker.nextId()));
        tbPromotionMapper.insert(promotion);
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Override
    public TbPromotion findOne(String id) {
        return tbPromotionMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据状态查询
     *
     * @param status
     * @return
     */
    @Override
    public List<TbPromotion> findStatus() {
        TbPromotionExample example = new TbPromotionExample();
        example.createCriteria().andStatusEqualTo("1");
        return tbPromotionMapper.selectByExample(example);
    }
}
