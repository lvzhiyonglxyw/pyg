package com.pinyougou.content.service.impl;

import java.util.List;

import com.pyg_dao.mapper.TbContentMapper;
import com.pyg_pojo.entity.PageResult;
import com.pyg_pojo.pojo.TbContent;
import com.pyg_pojo.pojo.TbContentExample;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.content.service.ContentService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
@Transactional
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper contentMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 查询全部
     */
    @Override
    public List<TbContent> findAll() {
        return contentMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbContent> page = (Page<TbContent>) contentMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(TbContent content) {
        contentMapper.insert(content);
        //根据广告分类id清空缓存中的数据
        redisTemplate.boundHashOps("content").delete(content.getCategoryId());
    }


    /**
     *
     * 根据商品分类id清空，如果前台页面中的商品分类id改变后，缓存中没有此id所以无法清空数据
     *
     * 所以需要判断页面的广告分类id和数据库中的广告分类id是否相同，
     * 如果相同就根据页面的广告分类id进行清空缓存，
     * 如果不相同就根据数据库中的广告分类id清空缓存
     * 修改
     */
    @Override
    public void update(TbContent content) {
        //根据页面修改的id进行查询
        TbContent tbContent = contentMapper.selectByPrimaryKey(content.getId());
        //判断：如果页面修改的数据广告分类id和数据库中的广告分类id相同
        if (tbContent.getCategoryId()==content.getCategoryId()){
            //相等那么就根据页面的广告分类id清空
            //根据广告分类id清空缓存中的数据
            redisTemplate.boundHashOps("content").delete(content.getCategoryId());
        }
        //不相等那么就根据数据库的广告分类id清空
        //根据广告分类id清空缓存中的数据
        redisTemplate.boundHashOps("content").delete(tbContent.getCategoryId());
        contentMapper.updateByPrimaryKey(content);
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbContent findOne(Long id) {
        return contentMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            //先根据主键id查询
            TbContent tbContent = contentMapper.selectByPrimaryKey(id);
            //删除广告数据
            contentMapper.deleteByPrimaryKey(id);
            //根据广告分类id清空缓存中的数据
            redisTemplate.boundHashOps("content").delete(tbContent.getCategoryId());
        }
    }


    @Override
    public PageResult findPage(TbContent content, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();

        if (content != null) {
            if (content.getTitle() != null && content.getTitle().length() > 0) {
                criteria.andTitleLike("%" + content.getTitle() + "%");
            }
            if (content.getUrl() != null && content.getUrl().length() > 0) {
                criteria.andUrlLike("%" + content.getUrl() + "%");
            }
            if (content.getPic() != null && content.getPic().length() > 0) {
                criteria.andPicLike("%" + content.getPic() + "%");
            }
            if (content.getStatus() != null && content.getStatus().length() > 0) {
                criteria.andStatusLike("%" + content.getStatus() + "%");
            }

        }

        Page<TbContent> page = (Page<TbContent>) contentMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 根据广告分类id查询广告
     *
     * @param id 广告分类id
     * @return
     */
    @Override
    public List<TbContent> categoryIdFindAll(Long id) {
        //根据广告分类id获取redis中的数据
        List<TbContent> content = (List<TbContent>) redisTemplate.boundHashOps("content").get(id);
        //判断redis中的数据是否为空
        if (content == null) {
            //为空从数据库中查询存入到redis中
            TbContentExample example = new TbContentExample();
            TbContentExample.Criteria criteria = example.createCriteria();
            //根据分类id查询
            criteria.andCategoryIdEqualTo(id);
            //排序,倒叙
            example.setOrderByClause("sort_order desc");
            //查询广告状态为有效的数据
            criteria.andStatusEqualTo("1");
            content=contentMapper.selectByExample(example);
            redisTemplate.boundHashOps("content").put(id,content);
            System.out.println("数据库中取值");
        } else {
            //直接从redis中取值
            System.out.println("redis中取值");
        }
        return content;
    }

}
