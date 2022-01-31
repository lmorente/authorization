package com.security.book.repository;

import com.security.book.repository.entity.ERole;
import com.security.book.repository.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

  Optional<Role> findByName(ERole name);
}
