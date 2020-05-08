package com.leyou.search.client;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "item-service")
public interface BrandClient {


    @GetMapping("brand/page")
    public PageResult<Brand> pageQuery(@RequestParam(value = "page",defaultValue = "1") Integer page,
                                                       @RequestParam(value = "rows",defaultValue = "10") Integer rows,
                                                       @RequestParam(value = "sortBy",required = false) String  sortBy,
                                                       @RequestParam(value = "desc",required = false) Boolean desc,
                                                       @RequestParam(value = "key",required = false) String  key);




    //http://api.leyou.com/api/item/brand/cid/76
    @GetMapping("brand/cid/{id}")
    public List<Brand> queryBrandByCategory(@PathVariable("id") Long cid);
}
