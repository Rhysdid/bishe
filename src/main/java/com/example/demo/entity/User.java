package com.example.demo.entity;

import io.swagger.annotations.ApiModel;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@ApiModel
@Component
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY, generator = "native")
    private Integer userid;

    private String password;

    private String status;

//    public List<UserContainer> toList(Optional<UserContainer> option) {
//        return option.
//                map(Collections::singletonList).
//                orElse(Collections.emptyList());
//    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getPassword() { return password; }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
