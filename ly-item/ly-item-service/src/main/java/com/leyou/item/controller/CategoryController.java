package com.leyou.item.controller;

import com.leyou.item.pojo.Category;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.soap.Addressing;
import java.util.List;

@RestController
@RequestMapping("category")
public class CategoryController {
    //#http://127.0.0.1:9081/category/list?pid=0

    @Autowired
    private CategoryService categoryService;

    @RequestMapping("list")
    public ResponseEntity<List<Category>> queryByParentId(@RequestParam("pid") Long id){

        //查询分类表
        List<Category> categories=categoryService.queryByParentId(id);
        if(null!=categories&&categories.size()!=0){


            return ResponseEntity.ok(categories);//返回页面相应码200，数据

        }
        return  ResponseEntity.status(HttpStatus.NO_CONTENT).build();//204


    }


}
