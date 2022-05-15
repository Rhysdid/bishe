package com.example.demo.controller;

import com.example.demo.config.MinIoProperties;
import com.example.demo.entity.ResState;
import com.example.demo.entity.User;
import com.example.demo.entity.UserContainer;
import com.example.demo.entity.UserMinio;
import com.example.demo.service.MinIoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.License;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/minio")
@Api(tags = {"MinIO测试接口"})
public class MinIoController {

    @Autowired
    MinIoProperties minIoProperties;
    @Autowired
    private MinIoService minIoUtil;

    @ApiOperation(value = "上传文件")
    @PostMapping(value = "/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResState upload(Integer userid, @RequestPart("file") MultipartFile file) throws Exception {
        ResState res = minIoUtil.upload(minIoProperties.getBucketName(), file,userid);
        return res;
    }

    @ApiOperation(value = "下载文件")
    @GetMapping(value = "/download")
    public void download(@RequestParam("fileName") String fileName, HttpServletResponse response) {
        minIoUtil.download(minIoProperties.getBucketName(), fileName, response);
    }

    @ApiOperation(value = "删除文件")
    @DeleteMapping(value = "/delete")
    public ResState delete(@RequestBody UserMinio userMinio) {
        ResState res=minIoUtil.deleteFile(minIoProperties.getBucketName(), userMinio.getFilename(),userMinio.getUserid());
        return res;
    }

    @ApiOperation(value = "获取文件url")
    @GetMapping(value = "/downloadurl")
    public ResState downloadurl(@RequestParam("bucketName")String bucketName,@RequestParam("fileName") String fileName,@RequestParam("userid") Integer userid) {
        ResState res=minIoUtil.getFileUrl(bucketName,fileName,userid);
        return res;
    }

    @ApiOperation(value = "获取用户的文件")
    @PostMapping(value = "/getuserfile")
    public List<UserMinio> getuserfile(@RequestBody UserMinio userMinio) {
        List<UserMinio> list=minIoUtil.getuserfile(userMinio.getUserid());
        return list;
    }

    @ApiOperation(value = "获取所有文件信息")
    @PostMapping(value = "/getallfile")
    public Iterable<UserMinio> getallfile(@RequestBody User user) {
        Iterable<UserMinio> list=minIoUtil.getallfile(user.getStatus());
        return list;
    }

    @ApiOperation(value = "获取用户的剩余空间")
    @PostMapping(value = "/getuserMem")
    public Integer getuserMem(@RequestBody UserMinio userMinio) {
        Integer res=minIoUtil.getRestmem(userMinio.getUserid());
        return res;
    }

}
