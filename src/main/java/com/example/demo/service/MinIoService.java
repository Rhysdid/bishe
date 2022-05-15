package com.example.demo.service;

import com.example.demo.config.MinIoProperties;
import com.example.demo.entity.ResState;
import com.example.demo.entity.UserMinio;
import com.example.demo.repository.UserMinioRepository;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.PutObjectOptions;
import io.minio.messages.Bucket;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Slf4j
@Component
public class MinIoService {

    @Autowired
    MinIoProperties minIoProperties;
    @Autowired
    private UserMinioRepository userMinioRepository;

    private static MinioClient minioClient;




    /**
     * 初始化minio配置
     *
     * @param :
     * @return: void
     * @date : 2020/8/16 20:56
     */

    public Iterable<UserMinio> getallfile(String status){
        if(status.equals("1")){
            Iterable<UserMinio> res=userMinioRepository.findAll();
            return res;
        }else{
            return null;
        }
    }

    public List<UserMinio> getuserfile(Integer userid){
        List<UserMinio> res=userMinioRepository.findByUserid(userid);
        return res;
    }

    public Integer getRestmem(Integer userid){
        List<UserMinio> lists=userMinioRepository.findByUserid(userid);
        int res=204800;
        for (UserMinio list:lists){
            res-= list.getSize();
        }
        return res;
    }

    @PostConstruct
    public void init() {
        try {
            minioClient = new MinioClient(minIoProperties.getUrl(), minIoProperties.getAccessKey(),
                    minIoProperties.getSecretKey());
            createBucket(minIoProperties.getBucketName());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("初始化minio配置异常: 【{}】", e.fillInStackTrace());
        }
    }

    /**
     * 判断 bucket是否存在
     *
     * @param bucketName:
     *            桶名
     * @return: boolean
     * @date : 2020/8/16 20:53
     */
    @SneakyThrows(Exception.class)
    public static boolean bucketExists(String bucketName) {
        return minioClient.bucketExists(bucketName);
    }

    /**
     * 创建 bucket
     *
     * @param bucketName:
     *            桶名
     * @return: void
     * @date : 2020/8/16 20:53
     */
    @SneakyThrows(Exception.class)
    public static void createBucket(String bucketName) {
        boolean isExist = minioClient.bucketExists(bucketName);
        if (!isExist) {
            minioClient.makeBucket(bucketName);
        }
    }

    /**
     * 获取全部bucket
     *
     * @param :
     * @return: java.util.List<io.minio.messages.Bucket>
     * @date : 2020/8/16 23:28
     */
    @SneakyThrows(Exception.class)
    public static List<Bucket> getAllBuckets() {
        return minioClient.listBuckets();
    }

    /**
     * 文件上传
     *
     * @param bucketName:
     *            桶名
     * @param fileName:
     *            文件名
     * @param filePath:
     *            文件路径
     * @return: void
     * @date : 2020/8/16 20:53
     */
    @SneakyThrows(Exception.class)
    public static void upload(String bucketName, String fileName, String filePath) {
        minioClient.putObject(bucketName, fileName, filePath, null);
    }

    /**
     * 文件上传
     *
     * @param bucketName:
     *            桶名
     * @param fileName:
     *            文件名
     * @param stream:
     *            文件流
     * @return: java.lang.String : 文件url地址
     * @date : 2020/8/16 23:40
     */


    /**
     * 文件上传
     *
     * @param bucketName:
     *            桶名
     * @param file:
     *            文件
     * @return: java.lang.String : 文件url地址
     * @date : 2020/8/16 23:40
     */
    @SneakyThrows(Exception.class)
    public ResState upload(String bucketName, MultipartFile file, Integer userid) {
        ResState res=new ResState();
        if (null == file || 0 == file.getSize()) {
            res.setMsg("文件不能为空");
            res.setState(false);
            return res;
        }
        List<UserMinio> lists=userMinioRepository.findByUserid(userid);
        int sum=0;
        for(UserMinio list:lists){
            sum+=list.getSize();
        }
        if(sum>409600){
            res.setMsg("您已到达存储上限200M，请删除部分文件后重新上传");
            res.setState(false);
            return res;
        }
        //文件名
        String originalFilename = file.getOriginalFilename();
        System.out.println(originalFilename);
        //新的文件名 = 存储桶文件名_时间戳.后缀名
        String fileName = userid.toString()+ "_" +
                System.currentTimeMillis() +
                originalFilename.substring(originalFilename.lastIndexOf("."));
        final InputStream is = file.getInputStream();
        minioClient.putObject(bucketName, fileName, is, new PutObjectOptions(is.available(), -1));
        is.close();
        UserMinio userMinio=new UserMinio();
        userMinio.setUserid(userid);
        userMinio.setFilename(fileName);
        userMinio.setPastname(originalFilename);
        userMinio.setSize((int)file.getSize()/1024);
        userMinioRepository.save(userMinio);
        res.setMsg("上传成功");
        res.setState(true);
        return res;
    }

    /**
     * 删除文件
     *
     * @param bucketName:
     *            桶名
     * @param fileName:
     *            文件名
     * @return: void
     * @date : 2020/8/16 20:53
     */
    @Transactional
    @SneakyThrows(Exception.class)
    public ResState deleteFile(String bucketName, String fileName,Integer userid) {
        List<UserMinio> lists= userMinioRepository.findByUserid(userid);
        ResState res=new ResState();
        boolean exist=false;
        for(UserMinio list:lists){
            if(list.getFilename().equals(fileName)){
                exist=true;
            }
        }
        if(!exist){
            res.setMsg("您没有这个文件，请检查输入");
            res.setState(false);
            return res;
        }
        minioClient.removeObject(bucketName, fileName);
        userMinioRepository.deleteByFilename(fileName);
        res.setMsg("删除成功");
        res.setState(true);
        return res;
    }

    /**
     * 下载文件
     *
     * @param bucketName:
     *            桶名
     * @param fileName:
     *            文件名
     * @param response:
     * @return: void
     * @date : 2020/8/17 0:34
     */
    @SneakyThrows(Exception.class)
    public void download(String bucketName, String fileName, HttpServletResponse response) {
        // 获取对象的元数据
        final ObjectStat stat = minioClient.statObject(bucketName, fileName);
        response.setContentType(stat.contentType());
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        InputStream is = minioClient.getObject(bucketName, fileName);
        IOUtils.copy(is, response.getOutputStream());
        is.close();
    }

    /**
     * 获取minio文件的下载地址
     *
     * @param bucketName:
     *            桶名
     * @param fileName:
     *            文件名
     * @return: java.lang.String
     * @date : 2020/8/16 22:07
     */
    @SneakyThrows(Exception.class)
    public ResState getFileUrl(String bucketName, String fileName,Integer userid) {
        List<UserMinio> list=userMinioRepository.findByUserid(userid);
        boolean ifexist=false;
        for(UserMinio userMinio:list){
            if(userMinio.getFilename().equals(fileName))ifexist=true;
        }
        ResState res=new ResState();
        if(ifexist){
            res.setMsg(minioClient.presignedGetObject(bucketName, fileName));
            res.setState(true);
            return res;
        }else{
            res.setMsg("获取失败，请检查你的输入是否正确");
            res.setState(false);
            return res;
        }
    }

}
