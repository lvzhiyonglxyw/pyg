package com.pinyougou.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.search.service.SeachItenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
@SuppressWarnings("all")
@RestController
@RequestMapping("/seachItenController")
public class SeachItenController {

    @Reference
    private SeachItenService seachItenService;

    /**
     * 使用solr进行查询
     * @param seachMap
     * @return
     */
    @RequestMapping("/seach")
    public Map seach(@RequestBody Map seachMap) {
        return seachItenService.seachItem(seachMap);
    }
}
