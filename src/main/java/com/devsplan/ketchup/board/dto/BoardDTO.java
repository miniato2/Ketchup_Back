package com.devsplan.ketchup.board.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@ToString
public class BoardDTO {

    private int boardNo;                        // 게시물 번호
    private String memberNo;                       // 사번
    private int departmentNo;                   // 부서번호
    private String boardTitle;                  // 게시물 제목
    private List<BoardFileDTO> boardFiles;       // 게시물 파일들
    private String boardContent;                // 게시물 내용
    private Timestamp boardCreateDttm;      // 게시글 등록일시
    private Timestamp boardUpdateDttm;      // 게시글 수정일시
    private String boardFilePath;

    public void setBoardCreateDttm(Timestamp timestamp) {
        this.boardCreateDttm = timestamp;
    }

    public void setBoardUpdateDttm(Timestamp timestamp) {
        this.boardUpdateDttm = timestamp;
    }

}
