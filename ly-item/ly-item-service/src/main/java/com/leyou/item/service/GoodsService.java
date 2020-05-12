package com.leyou.item.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.mapper.*;
import com.leyou.item.pojo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private AmqpTemplate template;

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

    @Transactional
    public void saveGoods(SpuBo spuBo) {

        spuBo.setSaleable(true);
        spuBo.setValid(true);
        spuBo.setCreateTime(new Date());
        spuBo.setLastUpdateTime(new Date());
        //保存spu
        this.spuMapper.insertSelective(spuBo);

        SpuDetail spuDetail = spuBo.getSpuDetail();
        spuDetail.setSpuId(spuBo.getId());
        //保存spudetail
        this.spuDetailMapper.insertSelective(spuDetail);

        //保存sku和stock
        List<Sku> skus = spuBo.getSkus();
        saveSkus(spuBo,skus);
        //发一条消息
        send("insert",spuBo.getId());

    }

    private void saveSkus(SpuBo spuBo, List<Sku> skus) {
        for(Sku sku:skus){
            sku.setSpuId(spuBo.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(new Date());
            //保存sku
            skuMapper.insertSelective(sku);

            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            this.stockMapper.insert(stock);


        }


    }

    public SpuDetail querySpuDetailBySpuId(Long spuId) {
        return this.spuDetailMapper.selectByPrimaryKey(spuId);


    }

    public List<Sku> querySkuBySpuId(Long spuId) {

        Sku sku = new Sku();
        sku.setSpuId(spuId);
        //select * from tb_sku where spu_id=2
        List<Sku> skuList = this.skuMapper.select(sku);
        for(Sku s:skuList){
            Long id = s.getId();//sku的id

            //根据sku的id查询库存表
            Stock stock = this.stockMapper.selectByPrimaryKey(id);
            sku.setStock(stock.getStock());


        }
        return skuList;
    }

    @Transactional
    public void updateGoods(SpuBo spuBo) {
        spuBo.setLastUpdateTime(new Date());
        //更新spu表
        this.spuMapper.updateByPrimaryKeySelective(spuBo);

        //更新spudetail表
        this.spuDetailMapper.updateByPrimaryKeySelective(spuBo.getSpuDetail());
        //删除sku和stock表的老数据，然后进行插入

        //先查询sku数据
        Sku sku = new Sku();
        sku.setSpuId(spuBo.getId());

        List<Sku> skuList = this.skuMapper.select(sku);


        //删除一
       /* for(Sku s:skuList){
            Long id = s.getId();//获取sku的id
            //根据sku的id删除stock表
            this.stockMapper.deleteByPrimaryKey(id);
            //delete from tb_stock where sku_id =2600242
            //删除sku表
            this.skuMapper.delete(s);
        }*/

        //删除二
        if(!CollectionUtils.isEmpty(skuList)){

            //利用jdk新特性
            List<Long> ids = skuList.stream().map(Sku::getId).collect(Collectors.toList());//2600242 2600248

            //delete from tb_stock where sku_id in(2600242,2600248)
            this.stockMapper.deleteByIdList(ids);
            this.skuMapper.delete(sku);

        }
        //插入
        saveSkus(spuBo,spuBo.getSkus());


        //重建索引
        //产生静态页面

        //发一条消息
        send("update",spuBo.getId());





    }

    //发送消息
    public void send(String type,Long id){

        //                   routing key
                            //item.update
                            //item.insert
        template.convertAndSend("item."+type,id);

    }

    public Spu querySpuById(Long spuId) {

        return this.spuMapper.selectByPrimaryKey(spuId);

    }

    //发一条消息
    //send("delete",spuBo.getId());
}
