package com.leyou.item.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

@Service
public class BrandService {

    @Autowired
    private BrandMapper brandMapper;

    public PageResult<Brand> pageQuery(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        //page=1&rows=5&sortBy=id&desc=false&key=iphone

        //开启分页助手
        PageHelper.startPage(page,rows);
        //过滤
        Example example = new Example(Brand.class);
        if(StringUtils.isNoneBlank(key)){
            //创建查询条件的构造对象
            Example.Criteria criteria = example.createCriteria();
            criteria.andLike("name","%"+key+"%");
            //name like "%"+iphone+"%"
        }
        //判断排序字段不为空
        if(StringUtils.isNoneBlank(sortBy)){

            example.setOrderByClause(sortBy+(desc?" DESC":" ASC"));
            //order by id ASC

        }
        //查询结果并转化分页条件结果
        Page<Brand> brandPage=(Page<Brand>)brandMapper.selectByExample(example);
        //通用mappper select * from tb_brand where name like "%"+iphone+"%" order by id ASC
        //分页助手 拦截sql
        //select * from tb_brand where name like "%"+iphone+"%" order by id ASC  limit 0,5
        //select count(*) from tb_brand name like "%"+iphone+"%"

        //PageResult(Long total, Long totalPage, List<T> items)
        PageResult pageResult = new PageResult(brandPage.getTotal(), new Long(brandPage.getPages()), brandPage.getResult());
        return pageResult;


    }
}

