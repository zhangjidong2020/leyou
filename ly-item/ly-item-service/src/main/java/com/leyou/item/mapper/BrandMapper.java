package com.leyou.item.mapper;

import com.leyou.item.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import tk.mybatis.mapper.common.Mapper;

public interface BrandMapper  extends Mapper<Brand> {


    @Insert("insert into tb_category_brand(brand_id,category_id) values(#{id},#{cid})")
    void insertBrandCategory(Long id, Long cid);
    //id 品牌id
    //cid 分类id
}
