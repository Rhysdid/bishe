package com.example.demo.service;

import com.example.demo.entity.ResState;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.api.model.SearchItem;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.command.PullImageResultCallback;
import com.github.dockerjava.jaxrs.JerseyDockerCmdExecFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

public class ImageService {
    //"E:\\毕设\\zhengshu"
    DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().withDockerTlsVerify(true)
            .withDockerCertPath("/usr/jar/zhengshu").withDockerHost("tcp://124.221.246.68:2375")
            .withDockerConfig("/usr/jar/zhengshu").withApiVersion("1.41").withRegistryUrl("https://index.docker.io/v1/")
            .withRegistryUsername("wjq").withRegistryPassword("Dbnzja+513")
            .withRegistryEmail("xx").build();
    DockerCmdExecFactory dockerCmdExecFactory =  new JerseyDockerCmdExecFactory()
            .withReadTimeout(100000)
            .withConnectTimeout(100000)
            .withMaxTotalConnections(100)
            .withMaxPerRouteConnections(10);

    private DockerClient dockerClient = DockerClientBuilder.getInstance(config).withDockerCmdExecFactory(dockerCmdExecFactory).build();


    public List<Image> getAllImages(){
        List<Image> images = dockerClient.listImagesCmd().exec();
        return images;
    }

    public List<SearchItem> searchImages(String imageName){
        List<SearchItem> dockerSearch = dockerClient.searchImagesCmd(imageName).exec();
        return dockerSearch;
    }

    public ResState pullImage(String imageName){
        ResState res=new ResState();
        dockerClient.pullImageCmd(imageName).exec(new PullImageResultCallback()).awaitSuccess();
        res.setMsg("下载镜像成功");
        res.setState(true);
        return res;
    }

    public ResState removeImage(String imageName){
        ResState res=new ResState();
        dockerClient.removeImageCmd(imageName).exec();
        res.setMsg("删除镜像成功");
        res.setState(true);
        return res;
    }
}
