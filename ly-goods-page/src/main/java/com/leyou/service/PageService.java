package com.leyou.service;

import com.leyou.client.GoodsClient;
import com.leyou.item.pojo.Spu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PageService {
    @Autowired
    private GoodsClient goodsClient;
    public Map<String, Object> loadData(Long spuId) {
        Map<String, Object> map=new HashMap<String, Object>();
        Spu spu = goodsClient.querySpuById(spuId);

        //根据spu的id查询spu'
        return  null;
    }
}
