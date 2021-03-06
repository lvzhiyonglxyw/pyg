package com.pyg_dao.mapper;

import com.pyg_pojo.pojo.TbCities;
import com.pyg_pojo.pojo.TbCitiesExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbCitiesMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_cities
     *
     * @mbggenerated Sat Mar 02 15:32:51 CST 2019
     */
    /*数据总个数*/
    int countByExample(TbCitiesExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_cities
     *
     * @mbggenerated Sat Mar 02 15:32:51 CST 2019
     */
    /*删除*/
    int deleteByExample(TbCitiesExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_cities
     *
     * @mbggenerated Sat Mar 02 15:32:51 CST 2019
     */
    /*根据主键删除*/
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_cities
     *
     * @mbggenerated Sat Mar 02 15:32:51 CST 2019
     */

    int insert(TbCities record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_cities
     *
     * @mbggenerated Sat Mar 02 15:32:51 CST 2019
     */
    int insertSelective(TbCities record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_cities
     *
     * @mbggenerated Sat Mar 02 15:32:51 CST 2019
     */
    List<TbCities> selectByExample(TbCitiesExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_cities
     *
     * @mbggenerated Sat Mar 02 15:32:51 CST 2019
     */
    TbCities selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_cities
     *
     * @mbggenerated Sat Mar 02 15:32:51 CST 2019
     */
    int updateByExampleSelective(@Param("record") TbCities record, @Param("example") TbCitiesExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_cities
     *
     * @mbggenerated Sat Mar 02 15:32:51 CST 2019
     */
    int updateByExample(@Param("record") TbCities record, @Param("example") TbCitiesExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_cities
     *
     * @mbggenerated Sat Mar 02 15:32:51 CST 2019
     */
    int updateByPrimaryKeySelective(TbCities record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_cities
     *
     * @mbggenerated Sat Mar 02 15:32:51 CST 2019
     */
    int updateByPrimaryKey(TbCities record);
}