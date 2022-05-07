package com.example.demo.service;

import com.example.demo.entity.ResState;
import com.example.demo.entity.UserSql;
import com.example.demo.repository.UserSqlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class SqlAllocate {
    @Autowired //是用在JavaBean中的注解，通过byType形式，用来给指定的字段或方法注入所需的外部资源。
    private JdbcTemplate jdbcTemplate; //jdbc连接工具类
    @Autowired
    private UserSqlRepository userSqlRepository;

    public ResState creatuser(UserSql userSql) {
        String sql = "create user ?@'%' IDENTIFIED BY ?;";
        ResState res=new ResState();
        Object[] params = {userSql.getSqlname(),userSql.getSqlpassword()};
        int s=jdbcTemplate.update(sql,params);
        userSqlRepository.save(userSql);
        res.setMsg("创建成功");
        res.setState(true);
        return res;
    }

    public ResState giveChomd(UserSql userSql) {
        String sql = "GRANT SELECT, INSERT, UPDATE,DELETE ON bishe.* TO ?@'%';";
        ResState res=new ResState();
        Object[] params = {userSql.getSqlname()};
        boolean i=jdbcTemplate.update(sql,params)>0;
        res.setMsg("授权成功");
        res.setState(true);
        return res;
    }

    public ResState dropusr(UserSql userSql) {
        String sql = "DROP USER ?;";
        ResState res=new ResState();
        Object[] params = {userSql.getSqlname()};
        boolean i=jdbcTemplate.update(sql,params)>0;
        res.setMsg("删除用户成功");
        res.setState(true);
        return res;
    }
}
