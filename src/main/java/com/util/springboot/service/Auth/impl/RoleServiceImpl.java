package com.luckyhua.springboot.service.auth.impl;

import com.luckyhua.springboot.dao.mapper.RoleMapper;
import com.luckyhua.springboot.dao.mapper.UserRoleMapper;
import com.luckyhua.springboot.model.Role;
import com.luckyhua.springboot.model.UserRole;
import com.luckyhua.springboot.model.UserRoleExample;
import com.luckyhua.springboot.service.auth.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author luckyhua
 * @date 2016/11/25
 * @description
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Role> getUserRoles(Integer userId) {
        UserRoleExample userRoleExample = new UserRoleExample();
        userRoleExample.createCriteria().andUserIdEqualTo(userId);
        List<UserRole> userRoles = userRoleMapper.selectByExample(userRoleExample);
        Set<Integer> roleIdsSet = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toSet());
        return roleIdsSet.stream().map(id -> roleMapper.selectByPrimaryKey(id)).collect(Collectors.toList());
    }

}
