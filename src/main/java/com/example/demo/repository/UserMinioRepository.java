package com.example.demo.repository;


import com.example.demo.entity.UserContainer;
import com.example.demo.entity.UserMinio;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
@Component
@Repository
public interface UserMinioRepository extends CrudRepository<UserMinio, Integer> {
    public List<UserMinio> findByUserid(Integer userid);
    public List<UserMinio> deleteByFilename(String filename);
}
