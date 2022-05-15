package com.example.demo.entity;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Entity
@ApiModel
@Component
public class UserContainer {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY, generator = "native")
    private Integer id;

    private Integer userid;
    private String containerid;
    private String imagename;
    private Integer cpu;
    private Long mem;

    public List<UserContainer> toList(Optional<UserContainer> option) {
        return option.
                map(Collections::singletonList).
                orElse(Collections.emptyList());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getCpu() {
        return cpu;
    }

    public void setCpu(Integer cpu) {
        this.cpu = cpu;
    }

    public Long getMem() {
        return mem;
    }

    public void setMem(Long mem) {
        this.mem = mem;
    }

    public String getContainerid() {
        return containerid;
    }

    public void setContainerid(String containerid) {
        this.containerid = containerid;
    }

    public String getImagename() {
        return imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }
}
