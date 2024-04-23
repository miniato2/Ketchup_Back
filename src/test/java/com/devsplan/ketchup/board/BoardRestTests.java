package com.devsplan.ketchup.board;

import com.devsplan.ketchup.board.dto.BoardDTO;
import com.devsplan.ketchup.board.dto.BoardFileDTO;
import com.devsplan.ketchup.board.entity.Board;
import com.devsplan.ketchup.board.entity.BoardFile;
import com.devsplan.ketchup.board.service.BoardService;
import com.devsplan.ketchup.member.DepartmentDTO;
import com.devsplan.ketchup.member.MemberDTO;
import com.devsplan.ketchup.util.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

@SpringBootTest
public class BoardRestTests {

    @Autowired
    private BoardService boardService;

    @Autowired
    private FileUtils fileUtils;


    private static Stream<Arguments> boardInfo() {
        return Stream.of(
                Arguments.of(
                        1,
                        "title",
                        Arrays.asList(
                                new BoardFileDTO(null, "filename0.txt", "text/plain", "File content 0", 10L),
                                new BoardFileDTO(null, "filename1.txt", "text/plain1", "File content 1", 12L),
                                new BoardFileDTO(null, "filename2.txt", "text/plain2", "File content 2", 3L)
                        ),
                        "content",
                        LocalDateTime.now()
                )
        );
    }
    @DisplayName("자료실 게시글 등록")
    @ParameterizedTest
    @MethodSource("boardInfo")
    void insertBoard(int boardNo, String boardTitle, List<BoardFileDTO> boardFiles, String boardContent, LocalDateTime boardCreateDttm) {
        // given
//        MemberDTO memberDTO = new MemberDTO(1, "홍길동", 1);
//        DepartmentDTO departmentDTO = new DepartmentDTO(1, "영업부");

        boardFiles.set(0, new BoardFileDTO(null, "filename0.txt", "text/plain", "File content 0", 10L));
        boardFiles.set(1, new BoardFileDTO(null, "filename1.txt", "text/plain", "File content 1", 12L));
        boardFiles.set(2, new BoardFileDTO(null, "filename2.txt", "text/plain", "File content 2", 15L));

        BoardDTO boardDTO = new BoardDTO(
            boardNo,
            boardTitle,
            boardFiles,
            boardContent,
            boardCreateDttm
        );
        boardDTO.setMemberNo(1);
        boardDTO.setDepartmentNo(1);

        // when
        // then
        Assertions.assertDoesNotThrow(
                () -> boardService.insertBoard(boardDTO));

    }

    @DisplayName("자료실 게시글 목록조회")
    @Test
    void selectBoardList() {
//        /boards?departmentno={departmentno}&title={title}&page={pageno}
        //given
        //when
        List<BoardDTO> boardList = boardService.selectBoardList();
        //then
        Assertions.assertNotNull(boardList);
        boardList.forEach(System.out::println);
    }

    @DisplayName("자료실 게시글 검색조회")
    @Test
    void selectBoardSearchList() {
        //given
        //when
        //then
    }

    @DisplayName("자료실 게시글 상세조회")
    @Test
    void selectBoardDetail() {
        //given
        //when
        //then
    }



    @DisplayName("자료실 게시글 수정")
    @Test
    void updateBoard() {
        //given
        //when
        //then
    }

    @DisplayName("자료실 게시글 삭제")
    @Test
    void deleteBoard() {
        //given
        //when
        //then
    }
}
