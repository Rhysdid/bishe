package com.example.demo.entity;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @Author 海龟
 * @Date 2021/8/21 17:04
 * @Desc MySQLJavaBean
 */
@Entity
@ApiModel
@Component
public class UserSql {
    @Id
    private Integer userid;
    private String sqlpassword;
    private Integer publicport;
    private String containerid;


    public Integer getUserid() { return userid; }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getSqlpassword() {
        return sqlpassword;
    }

    public void setSqlpassword(String sqlpassword) {
        this.sqlpassword = sqlpassword;
    }

    public Integer getPublicport() {
        return publicport;
    }

    public void setPublicport(Integer publicport) {
        this.publicport = publicport;
    }

    public String getContainerid() {
        return containerid;
    }

    public void setContainerid(String containerid) {
        this.containerid = containerid;
    }

}