package com.pyg_dao.mapper;

import com.pyg_pojo.pojo.TbOrder;
import com.pyg_pojo.pojo.TbOrderExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbOrderMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_order
     *
     * @mbggenerated Sat Mar 02 15:32:51 CST 2019
     */
    int countByExample(TbOrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_order
     *
     * @mbggenerated Sat Mar 02 15:32:51 CST 2019
     */
    int deleteByExample(TbOrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_order
     *
     * @mbggenerated Sat Mar 02 15:32:51 CST 2019
     */
    int deleteByPrimaryKey(Long orderId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_order
     *
     * @mbggenerated Sat Mar 02 15:32:51 CST 2019
     */
    int insert(TbOrder record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_order
     *
     * @mbggenerated Sat Mar 02 15:32:51 CST 2019
     */
    int insertSelective(TbOrder record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_order
     *
     * @mbggenerated Sat Mar 02 15:32:51 CST 2019
     */
    List<TbOrder> selectByExample(TbOrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_order
     *
     * @mbggenerated Sat Mar 02 15:32:51 CST 2019
     */
    TbOrder selectByPrimaryKey(Long orderId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_order
     *
     * @mbggenerated Sat Mar 02 15:32:51 CST 2019
     */
    int updateByExampleSelective(@Param("record") TbOrder record, @Param("example") TbOrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_order
     *
     * @mbggenerated Sat Mar 02 15:32:51 CST 2019
     */
    int updateByExample(@Param("record") TbOrder record, @Param("example") TbOrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_order
     *
     * @mbggenerated Sat Mar 02 15:32:51 CST 2019
     */
    int updateByPrimaryKeySelective(TbOrder record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_order
     *
     * @mbggenerated Sat Mar 02 15:32:51 CST 2019
     */
    int updateByPrimaryKey(TbOrder record);
}