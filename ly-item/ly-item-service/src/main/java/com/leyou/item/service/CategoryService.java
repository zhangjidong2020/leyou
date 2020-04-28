package com.leyou.item.service;

import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category> queryByParentId(Long id) {

        Category category = new Category();
        category.setParentId(id);

        List<Category> categories = categoryMapper.select(category);
        //select * from tb_category parent_id=0;
        return categories;


    }
}
