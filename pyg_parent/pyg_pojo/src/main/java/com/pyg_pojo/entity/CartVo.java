package com.pyg_pojo.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @version: v1.0
 * @author: 刘忠达
 * @date: 2019/3/31 16:17
 */
public class CartVo implements Serializable {
    private List<Cart> cartList;
    private Long itemId;
    private Integer num;

    public List<Cart> getCartList() {
        return cartList;
    }

    public void setCartList(List<Cart> cartList) {
        this.cartList = cartList;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
