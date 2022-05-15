package com.example.demo.controller;

import com.example.demo.entity.ResState;
import com.example.demo.entity.User;
import com.example.demo.entity.UserSql;
import com.example.demo.service.SqlAllocateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags= "sql资源接口")
public class SqlController {
    @Autowired
    private SqlAllocateService sqlAllocate;

    @ApiOperation(value = "获取mysql服务")
    @PostMapping (value = "/creatmysql")
    public ResState creatuser(@RequestBody UserSql userSql){
        ResState res = sqlAllocate.creatmysql(userSql);
        return res;
    }

    @ApiOperation(value = "展示所有mysql服务")
    @PostMapping (value = "/showmysql")
    public Iterable<UserSql> showmysql(@RequestBody User user){
        Iterable<UserSql> res = sqlAllocate.showmysql(user);
        return res;
    }

    @ApiOperation(value = "删除指定mysql服务")
    @DeleteMapping (value = "/showmysql")
    public ResState deletemysql(@RequestBody UserSql userSql){
        ResState res = sqlAllocate.deletemysql(userSql);
        return res;
    }

}
