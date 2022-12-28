package com.example.tddprac.repository;

import com.example.tddprac.domain.Membership;
import com.example.tddprac.dto.MembershipDetailResponse;
import com.example.tddprac.enums.MembershipType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
    Membership findByUserIdAndMembershipType(final String userId, final MembershipType membershipType);

    List<Membership> findAllByUserId(final String userId);


}
