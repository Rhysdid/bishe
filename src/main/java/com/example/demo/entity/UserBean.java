package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author 海龟
 * @Date 2021/8/21 17:04
 * @Desc MySQLJavaBean
 */
@Data //包含了get，set和toString
@AllArgsConstructor //有参构造器 set
@NoArgsConstructor  //无参构造器 get
public class UserBean {
    private String name;
    private String password;
}