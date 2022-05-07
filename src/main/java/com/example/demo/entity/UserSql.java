package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @Author 海龟
 * @Date 2021/8/21 17:04
 * @Desc MySQLJavaBean
 */
@Entity
@Data //包含了get，set和toString
@AllArgsConstructor //有参构造器 set
@NoArgsConstructor  //无参构造器 get
public class UserSql {
    @Id
    private Integer userid;
    private String sqlname;
    private String sqlpassword;
}