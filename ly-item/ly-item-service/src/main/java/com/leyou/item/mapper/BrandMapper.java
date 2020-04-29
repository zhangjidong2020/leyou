package com.leyou.item.mapper;

import com.leyou.item.pojo.Brand;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import tk.mybatis.mapper.common.Mapper;

public interface BrandMapper  extends Mapper<Brand> {


    @Insert("insert into tb_category_brand(brand_id,category_id) values(#{id},#{cid})")
    void insertBrandCategory(Long id, Long cid);

    @Delete("delete from tb_category_brand where brand_id=#{id}")
    void deleteBrandCategory(Long id);
    //id 品牌id
    //cid 分类id
}
