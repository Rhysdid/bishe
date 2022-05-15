package com.example.demo.repository;


import com.example.demo.entity.UserContainer;
import com.example.demo.entity.UserMinio;
import com.example.demo.entity.UserSql;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
@Component
@Repository
public interface UserSqlRepository extends CrudRepository<UserSql, Integer> {
    public List<UserSql> findByUserid(Integer userid);
    public List<UserSql> deleteByUserid(Integer userid);
}
