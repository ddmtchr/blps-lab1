package com.ddmtchr.blpslab1.repository;

import com.ddmtchr.blpslab1.entity.Estate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstateRepository extends JpaRepository<Estate, Long> {
}
