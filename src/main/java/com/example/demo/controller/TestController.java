package com.example.demo.controller;

import com.example.demo.service.UserContainerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@Api(tags= "ceshi接口")
public class TestController {
    @Autowired
    private UserContainerService userContainerService;


    @GetMapping("/test1")
    @ApiOperation("test1")
    public List<String> getAllContainers(Integer userid) {
        return userContainerService.findByUserid(userid);
    }




}
