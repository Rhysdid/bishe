package com.example.demo.controller;

import com.example.demo.entity.ResState;
import com.example.demo.entity.UserContainer;
import com.example.demo.service.ImageService;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.SearchItem;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags= "镜像管理接口")
public class ImagesController {
    ImageService imageService=new ImageService();

    @GetMapping("/allimage")
    @ApiOperation("获取所有镜像")
    public List<Image> getAllImages() {
        return imageService.getAllImages();
    }

    @GetMapping("/searchimage")
    @ApiOperation("查找镜像")
    public @ResponseBody
    List<SearchItem> searchImage(String imageName) {
        return imageService.searchImages(imageName);
    }

    @PostMapping("/pullimage")
    @ApiOperation("下载镜像")
    public ResState pullImage(@RequestBody UserContainer userContainer) {
        return imageService.pullImage(userContainer.getImagename());
    }

    @DeleteMapping("/removeimage")
    @ApiOperation("删除镜像")
    public ResState removeImage(@RequestBody UserContainer userContainer) {
        return imageService.removeImage(userContainer.getImagename());
    }
}
