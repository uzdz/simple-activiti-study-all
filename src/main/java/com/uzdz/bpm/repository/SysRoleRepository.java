package com.uzdz.bpm.repository;

import com.uzdz.bpm.domain.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SysRoleRepository extends JpaRepository<SysRole, Integer> {

    List<SysRole> findByIdIn(List<Integer> ids);
}
