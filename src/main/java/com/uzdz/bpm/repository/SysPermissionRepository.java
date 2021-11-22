package com.uzdz.bpm.repository;

import com.uzdz.bpm.domain.SysPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SysPermissionRepository extends JpaRepository<SysPermission, Integer> {

    List<SysPermission> findByIdIn(List<Integer> ids);
}
