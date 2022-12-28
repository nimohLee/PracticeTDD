package com.example.tddprac.controller;

import com.example.tddprac.dto.MembershipDetailResponse;
import com.example.tddprac.dto.MembershipRequest;
import com.example.tddprac.dto.MembershipAddResponse;
import com.example.tddprac.enums.MembershipType;
import com.example.tddprac.errors.GlobalExceptionHandler;
import com.example.tddprac.errors.MembershipErrorResult;
import com.example.tddprac.errors.MembershipException;
import com.example.tddprac.service.MembershipService;
import com.google.gson.Gson;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Stream;

import static com.example.tddprac.config.MembershipConstants.USER_ID_HEADER;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MembershipControllerTest {

    @InjectMocks
    private MembershipController membershipController;

    @Mock
    private MembershipService membershipService;

    private MockMvc mockMvc;
    private Gson gson;


    @BeforeEach
    public void init() {
        gson = new Gson();
        mockMvc = MockMvcBuilders.standaloneSetup(membershipController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void 멤버십등록실패_사용자식별값이헤더에없음() throws Exception {
        //given
        final String url = "/api/v1/memberships";
        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(membershipRequest(10000, MembershipType.NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        resultActions.andExpect(status().isBadRequest());
    }

    private MembershipRequest membershipRequest(final Integer point, final MembershipType membershipType){
        return MembershipRequest.builder()
                .point(point)
                .membershipType(membershipType)
                .build();
    }

    // 파라미터 값만 변경되는 테스트일 경우 합칠 수 있음
    @ParameterizedTest
    @MethodSource("invalidMembershipAddParameter")
    public void 멤버십등록실패_잘못된파라미터(final Integer point, final MembershipType membershipType) throws Exception{
        //given
        final String url = "/api/v1/memberships";

        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "12345")
                        .content(gson.toJson(membershipRequest(point, membershipType)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> invalidMembershipAddParameter() {
        return Stream.of(
                Arguments.of(null, MembershipType.NAVER),
                Arguments.of(-1,MembershipType.NAVER),
                Arguments.of(10000,null)
        );
    }
//
//    @Test
//    public void 멤버십등록실패_포인트가null() throws Exception {
//        //given
//        final String url = "/api/v1/memberships";
//
//        //when
//        final ResultActions resultActions = mockMvc.perform(
//                MockMvcRequestBuilders.post(url)
//                        .header(USER_ID_HEADER, "12345")
//                        .content(gson.toJson(membershipRequest(null,MembershipType.NAVER)))
//                        .contentType(MediaType.APPLICATION_JSON)
//        );
//        //then
//        resultActions.andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void 멤버십등록실패_포인트가음수() throws Exception {
//        //given
//        final String url = "/api/v1/memberships";
//
//        //when
//        final ResultActions resultActions = mockMvc.perform(
//                MockMvcRequestBuilders.post(url)
//                        .header(USER_ID_HEADER,"12345")
//                        .content(gson.toJson(membershipRequest(-1,MembershipType.NAVER)))
//                        .contentType(MediaType.APPLICATION_JSON)
//        );
//
//        //then
//        resultActions.andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void 멤버십등록실패_멤버십종류거Null() throws Exception {
//        //given
//        final String url = "/api/v1/memberships";
//
//        //when
//        final ResultActions resultActions = mockMvc.perform(
//                MockMvcRequestBuilders.post(url)
//                        .header(USER_ID_HEADER,"12345")
//                        .content(gson.toJson(membershipRequest(10000,null)))
//                        .contentType(MediaType.APPLICATION_JSON)
//        );
//
//        //then
//        resultActions.andExpect(status().isBadRequest());
//    }

    @Test
    public void 멤버십등록실패_MemberService에서Throw() throws Exception {
        //given
        final String url = "/api/v1/memberships";
        doThrow(new MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER))
                .when(membershipService)
                .addMembership("12345",MembershipType.NAVER,10000);
        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER,"12345")
                        .content(gson.toJson(membershipRequest(10000,MembershipType.NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 멤버십등록성공() throws Exception {
        //given
        final String url = "/api/v1/memberships";
        final MembershipAddResponse membershipResponse = MembershipAddResponse.builder()
                .id(-1L)
                .membershipType(MembershipType.NAVER).build();

        doReturn(membershipResponse).when(membershipService).addMembership("12345",MembershipType.NAVER, 10000);

        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "12345")
                        .content(gson.toJson(membershipRequest(10000, MembershipType.NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        resultActions.andExpect(status().isCreated());

        final MembershipAddResponse response = gson.fromJson(resultActions.andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), MembershipAddResponse.class);

        Assertions.assertThat(response.getMembershipType()).isEqualTo(MembershipType.NAVER);
        Assertions.assertThat(response.getId()).isNotNull();
    }

    @Test
    public void 멤버십목록조회실패_사용자식별값이헤더에없음() throws Exception {
        //given
        final String url = "/api/v1/memberships";

        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                //헤더 없음
        );

        //then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 멤버십목록조회성공() throws Exception {
        //given
        final String url = "/api/v1/memberships";
        doReturn(Arrays.asList(
                MembershipDetailResponse.builder().build(),
                MembershipDetailResponse.builder().build(),
                MembershipDetailResponse.builder().build()
        )).when(membershipService).getMembershipList("12345");

        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .header(USER_ID_HEADER,"12345")
        );

        //then
        resultActions.andExpect(status().isOk());
    }
}
