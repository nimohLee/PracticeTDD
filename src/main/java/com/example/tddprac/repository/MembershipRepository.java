package com.example.tddprac.repository;

import com.example.tddprac.domain.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
}
