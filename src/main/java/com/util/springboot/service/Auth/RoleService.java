package com.util.springboot.service.auth;

import com.util.springboot.model.Role;
import java.util.List;


public interface RoleService {

    List<Role> getUserRoles(Integer userId);

}
