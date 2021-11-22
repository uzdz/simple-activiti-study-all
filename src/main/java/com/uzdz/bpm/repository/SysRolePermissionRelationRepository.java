package com.uzdz.bpm.repository;

import com.uzdz.bpm.domain.SysRolePermissionRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SysRolePermissionRelationRepository extends JpaRepository<SysRolePermissionRelation, Integer> {

    List<SysRolePermissionRelation> findByRoleIdIn(List<Integer> ids);
}
