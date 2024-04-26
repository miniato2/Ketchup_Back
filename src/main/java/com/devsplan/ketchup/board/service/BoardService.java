package com.devsplan.ketchup.board.service;

import com.devsplan.ketchup.board.dto.BoardDTO;
import com.devsplan.ketchup.board.dto.BoardFileDTO;
import com.devsplan.ketchup.board.entity.Board;
import com.devsplan.ketchup.board.entity.BoardFile;
import com.devsplan.ketchup.board.repository.BoardFileRepository;
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

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;
    private final ModelMapper modelMapper;
    private final FileUtils fileUtils;

    /* 이미지 저장 할 위치 및 응답 할 이미지 주소 */
    @Value("${image.image-dir}")
    private String IMAGE_DIR;

    @Value("${image.image-url}")
    private String IMAGE_URL;

    public BoardService(BoardRepository boardRepository, ModelMapper modelMapper, FileUtils fileUtils, BoardFileRepository boardFileRepository) {
        this.boardRepository = boardRepository;
        this.boardFileRepository = boardFileRepository;
        this.modelMapper = modelMapper;
        this.fileUtils = fileUtils;
    }

    /* 부서별 자료실 게시물 등록 */
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

    /* 부서별 자료실 게시물 목록조회 & 페이징 & 목록 제목검색 조회 */
    public Page<BoardDTO> selectBoardList(int departmentNo, Pageable pageable, String title) {

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1,
                pageable.getPageSize(),
                Sort.by("boardNo").descending());

        Page<Board> boardList;

        if (title != null && !title.isEmpty()) {
            boardList = boardRepository.findByDepartmentNoAndBoardTitleContaining(departmentNo,  pageable, title);

        } else {
            boardList = boardRepository.findByDepartmentNo(departmentNo, pageable);
        }

//        return boardList.stream().map(board -> modelMapper.map(board, BoardDTO.class)).collect(Collectors.toList());
        return boardList.map(board -> modelMapper.map(board, BoardDTO.class));
    }


    /* 부서별 자료실 게시물 상세조회 */
    public BoardDTO selectBoardDetail(int boardNo) {

        Board foundBoard = boardRepository.findById(boardNo).orElseThrow(IllegalArgumentException::new);

        return modelMapper.map(foundBoard, BoardDTO.class);
    }

    /* 부서별 자료실 게시물 삭제 */
    @Transactional
    public void deleteBoard(int boardNo, int memberNo) {
        Board board = boardRepository.findById(boardNo).orElseThrow(IllegalArgumentException::new);

        if(board.getMemberNo() == memberNo) {
            boardRepository.delete(board);
        } else {
            System.out.println("삭제 에러,,,");
        }

    }


    public Map<String, Object> insertBoardWithFile(BoardDTO boardDTO, List<MultipartFile> files) throws IOException {

        log.info("boardDTO : " + boardDTO);

        Map<String, Object> result = new HashMap<>();

        try {
            // 게시물 저장
            Board board = modelMapper.map(boardDTO, Board.class);
            Board savedBoard = boardRepository.save(board);

            // 파일이 있으면 각 파일을 저장하고, BoardFile 엔티티를 생성하여 연결
            if (files != null) {
                for (MultipartFile file : files) {
                    String fileName = file.getOriginalFilename();
                    String fileType = file.getContentType();
                    String savedFilePath = fileUtils.saveFile(IMAGE_DIR, fileName, file);

                    // 파일을 저장할 디렉토리 생성 (만약 디렉토리가 없다면)
                    File uploadDir = new File(IMAGE_DIR);
                    if (!uploadDir.exists()) {
                        uploadDir.mkdirs();
                    }

                    File newFile = new File(savedFilePath);
                    file.transferTo(newFile);

                    BoardFileDTO boardFileDTO = new BoardFileDTO();
                    boardFileDTO.setBoardNo(savedBoard.getBoardNo());
                    boardFileDTO.setBoardFileName(fileName);
                    boardFileDTO.setBoardFilePath(savedFilePath);
                    boardFileDTO.setBoardOriginName(fileName);
                    boardFileDTO.setBoardFileSize(file.getSize());
                    boardFileDTO.setFileType(fileType);

                    BoardFile boardFile = modelMapper.map(boardFileDTO, BoardFile.class);
                    boardFileRepository.save(boardFile);
                }
            }

            result.put("result", true);
        } catch (Exception e) {
            log.error("Error while inserting Announce with Files: " + e.getMessage());
            result.put("result", false);
        }

        return result;

    }

    /* 부서별 자료실 게시물 수정 */
    public String updateBoard(int boardNo, BoardDTO boardInfo, int memberNo) {

        Board foundBoard = boardRepository.findById(boardNo).orElseThrow(IllegalArgumentException::new);

        if(foundBoard.getMemberNo() == memberNo) {

            foundBoard.boardTitle(boardInfo.getBoardTitle());
            foundBoard.boardContent(boardInfo.getBoardContent());
            foundBoard.boardUpdateDttm(new Timestamp(System.currentTimeMillis()));

            boardRepository.save(foundBoard);
            return "성공";

        } else {
            return "실패";
        }
    }
}
