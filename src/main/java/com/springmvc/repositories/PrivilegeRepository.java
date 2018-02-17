package com.springmvc.repositories;

import com.springmvc.entity.Privilege;
import org.springframework.data.repository.CrudRepository;

public interface PrivilegeRepository extends CrudRepository<Privilege, Integer> {
}
