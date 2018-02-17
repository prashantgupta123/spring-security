package com.springmvc.repositories;

import com.springmvc.entity.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Integer> {
    Role findByAuthority(String authority);
}
