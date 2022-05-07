
package com.example.demo;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.jaxrs.JerseyDockerCmdExecFactory;
import jnr.unixsocket.UnixSocket;
import jnr.unixsocket.UnixSocketAddress;
import jnr.unixsocket.UnixSocketChannel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.*;

@SpringBootApplication
@RestController
public class DemoApplication {


	public static void main(String[] args) throws IOException {
//		DockerClient dockerClient = DockerClientBuilder.getInstance("tcp://124.221.246.68:2375").build();
//		Info info = dockerClient.infoCmd().exec();
//		System.out.println("芜湖");
//		System.out.println(info);
//		System.out.println("起飞");

//		DockerClient dockerClient = DockerClientBuilder.getInstance("tcp://124.221.246.68:2375").build();
//		List<Image> images = dockerClient.listImagesCmd().exec();
//		System.out.println("芜湖");
//		for(int i=0; i < images.size(); i++){
//			System.out.println(images.get(i));
//		}
//		System.out.println("起飞");

		SpringApplication.run(DemoApplication.class, args);

	}



}
            