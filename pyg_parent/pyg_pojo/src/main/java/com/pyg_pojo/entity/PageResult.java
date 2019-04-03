package com.pyg_pojo.entity;

import java.io.Serializable;
import java.util.List;

public class PageResult implements Serializable {

    private long Total;
    private List rows;

    public long getTotal() {
        return Total;
    }

    public void setTotal(Integer total) {
        Total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }

    public PageResult(long total, List rows) {

        this.Total = total;
        this.rows = rows;
    }
}
