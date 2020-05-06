package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GoodsController {

    @Autowired
    private GoodsService goodsService;
    //http://api.leyou.com/api/item/spu/page?key=小米&saleable=true&page=1&rows=5
    @RequestMapping("spu/page")
    public ResponseEntity<PageResult<SpuBo>> querySpuByPage(@RequestParam(value = "page",defaultValue = "1") Integer page,
                                                            @RequestParam(value = "rows",defaultValue = "10") Integer rows,
                                                            @RequestParam(value = "saleable",required = false) Boolean saleable,
                                                            @RequestParam(value = "key",required = false) String  key){


        PageResult<SpuBo> pageResult=this.goodsService.querySpuByPage(page,rows,saleable,key);

        if(pageResult!=null&&pageResult.getItems()!=null&&pageResult.getItems().size()!=0){
            return ResponseEntity.ok(pageResult);


        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
    //http://api.leyou.com/api/item/goods
    @PostMapping("goods")
    public ResponseEntity<Void> saveGoods(@RequestBody SpuBo spuBo){
        //@RequestBody接受前台json格式字符串

        this.goodsService.saveGoods(spuBo);
        return ResponseEntity.status(HttpStatus.CREATED).build();//相应码201



    }


}
