package com.devsplan.ketchup.board.service;

import com.devsplan.ketchup.board.dto.BoardDTO;
import com.devsplan.ketchup.board.dto.BoardFileDTO;
import com.devsplan.ketchup.board.entity.Board;
import com.devsplan.ketchup.board.entity.BoardFile;
import com.devsplan.ketchup.board.repository.BoardRepository;
import com.devsplan.ketchup.util.FileUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;
    private final ModelMapper modelMapper;
    private final FileUtils fileUtils;

    /* 이미지 저장 할 위치 및 응답 할 이미지 주소 */
    @Value("${image.image-dir}")
    private String IMAGE_DIR;

    @Value("${image.image-url}")
    private String IMAGE_URL;

    public BoardService(BoardRepository boardRepository, ModelMapper modelMapper, FileUtils fileUtils) {
        this.boardRepository = boardRepository;
        this.modelMapper = modelMapper;
        this.fileUtils = fileUtils;
    }

    @Transactional
    public Object insertBoard(BoardDTO boardDTO) {
        log.info("[BoardService] insertBoard Start ===================");

        int result = 0;
        try {
            // 게시글 엔티티 생성
            Board board = Board.builder()
                    .boardNo(boardDTO.getBoardNo())
                    .memberNo(boardDTO.getMemberNo())
                    .departmentNo(boardDTO.getDepartmentNo())
                    .boardTitle(boardDTO.getBoardTitle())
                    .boardContent(boardDTO.getBoardContent())
                    .boardCreateDttm(boardDTO.getBoardCreateDttm())
                    .build();

            // 게시글과 파일 엔티티 관계 설정
//            List<BoardFileDTO> boardFileDTOs = boardDTO.getBoardFile();
//            if (boardFileDTOs != null && !boardFileDTOs.isEmpty()) {
//                List<BoardFile> boardFiles = new ArrayList<>();
//                for (BoardFileDTO fileDTO : boardFileDTOs) {
//                    // 파일 업로드 및 엔티티 생성
////                    String uploadedFileName = fileUtils.saveFile("upload-dir", "testFile", MediaType.IMAGE_PNG_VALUE);
//                    BoardFile boardFile = new BoardFile(
//                        fileDTO.getBoardFileNo(),
//                        fileDTO.getBoardFileName(),
//                        null,
//                        fileDTO.getBoardOrigName(),
//                        fileDTO.getBoardFileSize()
//                    );
//                    boardFiles.add(boardFile);
//                }
//                board.boardFiles(boardFiles);
//            }

            // 게시글 저장
            boardRepository.save(modelMapper.map(board, Board.class));
            log.info("[BoardService] insertBoard End ===================");

            result = 1;
        } catch (Exception e) {
            log.error("Failed to insert board", e);
        }

        return (result > 0) ? "성공" : "실패";
    }


    public List<BoardDTO> selectBoardList() {

        List<Board> boardList = boardRepository.findAll(Sort.by("boardNo").descending());
        return boardList.stream().map(board -> modelMapper.map(board, BoardDTO.class)).collect(Collectors.toList());
    }
}
