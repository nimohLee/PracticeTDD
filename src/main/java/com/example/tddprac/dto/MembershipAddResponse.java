package com.example.tddprac.dto;

import com.example.tddprac.enums.MembershipType;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
public class MembershipAddResponse {
    private final Long id;
    private final MembershipType membershipType;
}
