package com.devsplan.ketchup.member;


import com.devsplan.ketchup.common.Authority;
import com.devsplan.ketchup.member.dto.DepDTO;
import com.devsplan.ketchup.member.dto.MemberDTO;
import com.devsplan.ketchup.member.dto.PositionDTO;
import com.devsplan.ketchup.member.service.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

@SpringBootTest
public class MemberRestTest {

    @Autowired
    private MemberService memberService;


    private static Stream<Arguments> getMember() {
        return Stream.of(
                Arguments.of(
                        new MemberDTO(
                                1,
                                "이후영",
                                "31558",
                                "01032283158",
                                "19940517",
                                'M',  // char로 수정
                                "서울시강동구암사동",
                                "yihooyung@gmail.com",
                                "yihooyung@ketchup.com",
                                new DepDTO(
                                        1,
                                        "개발팀",
                                        "이후영",
                                        15,
                                        'Y'  // char로 수정
                                ),
                                new PositionDTO(
                                        1,
                                        "팀장",
                                        3,
                                        Authority.LV1,
                                        'Y'  // char로 수정
                                ),
                                "계좌번호 01023",
                                'Y',  // char로 수정
                                "img_url"
                        )
                )
        );
    }

    private static Stream<Arguments> getDep() {
        return Stream.of(
                Arguments.of(
                                new DepDTO(
                                        1,
                                        "개발팀",
                                        "이후영",
                                        15,
                                        'Y'  // char로 수정
                                )
                        )
                );

    }

    private static Stream<Arguments> getPosition() {
        return Stream.of(
                Arguments.of(

                                new PositionDTO(
                                        1,
                                        "팀장",
                                        3,
                                   Authority.LV1,
                                        'Y'  // char로 수정
                                )

                        )
                );
    }




    @ParameterizedTest
    @MethodSource("getMember")
    void testInsertMember(MemberDTO member) {
        // given


        // then

        Assertions.assertDoesNotThrow(
                () -> {

                    memberService.insertMember(member);
                },
                "멤버 추가 중 예외가 발생했습니다."  // 예외 발생 시 메시지
        );



    }

    @ParameterizedTest
    @MethodSource("getDep")
    void testInsertDep(DepDTO dep) {

        Assertions.assertDoesNotThrow(
                () -> {

                    memberService.insertDep(dep);
                },
                "부서 추가 중 예외가 발생했습니다."  // 예외 발생 시 메시지
        );


    }

    @ParameterizedTest
    @MethodSource("getPosition")
    void testInsertPosition(PositionDTO positionDTO) {
        // given



       //then
        Assertions.assertDoesNotThrow(
                () -> {

                    memberService.insertPosition(positionDTO);
                },
                "직급 추가 중 예외가 발생했습니다."
        );


    }







}
