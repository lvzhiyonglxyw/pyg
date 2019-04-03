package com.itheima.Test01;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.InputStream;

public class TestClass {
    @Autowired
    private HttpServletRequest request;
    @Test
    public void test(){
        File file = new File("E:/heima/Java/pyg_parent/pyg_page_web/webapp/");

        System.out.println(file.getAbsolutePath());
        new File("E:/heima/Java/pyg_parent/pyg_page_web/webapp/123.txt").delete();
    }
}
