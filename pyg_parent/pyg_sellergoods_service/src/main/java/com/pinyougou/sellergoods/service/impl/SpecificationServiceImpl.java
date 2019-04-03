package com.pinyougou.sellergoods.service.impl;

import java.util.List;
import java.util.Map;

import com.pyg.pyg_sellergoods_interface.SpecificationService;
import com.pyg_dao.mapper.TbSpecificationMapper;
import com.pyg_dao.mapper.TbSpecificationOptionMapper;
import com.pyg_pojo.entity.PageResult;
import com.pyg_pojo.entity.Specification;
import com.pyg_pojo.pojo.TbSpecification;
import com.pyg_pojo.pojo.TbSpecificationExample;
import com.pyg_pojo.pojo.TbSpecificationOption;
import com.pyg_pojo.pojo.TbSpecificationOptionExample;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.transaction.annotation.Transactional;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
@Transactional
public class SpecificationServiceImpl implements SpecificationService {

    @Autowired
    private TbSpecificationMapper specificationMapper;

    @Autowired
    private TbSpecificationOptionMapper tbSpecificationOptionMapper;

    /**
     * 查询全部
     */
    @Override
    public List<TbSpecification> findAll() {
        return specificationMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbSpecification> page = (Page<TbSpecification>) specificationMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(Specification specification) {
        //添加规格
        specificationMapper.insert(specification.getTbSpecification());
        //添加规格选项
        //获取包装类中的list规格选项
        List<TbSpecificationOption> tbSpecificationOptionList = specification.getTbSpecificationOptionList();
        for (TbSpecificationOption tbSpecificationOption : tbSpecificationOptionList) {
            //获取新添加的规格id并添加到规格选项中
            tbSpecificationOption.setSpecId(specification.getTbSpecification().getId());
            tbSpecificationOptionMapper.insert(tbSpecificationOption);
        }
    }

    /**
     * 修改
     * <p>
     * 先查（回显数据）
     * 再删除(根据规格id删除)
     * 再添加规格选项数据
     */
    @Override
    public void update(Specification specification) {
        //【修改规格】
        //调用包装类中的规格实体类属性
        TbSpecification tbSpecification = specification.getTbSpecification();
        //调用update方法进行根据主键修改规格信息
        specificationMapper.updateByPrimaryKey(tbSpecification);

        //【删除规格选项】
        //创建规格选项Example对象
        TbSpecificationOptionExample tbSpecificationOptionExample = new TbSpecificationOptionExample();
        //添加条件
        TbSpecificationOptionExample.Criteria criteria = tbSpecificationOptionExample.createCriteria();
        //根据id进行删除
        criteria.andSpecIdEqualTo(tbSpecification.getId());
        //调用删除对象方法
        tbSpecificationOptionMapper.deleteByExample(tbSpecificationOptionExample);

        //【添加规格选项】
        //获取包装类中的list规格选项
        List<TbSpecificationOption> tbSpecificationOptionList = specification.getTbSpecificationOptionList();
        for (TbSpecificationOption tbSpecificationOption : tbSpecificationOptionList) {
            //获取新添加的规格id并添加到规格选项中
            tbSpecificationOption.setSpecId(specification.getTbSpecification().getId());
            tbSpecificationOptionMapper.insert(tbSpecificationOption);
        }
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public Specification findOne(Long id) {
        //创建包装类对象
        Specification specification = new Specification();
        //根据id查询规格信息
        TbSpecification tbSpecification = specificationMapper.selectByPrimaryKey(id);
        //创建规格选项对象
        TbSpecificationOptionExample tbSpecificationOptionExample = new TbSpecificationOptionExample();
        //给从表添加查询条件
        TbSpecificationOptionExample.Criteria criteria = tbSpecificationOptionExample.createCriteria();
        //根据规格id查询规格选项
        criteria.andSpecIdEqualTo(tbSpecification.getId());
        List<TbSpecificationOption> tbSpecificationOptions = tbSpecificationOptionMapper.selectByExample(tbSpecificationOptionExample);
        //把规格中查询到的结果存入到包装类中
        specification.setTbSpecification(tbSpecification);
        //把规格选项中查询到的结果存入到包装类中
        specification.setTbSpecificationOptionList(tbSpecificationOptions);
        return specification;
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        TbSpecificationOptionExample example=new TbSpecificationOptionExample();
        TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
        for (Long id : ids) {
            //删除规格
            specificationMapper.deleteByPrimaryKey(id);
            //删除规格选项
            criteria.andSpecIdEqualTo(id);
        }
        tbSpecificationOptionMapper.deleteByExample(example);
    }


    @Override
    public PageResult findPage(TbSpecification specification, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbSpecificationExample example = new TbSpecificationExample();
        TbSpecificationExample.Criteria criteria = example.createCriteria();

        if (specification != null) {
            if (specification.getSpecName() != null && specification.getSpecName().length() > 0) {
                criteria.andSpecNameLike("%" + specification.getSpecName() + "%");
            }

        }

        Page<TbSpecification> page = (Page<TbSpecification>) specificationMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    //查询规格信息
    @Override
    public List<Map> selectOptionList() {

        return specificationMapper.selectOptionList();
    }

}
