package com.uzdz.bpm.repository;

import com.uzdz.bpm.domain.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SysUserRepository extends JpaRepository<SysUser, Integer> {

    Optional<SysUser> findByUsername(String username);
}
