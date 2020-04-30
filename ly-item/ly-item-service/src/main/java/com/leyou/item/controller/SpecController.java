package com.leyou.item.controller;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("spec")
public class SpecController {

    @Autowired
    private SpecService specService;
    //http://api.leyou.com/api/item/spec/groups/76

    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> querySpecGroups(@PathVariable("cid") Long cid){
        //根据分类id查询规格组
        List<SpecGroup> specGroups=this.specService.querySpecGroups(cid);
        if(specGroups!=null&&specGroups.size()>0){

            return ResponseEntity.ok(specGroups);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    //http://api.leyou.com/api/item/spec/params?gid=1
    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> querySpecParam(@RequestParam(value = "gid",required = false) Long gid,
                                                          @RequestParam(value = "cid",required = false) Long cid,
                                                          @RequestParam(value = "searching",required = false) Boolean searching,
                                                          @RequestParam(value = "generic",required = false) Boolean generic){

        //根据规格组id 查询规格参数
        List<SpecParam> specParams=this.specService.querySpecParam(gid,cid,searching,generic);
        if(specParams!=null&&specParams.size()>0){

            return ResponseEntity.ok(specParams);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();



    }



}
