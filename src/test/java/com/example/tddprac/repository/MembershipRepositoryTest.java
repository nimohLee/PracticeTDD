package com.example.tddprac.repository;

import com.example.tddprac.domain.Membership;
import com.example.tddprac.dto.MembershipDetailResponse;
import com.example.tddprac.enums.MembershipType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// @DataJpaTest : JPA Repository들에 대한 빈들을 등록하여 단위 테스트의 작성을 용이하게 함
@DataJpaTest
public class MembershipRepositoryTest {

    @Autowired
    private MembershipRepository membershipRepository;

    @Test
    public void 멤버십등록() {
        //given
        // entity builder
        final Membership membership = Membership.builder()
                .userId("userId").membershipType(MembershipType.NAVER)
                .point(10000)
                .build();

        //when
        membershipRepository.save(membership);
        final Membership findResult = membershipRepository.findByUserIdAndMembershipType("userId",MembershipType.NAVER);
        //then
        assertThat(findResult.getId()).isNotNull();
        assertThat(findResult.getUserId()).isEqualTo("userId");
        assertThat(findResult.getMembershipType()).isEqualTo(MembershipType.NAVER);
        assertThat(findResult.getPoint()).isEqualTo(10000);
    }

    @Test
    public void 멤버십조회_사이즈가0(){
        //given

        //when
        List<Membership> result = membershipRepository.findAllByUserId("userid");
        //then
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void 멤버십조회_사이즈가2(){
        //given
        final Membership naverMembership = Membership.builder()
                .userId("userId")
                .membershipType(MembershipType.NAVER)
                .point(10000)
                .build();

        final Membership kakaoMembership = Membership.builder()
                .userId("userId")
                .membershipType(MembershipType.KAKAO)
                .point(10000)
                .build();

        membershipRepository.save(naverMembership);
        membershipRepository.save(kakaoMembership);
        //when
        List<Membership> result = membershipRepository.findAllByUserId("userId");
        //then
        assertThat(result.size()).isEqualTo(2);
    }
}
