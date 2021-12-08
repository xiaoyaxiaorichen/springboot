package com.util.springboot.service.auth;

import com.util.springboot.model.User;

import java.util.List;


public interface UserService {

    User findByUserName(String userName);

    /**
     * 添加一个用户
     * @param user 用户对象
     * @return 是否添加成功
     */
    void add(User user);

    /**
     * 查询所有用户
     * @return 返回用户列表
     */
    List<User> findAll(Integer offset, Integer limit);

}
