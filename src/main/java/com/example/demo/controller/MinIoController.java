package com.example.demo.controller;

import com.example.demo.config.MinIoProperties;
import com.example.demo.entity.ResState;
import com.example.demo.service.MinIoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    public String delete(@RequestParam("fileName") String fileName) {
        minIoUtil.deleteFile(minIoProperties.getBucketName(), fileName);
        return "删除成功";
    }

    @ApiOperation(value = "获取文件url")
    @GetMapping(value = "/downloadurl")
    public String downloadurl(@RequestParam("bucketName")String bucketName,@RequestParam("fileName") String fileName) {
        String fileUrl=minIoUtil.getFileUrl(bucketName,fileName);
        return fileUrl;
    }

    @ApiOperation(value = "获取用户的文件")
    @PostMapping(value = "/getuserfile")
    public List<String> downloadurl(@RequestBody Integer userid) {
        List<String> list=minIoUtil.getuserfile(userid);
        return list;
    }

}
