package com.pyg_pojo.entity;

import com.pyg_pojo.pojo.TbSpecification;
import com.pyg_pojo.pojo.TbSpecificationOption;

import java.io.Serializable;
import java.util.List;

public class Specification implements Serializable {

    //规格表
    private TbSpecification tbSpecification;
    //规格选项表
    private List<TbSpecificationOption> tbSpecificationOptionList;

    public TbSpecification getTbSpecification() {
        return tbSpecification;
    }

    public void setTbSpecification(TbSpecification tbSpecification) {
        this.tbSpecification = tbSpecification;
    }

    public List<TbSpecificationOption> getTbSpecificationOptionList() {
        return tbSpecificationOptionList;
    }

    public void setTbSpecificationOptionList(List<TbSpecificationOption> tbSpecificationOptionList) {
        this.tbSpecificationOptionList = tbSpecificationOptionList;
    }

    public Specification() {

    }

    public Specification(TbSpecification tbSpecification, List<TbSpecificationOption> tbSpecificationOptionList) {

        this.tbSpecification = tbSpecification;
        this.tbSpecificationOptionList = tbSpecificationOptionList;
    }
}
