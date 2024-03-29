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
public class UserMinio {
    @Id
    private String filename;

    private Integer userid;

    private String pastname;

    private Integer size;


    public Integer getUserid() { return userid; }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPastname() {
        return pastname;
    }

    public void setPastname(String pastname) {
        this.pastname = pastname;
    }

}
