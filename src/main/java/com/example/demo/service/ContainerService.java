package com.example.demo.service;

import com.example.demo.entity.ContainerInfo;
import com.example.demo.entity.ResState;
import com.example.demo.entity.UserContainer;
import com.example.demo.repository.UserContainerRepository;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import com.github.dockerjava.jaxrs.JerseyDockerCmdExecFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.query.ResultProcessor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContainerService {
    //"E:/毕设/zhengshu"
    //Resource resource = new ClassPathResource("zhengshu");
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
    @Autowired
    private UserContainerService userContainerService;
    //private UserContainerService userContainerService = BeanUtils.getBean(UserContainerService.class);

    public List<Container> getAllContainers(){
        List<Container> containers = dockerClient.listContainersCmd().exec();
        return containers;
    }

    public List<InspectContainerResponse> inspectContainer(Integer userid){
        List<String> list = userContainerService.findByUserid(userid);
        List<InspectContainerResponse> res=new ArrayList<>();
        for(String s:list){
            res.add(dockerClient.inspectContainerCmd(s).exec());
        }
        return res;
    }

    public ResState stopContainer(String containerID,Integer userid){
        ResState res=new ResState();
        List<String> list=userContainerService.findByUserid(userid);
        boolean exist=false;
        for(String s:list){
            if(s.equals(containerID))exist=true;
        }
        if(!exist){
            res.setMsg("您无权停止该容器");
            res.setState(false);
            return res;
        }
        List<Container> containers = dockerClient.listContainersCmd().exec();
        boolean running=false;
        for(Container container:containers){
            if(container.getId().substring(0,12).equals(containerID))running=true;
        }
        if(running){
            dockerClient.stopContainerCmd(containerID).exec();
            res.setMsg("停止容器成功");
            res.setState(true);
            return res;
        }else{
            res.setMsg("容器未在运行");
            res.setState(false);
            return res;
        }
    }

    public ResState startContainer(String containerID,Integer userid){
        ResState res=new ResState();
        List<String> list=userContainerService.findByUserid(userid);
        boolean exist=false;
        for(String s:list){
            if(s.equals(containerID))exist=true;
        }
        if(!exist){
            res.setMsg("您无权运行该容器");
            res.setState(false);
            return res;
        }
        List<Container> containers = dockerClient.listContainersCmd().exec();
        boolean running=false;
        for(Container container:containers){
            if(container.getId().substring(0,12).equals(containerID))running=true;
        }
        if(running){
            res.setMsg("容器已在运行");
            res.setState(false);
            return res;
        }else{
            dockerClient.startContainerCmd(containerID).exec();
            res.setMsg("运行容器成功");
            res.setState(true);
            return res;
        }
    }

    public ResState deleteContainer(String containerID, Integer userid){
        ResState res=new ResState();
        List<String> list=userContainerService.findByUserid(userid);
        boolean exist=false;
        for(String s:list){
            if(s.equals(containerID))exist=true;
        }
        if(!exist){
            res.setMsg("您无权删除该容器");
            res.setState(false);
            return res;
        }
        List<Container> containers = dockerClient.listContainersCmd().exec();
        boolean running=false;
        for(Container container:containers){
            if(container.getId().substring(0,12).equals(containerID))running=true;
        }
        if(running){
            res.setMsg("不能删除正在运行的容器");
            res.setState(false);
            return res;
        }else{
            dockerClient.removeContainerCmd(containerID).exec();
            userContainerService.delete(containerID);
            res.setMsg("删除容器成功");
            res.setState(true);
            return res;
        }
    }
    public interface ResultProcessor {
        void process(List<String> textList, String text);
    }

//    private List<String> runCommand(String cmd, ResultProcessor processor) {
//        List<String> resultList = new ArrayList<String>();
//        String[] cmds = {"/bin/sh", "-c", cmd};
//        Process pro = null;
//        try {
//            pro = Runtime.getRuntime().exec(cmds);
//            pro.waitFor();
//            InputStream in = pro.getInputStream();
//            BufferedReader read = new BufferedReader(new InputStreamReader(in));
//            String line = null;
//            while ((line = read.readLine()) != null) {
//                processor.process(resultList, line);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        return resultList;
//    }

    public ResState creatContainer(ContainerInfo containerInfo) {
        //ExposedPort tcp22 = ExposedPort.tcp(22);
        ResState res=new ResState();
        String tim= String.valueOf(System.currentTimeMillis());
        try{
            String[] cmds = {"/bin/sh","-c","mkdir "+containerInfo.getUserid()+"_"+tim+" && wget -O /usr/jar/"
                    +containerInfo.getUserid()+"_"+tim+"/"+containerInfo.getFilename()+
                    " http://124.221.246.68:20087/api/minio/download?fileName="+containerInfo.getFilename()};
            Process process=Runtime.getRuntime().exec(cmds);
            new CleanInputCache(process.getInputStream(),"INFO").start();
            new CleanInputCache(process.getErrorStream(),"ERROR").start();
            process.waitFor();
            BufferedReader r = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String s;
            while ((s = r.readLine())!=null) {
                System.out.println( s );
            }
            r.close();
        }catch ( IOException ioe ) { ioe.printStackTrace(); }
        catch ( InterruptedException ie ) { ie.printStackTrace(); }

        ExposedPort tcppublic = ExposedPort.tcp(containerInfo.getPrivatePort());
        Ports portBindings = new Ports();
        //portBindings.bind(tcp22, Ports.Binding.bindPort(11022));
        portBindings.bind(tcppublic, Ports.Binding.bindPort(containerInfo.getPublicPort()));
        CreateContainerResponse containerResponse = dockerClient.createContainerCmd(containerInfo.getImgName())
                .withName(containerInfo.getContainerName())
                //.withImage(containerInfo.getImgName())s
                .withExposedPorts(tcppublic)
                .withPortBindings(portBindings)
                .withBinds(Bind.parse("/root/fin/"+containerInfo.getUserid()+"_"+tim+":/usr/jar"))
                .withCmd("bash", "-c", "java -jar /usr/jar/"+containerInfo.getFilename())
                //.withBinds(Bind.parse("/root/minio:/usr/jar"))
                //.withCmd("bash", "-c", "java -jar /usr/jar/demo-0.0.1-SNAPSHOT.jar")
                .exec();
        System.out.println(containerResponse.toString());
        dockerClient.startContainerCmd(containerResponse.getId()).exec();
        List<Container> containers = dockerClient.listContainersCmd().exec();
        boolean exist=false;
        for(Container container:containers){
            if(container.getId().equals(containerResponse.getId()))exist=true;
        }
        if(exist){
            UserContainer userContainer=new UserContainer();
            userContainer.setId(Integer.MAX_VALUE);
            userContainer.setContainerid(containerResponse.getId().substring(0,12));
            userContainer.setUserid(containerInfo.getUserid());
            userContainer.setImagename(containerInfo.getImgName());
            System.out.println(userContainer.getContainerid()+" "+userContainer.getUserid()+" "+userContainer.getImagename()+" "+userContainer.getId());
            userContainerService.save(userContainer);
            res.setMsg("创建容器成功");
            res.setState(true);
            return res;
        }else{
            res.setMsg("创建容器失败，请检查端口，镜像等信息是否有误");
            res.setState(false);
            return res;
        }
        // 创建命令
//        ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(containerResponse.getId())
//                .withAttachStdout(true)
//                .withAttachStderr(true)
//                .withCmd("bash", "-c", "ls") //当前目录下列出所有文件
//                .exec();
//
//        // 执行命令
//        dockerClient.execStartCmd(execCreateCmdResponse.getId()).exec(
//                new ExecStartResultCallback(System.out, System.err));
    }

}

//    public CreateContainerResponse createContainers(DockerClient client,String containerName,String imageName){//映射端口8088—>80
//
//        ExposedPort tcp80 = ExposedPort.tcp(80);
//
//        Ports portBindings= newPorts();
//
//        portBindings.bind(tcp80, Ports.Binding.bindPort(8088));
//
//        CreateContainerResponse container=client.createContainerCmd(imageName)
//
//                .withName(containerName)
//
//                .withHostConfig(newHostConfig().withPortBindings(portBindings))
//
//                .withExposedPorts(tcp80).exec();returncontainer;
//
//        PortBinding portBinding = PortBinding.parse(containerInfo.getPublicPort() + ":" + containerInfo.getPrivatePort());
//        HostConfig hostConfig = HostConfig.newHostConfig()
//                .withPortBindings(portBinding);
//        CreateContainerResponse response = dockerClient.createContainerCmd(containerInfo.getImage())
//                .withName(containerInfo.getName())
//                .withHostConfig(hostConfig)
//                .withExposedPorts(ExposedPort.parse(containerInfo.getPrivatePort() + "/tcp"))
//                .exec();
//
//    }