package com.pyg_dao.mapper;

import com.pyg_pojo.pojo.TbFreightTemplate;
import com.pyg_pojo.pojo.TbFreightTemplateExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbFreightTemplateMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_freight_template
     *
     * @mbggenerated Sat Mar 02 15:32:51 CST 2019
     */
    int countByExample(TbFreightTemplateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_freight_template
     *
     * @mbggenerated Sat Mar 02 15:32:51 CST 2019
     */
    int deleteByExample(TbFreightTemplateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_freight_template
     *
     * @mbggenerated Sat Mar 02 15:32:51 CST 2019
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_freight_template
     *
     * @mbggenerated Sat Mar 02 15:32:51 CST 2019
     */
    int insert(TbFreightTemplate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_freight_template
     *
     * @mbggenerated Sat Mar 02 15:32:51 CST 2019
     */
    int insertSelective(TbFreightTemplate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_freight_template
     *
     * @mbggenerated Sat Mar 02 15:32:51 CST 2019
     */
    List<TbFreightTemplate> selectByExample(TbFreightTemplateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_freight_template
     *
     * @mbggenerated Sat Mar 02 15:32:51 CST 2019
     */
    TbFreightTemplate selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_freight_template
     *
     * @mbggenerated Sat Mar 02 15:32:51 CST 2019
     */
    int updateByExampleSelective(@Param("record") TbFreightTemplate record, @Param("example") TbFreightTemplateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_freight_template
     *
     * @mbggenerated Sat Mar 02 15:32:51 CST 2019
     */
    int updateByExample(@Param("record") TbFreightTemplate record, @Param("example") TbFreightTemplateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_freight_template
     *
     * @mbggenerated Sat Mar 02 15:32:51 CST 2019
     */
    int updateByPrimaryKeySelective(TbFreightTemplate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_freight_template
     *
     * @mbggenerated Sat Mar 02 15:32:51 CST 2019
     */
    int updateByPrimaryKey(TbFreightTemplate record);
}