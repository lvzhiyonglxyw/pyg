package com.pyg_dao.mapper;

import com.pyg_pojo.pojo.TbPromotion;
import com.pyg_pojo.pojo.TbPromotionExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbPromotionMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_promotion
     *
     * @mbggenerated Tue Apr 02 13:07:31 CST 2019
     */
    int countByExample(TbPromotionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_promotion
     *
     * @mbggenerated Tue Apr 02 13:07:31 CST 2019
     */
    int deleteByExample(TbPromotionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_promotion
     *
     * @mbggenerated Tue Apr 02 13:07:31 CST 2019
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_promotion
     *
     * @mbggenerated Tue Apr 02 13:07:31 CST 2019
     */
    int insert(TbPromotion record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_promotion
     *
     * @mbggenerated Tue Apr 02 13:07:31 CST 2019
     */
    int insertSelective(TbPromotion record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_promotion
     *
     * @mbggenerated Tue Apr 02 13:07:31 CST 2019
     */
    List<TbPromotion> selectByExample(TbPromotionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_promotion
     *
     * @mbggenerated Tue Apr 02 13:07:31 CST 2019
     */
    TbPromotion selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_promotion
     *
     * @mbggenerated Tue Apr 02 13:07:31 CST 2019
     */
    int updateByExampleSelective(@Param("record") TbPromotion record, @Param("example") TbPromotionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_promotion
     *
     * @mbggenerated Tue Apr 02 13:07:31 CST 2019
     */
    int updateByExample(@Param("record") TbPromotion record, @Param("example") TbPromotionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_promotion
     *
     * @mbggenerated Tue Apr 02 13:07:31 CST 2019
     */
    int updateByPrimaryKeySelective(TbPromotion record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_promotion
     *
     * @mbggenerated Tue Apr 02 13:07:31 CST 2019
     */
    int updateByPrimaryKey(TbPromotion record);
}