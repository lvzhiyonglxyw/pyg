package com.pyg_dao.mapper;

import com.pyg_pojo.pojo.TbSpecification;
import com.pyg_pojo.pojo.TbSpecificationExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TbSpecificationMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_specification
     *
     * @mbggenerated Sat Mar 02 15:32:51 CST 2019
     */
    int countByExample(TbSpecificationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_specification
     *
     * @mbggenerated Sat Mar 02 15:32:51 CST 2019
     */
    int deleteByExample(TbSpecificationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_specification
     *
     * @mbggenerated Sat Mar 02 15:32:51 CST 2019
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_specification
     *
     * @mbggenerated Sat Mar 02 15:32:51 CST 2019
     */
    int insert(TbSpecification record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_specification
     *
     * @mbggenerated Sat Mar 02 15:32:51 CST 2019
     */
    int insertSelective(TbSpecification record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_specification
     *
     * @mbggenerated Sat Mar 02 15:32:51 CST 2019
     */
    List<TbSpecification> selectByExample(TbSpecificationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_specification
     *
     * @mbggenerated Sat Mar 02 15:32:51 CST 2019
     */
    TbSpecification selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_specification
     *
     * @mbggenerated Sat Mar 02 15:32:51 CST 2019
     */
    int updateByExampleSelective(@Param("record") TbSpecification record, @Param("example") TbSpecificationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_specification
     *
     * @mbggenerated Sat Mar 02 15:32:51 CST 2019
     */
    int updateByExample(@Param("record") TbSpecification record, @Param("example") TbSpecificationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_specification
     *
     * @mbggenerated Sat Mar 02 15:32:51 CST 2019
     */
    int updateByPrimaryKeySelective(TbSpecification record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_specification
     *
     * @mbggenerated Sat Mar 02 15:32:51 CST 2019
     */
    int updateByPrimaryKey(TbSpecification record);

    //按查询规格
    List<Map> selectOptionList();
}