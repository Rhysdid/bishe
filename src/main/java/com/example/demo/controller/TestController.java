package com.example.demo.controller;

import com.example.demo.entity.ContainerInfo;
import com.example.demo.entity.ResState;
import com.example.demo.entity.UserBean;
import com.example.demo.entity.UserContainer;
import com.example.demo.service.ContainerService;
import com.example.demo.service.UserContainerService;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.InspectVolumeResponse;
import com.github.dockerjava.api.command.ListVolumesResponse;
import com.github.dockerjava.api.command.LogContainerCmd;
import com.github.dockerjava.api.model.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jnr.ffi.annotations.In;
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
