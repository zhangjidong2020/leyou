package com.leyou.controller;

import com.leyou.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Controller
public class PageController {


    @Autowired
    private PageService pageService;

    @GetMapping("item/{spuId}.html")
    public String  toPage(@PathVariable("spuId") Long spuId, Model model){

        Map<String,Object> map=pageService.loadData(spuId);
        model.addAllAttributes(map);
        return "item";

    }
}
