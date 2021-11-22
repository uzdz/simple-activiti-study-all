package com.uzdz.bpm.repository;

import com.uzdz.bpm.domain.SysUserRoleRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SysUserRoleRelationRepository extends JpaRepository<SysUserRoleRelation, Integer> {

    List<SysUserRoleRelation> findByUserId(Integer userId);
}
