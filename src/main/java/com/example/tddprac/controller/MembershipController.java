package com.example.tddprac.controller;

import com.example.tddprac.dto.MembershipDetailResponse;
import com.example.tddprac.dto.MembershipRequest;
import com.example.tddprac.dto.MembershipAddResponse;
import com.example.tddprac.service.MembershipService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class MembershipController {

    private final MembershipService membershipService;

    private static final String USER_ID_HEADER = "X-USER-ID";

    public MembershipController(MembershipService membershipService) {
        this.membershipService = membershipService;
    }

    @PostMapping("/api/v1/memberships")
    public ResponseEntity<MembershipAddResponse> addMembership(
            @RequestHeader(USER_ID_HEADER) final String userId,
            @RequestBody @Valid final MembershipRequest membershipRequest){

        final MembershipAddResponse membershipResponse = membershipService.addMembership(userId,membershipRequest.getMembershipType(), membershipRequest.getPoint());

        return ResponseEntity.status(HttpStatus.CREATED).body(membershipResponse);
    }

    @GetMapping("/api/v1/memberships")
    public ResponseEntity<List<MembershipDetailResponse>> getMembershipList(
            @RequestHeader(USER_ID_HEADER) final String userId){

        return ResponseEntity.ok(membershipService.getMembershipList(userId));
    }

}
