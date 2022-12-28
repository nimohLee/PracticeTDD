package com.example.tddprac.dto;

import com.example.tddprac.enums.MembershipType;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@Builder
public class MembershipDetailResponse {
    private final Long id;
    private final MembershipType membershipType;
    private final int point;
    private final LocalDateTime updatedAt;
    private final LocalDateTime createdAt;
}
