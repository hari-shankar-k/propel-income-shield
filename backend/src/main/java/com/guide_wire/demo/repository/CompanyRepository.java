package com.guide_wire.demo.repository;

import com.guide_wire.demo.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByCompanyCode(String companyCode);
}