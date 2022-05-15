package com.example.demo.controller;

import com.example.demo.entity.ContainerInfo;
import com.example.demo.entity.ResState;
import com.example.demo.entity.UserContainer;
import com.example.demo.service.ContainerService;
import com.example.demo.service.UserContainerService;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@Api(tags= "容器管理接口")
public class ContainersController {
    @Autowired
    private ContainerService containerService;

    @GetMapping("/allcontainer")
    @ApiOperation("获取所有容器")
    public List<Container> getAllContainers() {
        return containerService.getAllContainers();
    }

    @GetMapping("/managecontainer")
    @ApiOperation("管理所有容器")
    public Iterable<UserContainer> manageAllContainers() { return containerService.manageAllContainers(); }

    @PostMapping ("/inspectcontainer")
    @ApiOperation("用户获取容器信息")
    public List<InspectContainerResponse> inspectContainer(@RequestBody UserContainer userContainer) {
        List<InspectContainerResponse> res=containerService.inspectContainer(userContainer.getUserid());
        return res;
    }

    @PostMapping ("/statscontainer")
    @ApiOperation("用户获取容器stats")
    public List<Statistics> statsContainer(@RequestBody UserContainer userContainer) {
        List<Statistics> res=containerService.statsContainer(userContainer.getUserid());
        return res;
    }

    @PostMapping ("/stopcontainer")
    @ApiOperation("停止指定容器")
    public ResState stopContainer(@RequestBody UserContainer userContainer) {
        return containerService.stopContainer(userContainer.getContainerid(),userContainer.getUserid());
    }

    @PostMapping ("/startcontainer")
    @ApiOperation("运行指定容器")
    public ResState startContainer(@RequestBody UserContainer userContainer) {
        return containerService.startContainer(userContainer.getContainerid(),userContainer.getUserid());
    }

    @DeleteMapping ("/deletecontainer")
    @ApiOperation("删除指定容器")
    public ResState deleteContainer(@RequestBody UserContainer userContainer) {
        return containerService.deleteContainer(userContainer.getContainerid(),userContainer.getUserid());
    }

    @PostMapping("/creatcontainer")
    @ApiOperation("创建容器")
    public ResState creatContainer(@RequestBody ContainerInfo containerInfo) {
        return containerService.creatContainer(containerInfo);
    }
}
