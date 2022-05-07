package com.example.demo.entity;

import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity // This tells Hibernate to make a table out of this class
@Component
public class ContainerInfo {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator = "native")
    private String imgName;
    private String containerName;
    private Integer cpuShares;
    private Long memory;
    private Integer userid;
    private int publicPort;
    private int privatePort;
    private String filename;

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getCpuShares() {
        return cpuShares;
    }

    public void setCpuShares(Integer cpuShares) {
        this.cpuShares = cpuShares;
    }

    public Long getMemory(){return memory;}

    public void setMemory(Long memory){this.memory=memory;}

    public int getPublicPort(){return publicPort;}

    public void setPublicPort(int publicPort){this.publicPort=publicPort;}

    public int getPrivatePort(){return privatePort;}

    public void setPrivatePort(int privatePort){this.privatePort=privatePort;}
}
