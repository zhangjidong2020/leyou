package com.leyou.item.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.mapper.SpuDetailMapper;
import com.leyou.item.mapper.SpuMapper;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Spu;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class GoodsService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandMapper brandMapper;

    public PageResult<SpuBo> querySpuByPage(Integer page, Integer rows, Boolean saleable, String key) {
        //开启分页助手
        PageHelper.startPage(page,rows);
        //创建查询条件
        Example example = new Example(Spu.class);
        //查询条件的构建工具
        Example.Criteria criteria = example.createCriteria();
        //是否进行模糊查询
        if(StringUtils.isNoneBlank(key)){
            criteria.andLike("title","%"+key+"%");
        }
        if(saleable!=null){
            criteria.andEqualTo("saleable",saleable);
        }

        //查询spu表 select * from tb_spu where title like '%+小米+%' and saleable=false
        List<Spu> spus = this.spuMapper.selectByExample(example);
        //转成分页助手的page对象
        Page<Spu> spuPage = (Page<Spu>)spus;

        //用来存放spubo
        List<SpuBo> spuBos = new ArrayList<>();

        for(Spu spu:spus){

            SpuBo spuBo = new SpuBo();
            //属性拷贝
            BeanUtils.copyProperties(spu,spuBo);
            //根据spu的商品分类的id,查询商品分类的名称
            List<String> names=categoryService.queryNamesByIds(Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3()));


            spuBo.setCname(StringUtils.join(names, "/"));//设置商品分类的名称

            Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());
            spuBo.setBname(brand.getName());//设置商品的名称

            spuBos.add(spuBo);//放入list

        }

        return new PageResult<>(spuPage.getTotal(),new Long(spuPage.getPages()),spuBos);


    }
}
