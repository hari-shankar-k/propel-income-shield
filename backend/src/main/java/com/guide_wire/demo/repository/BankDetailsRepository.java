package com.guide_wire.demo.repository;

import com.guide_wire.demo.entity.BankDetails;
import com.guide_wire.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankDetailsRepository extends JpaRepository<BankDetails, Long> {

    Optional<BankDetails> findByUser(User user);
}