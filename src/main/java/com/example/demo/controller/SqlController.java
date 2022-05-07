package com.example.demo.controller;

import com.example.demo.entity.ResState;
import com.example.demo.entity.UserBean;
import com.example.demo.entity.UserContainer;
import com.example.demo.entity.UserSql;
import com.example.demo.service.SqlAllocate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags= "sql资源接口")
public class SqlController {
    @Autowired
    private SqlAllocate sqlAllocate;

    @ApiOperation(value = "创建用户")
    @PostMapping (value = "/creatuser")
    public ResState creatuser(@RequestBody UserSql userSql){
        ResState res = sqlAllocate.creatuser(userSql);
        return res;
    }

    @ApiOperation(value = "授权用户")
    @PostMapping (value = "/chmoduser")
    public ResState chmoduser(@RequestBody UserSql userSql){
        ResState res = sqlAllocate.giveChomd(userSql);
        return res;
    }

    @ApiOperation(value = "授权用户")
    @PostMapping (value = "/dropuser")
    public ResState dropuser(@RequestBody UserSql userSql){
        ResState res = sqlAllocate.dropusr(userSql);
        return res;
    }
}
