package com.example.demo.service;

import com.example.demo.entity.ResState;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public ResState login(User user) {
        ResState res=new ResState();
        User get=userRepository.findByuserid(user.getUserid());
        if(get==null){
            res.setMsg("不存在该用户");
            res.setState(false);
            return res;
        }
        System.out.println(get.getPassword()+"+"+user.getPassword()+"+"+get.getPassword().equals(user.getPassword()));
        if(!get.getPassword().equals(user.getPassword())){
            res.setMsg("密码错误");
            res.setState(false);
            return res;
        }else{
            res.setMsg("登录成功");
            res.setState(true);
            return res;
        }
    }
}
