package com.pyg_pojo.entity;

import com.pyg_pojo.pojo.TbOrderItem;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Cart implements Serializable {

    private String sellerId; //商家id
    private String sellerName;//商家
    private List<TbOrderItem>tbOrderItems;//订单明细

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return Objects.equals(sellerId, cart.sellerId) &&
                Objects.equals(sellerName, cart.sellerName) &&
                Objects.equals(tbOrderItems, cart.tbOrderItems);
    }

    @Override
    public int hashCode() {

        return Objects.hash(sellerId, sellerName, tbOrderItems);
    }

    @Override
    public String toString() {
        return "Cart{" +
                "sellerId='" + sellerId + '\'' +
                ", sellerName='" + sellerName + '\'' +
                ", tbOrderItems=" + tbOrderItems +
                '}';
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public List<TbOrderItem> getTbOrderItems() {
        return tbOrderItems;
    }

    public void setTbOrderItems(List<TbOrderItem> tbOrderItems) {
        this.tbOrderItems = tbOrderItems;
    }

    public Cart() {

    }

    public Cart(String sellerId, String sellerName, List<TbOrderItem> tbOrderItems) {

        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.tbOrderItems = tbOrderItems;
    }
}
