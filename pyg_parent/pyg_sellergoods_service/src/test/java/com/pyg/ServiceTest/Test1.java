package com.pyg.ServiceTest;

import com.pyg_dao.mapper.TbBrandMapper;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test1 {
    @Test
    public void test(){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring/applicationContext-dao.xml");
        TbBrandMapper bean = applicationContext.getBean(TbBrandMapper.class);
        //List<TbBrand> all = bean.findAll();

    }
}
