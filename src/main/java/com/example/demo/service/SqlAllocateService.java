package com.example.demo.service;

import com.example.demo.entity.ResState;
import com.example.demo.entity.User;
import com.example.demo.entity.UserSql;
import com.example.demo.repository.UserSqlRepository;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.jaxrs.JerseyDockerCmdExecFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class SqlAllocateService {
    @Autowired //是用在JavaBean中的注解，通过byType形式，用来给指定的字段或方法注入所需的外部资源。
    private JdbcTemplate jdbcTemplate; //jdbc连接工具类
    @Autowired
    private UserSqlRepository userSqlRepository;

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

    public ResState creatmysql(UserSql userSql) {
        ResState res=new ResState();
        List<UserSql> list=userSqlRepository.findByUserid(userSql.getUserid());
        if(list.size()!=0){
            res.setMsg("您已创建过sql服务，请自行登录");
            res.setState(false);
            return res;
        }
        ExposedPort tcppublic = ExposedPort.tcp(3306);
        Ports portBindings = new Ports();
        //portBindings.bind(tcp22, Ports.Binding.bindPort(11022));
        portBindings.bind(tcppublic, Ports.Binding.bindPort(userSql.getPublicport()));
        String tim= String.valueOf(System.currentTimeMillis());
        CreateContainerResponse containerResponse = dockerClient.createContainerCmd("mysql:latest")
                .withName(tim)
                .withEnv("MYSQL_ROOT_PASSWORD="+userSql.getSqlpassword())
                .withExposedPorts(tcppublic)
                .withPortBindings(portBindings)
                .exec();
        System.out.println(containerResponse.toString());
        dockerClient.startContainerCmd(containerResponse.getId()).exec();
        UserSql newuserSql=new UserSql();
        newuserSql.setSqlpassword(userSql.getSqlpassword());
        newuserSql.setPublicport(userSql.getPublicport());
        newuserSql.setUserid(userSql.getUserid());
        newuserSql.setContainerid(containerResponse.getId().substring(0,12));
        userSqlRepository.save(newuserSql);
        res.setMsg("创建成功");
        res.setState(true);
        return res;
    }

    public Iterable<UserSql> showmysql(User user) {
        Iterable<UserSql> list=userSqlRepository.findAll();
        return list;
    }

    @Transactional
    public ResState deletemysql(UserSql userSql) {
        ResState res=new ResState();
        List<UserSql> list=userSqlRepository.findByUserid(userSql.getUserid());
        if(list.size()==0){
            res.setMsg("不存在该用户或sql资源");
            res.setState(false);
            return res;
        }
        dockerClient.stopContainerCmd(userSql.getContainerid());
        dockerClient.removeContainerCmd(userSql.getContainerid());
        userSqlRepository.deleteByUserid(userSql.getUserid());
        res.setMsg("删除成功");
        res.setState(true);
        return res;
    }

}
