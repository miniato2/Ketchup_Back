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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    public String insertBoard(BoardDTO boardDTO) {
        log.info("[BoardService] insertBoard Start ===================");

        try {

            // 게시글 엔티티 생성
            Board board = modelMapper.map(boardDTO, Board.class);

           /* // 파일업로드
            List<BoardFile> boardFiles = new ArrayList<>();
            if (boardDTO.getBoardFile() != null && !boardDTO.getBoardFile().isEmpty()) {
                for (BoardFileDTO fileDTO : boardDTO.getBoardFile()) {
                    MultipartFile file = fileDTO.getFile();
                    String savedFilePath = fileUtils.saveFile(IMAGE_DIR, file.getOriginalFilename(), file);

                    BoardFile boardFile = BoardFile.builder()
                            .boardNo(board.getBoardNo()) // 게시글 번호 설정
                            .boardFileName(file.getOriginalFilename())
                            .boardFilePath(savedFilePath)
                            .boardOriginName(file.getOriginalFilename())
                            .boardFileSize(file.getSize())
                            .build();

                    boardFiles.add(boardFile);
                }
                board.boardFiles(boardFiles);
            }*/


            // 게시글 저장
            boardRepository.save(board);
            log.info("[BoardService] insertBoard End ===================");

            return "성공";
        } catch (Exception e) {
            log.error("Failed to insert board", e);
            return "실패";
        }

    }


    /* 부서별 자료실 게시물 목록조회 & 페이징 */
    public Page<BoardDTO> selectBoardList(int departmentNo, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("boardNo").descending());

        Page<Board> boardList = boardRepository.findByDepartmentNo(departmentNo, pageable);

//        return boardList.stream().map(board -> modelMapper.map(board, BoardDTO.class)).collect(Collectors.toList());
        return boardList.map(board -> modelMapper.map(board, BoardDTO.class));
    }

    /* 부서별 자료실 게시물 목록 검색조회 & 페이징 */
    public Page<BoardDTO> selectBoardSearchList(int departmentNo, int page, int size, String title) {

        return null;
    }
}
