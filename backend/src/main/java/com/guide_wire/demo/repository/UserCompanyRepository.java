package com.guide_wire.demo.repository;

import com.guide_wire.demo.entity.User;
import com.guide_wire.demo.entity.UserCompany;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCompanyRepository extends JpaRepository<UserCompany, Long> {

    Optional<UserCompany> findByUser(User user);
}