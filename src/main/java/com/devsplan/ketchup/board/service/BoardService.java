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
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.*;

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

    @Autowired
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
            boardDTO.setBoardCreateDttm(new Timestamp(System.currentTimeMillis()));
            Board board = modelMapper.map(boardDTO, Board.class);

            // 게시글 저장
            boardRepository.save(board);
            log.info("[BoardService] insertBoard End ===================");

            return "성공";
        } catch (Exception e) {
            log.error("Failed to insert board", e);
            return "실패";
        }

    }


    @Transactional
    public Map<String, Object> insertBoardWithFile(BoardDTO boardDTO, List<MultipartFile> files) throws IOException {

        log.info("boardDTO : " + boardDTO);
        Map<String, Object> result = new HashMap<>();

        try {
            boardDTO.setBoardCreateDttm(new Timestamp(System.currentTimeMillis()));
            Board savedBoard = modelMapper.map(boardDTO, Board.class);

            // 파일이 있으면 각 파일을 저장하고, BoardFile 엔티티를 생성하여 연결
            if (files != null && !files.isEmpty()) {
                for (MultipartFile file : files) {
                    String fileOriginName = UUID.randomUUID().toString().replace("-", "");
                    String fileName = file.getOriginalFilename();
                    String fileType = file.getContentType();
                    String savedFilePath = fileUtils.saveFile(IMAGE_DIR, fileName, file);
                    String filePath = savedFilePath + "\\" + fileName;

                    // 파일을 저장할 디렉토리 생성 (만약 디렉토리가 없다면)
                    File uploadDir = new File(IMAGE_DIR);
                    if (!uploadDir.exists()) {
                        uploadDir.mkdirs();
                    }

                    File newFile = new File(savedFilePath);
                    file.transferTo(newFile);

                    savedBoard.boardFilePath(filePath);
                    boardRepository.save(savedBoard);

                    BoardFileDTO boardFileDTO = new BoardFileDTO();
                    boardFileDTO.setBoardNo(savedBoard.getBoardNo());
                    boardFileDTO.setBoardFileName(fileName);
                    boardFileDTO.setBoardFilePath(filePath);
                    boardFileDTO.setBoardOriginName(fileOriginName);
                    boardFileDTO.setBoardFileSize(file.getSize());
                    boardFileDTO.setFileType(fileType);

                    BoardFile boardFile = modelMapper.map(boardFileDTO, BoardFile.class);
                    boardFileRepository.save(boardFile);
                }

                result.put("result", true);
            } else {
                // 파일이 없는 경우에 대한 처리
                log.info("첨부 파일이 없습니다.");
                result.put("result", false);
            }
        } catch (Exception e) {
            log.error("첨부파일 등록 실패: " + e.getMessage());
            result.put("result", false);
        }
        return result;
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
        return boardList.map(board -> modelMapper.map(board, BoardDTO.class));
    }

    /* 부서 전체 게시물 목록조회(권한자-대표) */
    public Page<BoardDTO> selectAllBoards(Pageable pageable, String title) {

        try {
            Page<Board> boardList;
            if (title != null && !title.isEmpty()) {
                boardList = boardRepository.findByBoardTitleContaining(pageable, title);
            } else {
                boardList = boardRepository.findAll(pageable);
            }

            return boardList.map(board -> modelMapper.map(board, BoardDTO.class));
        } catch (Exception e) {
            log.error("목록 조회 실패: ", e);
            throw new RuntimeException("목록 조회 실패");
        }
    }

    /* 부서별 자료실 게시물 상세조회 */
    public BoardDTO selectBoardDetail(int boardNo) {

        Board foundBoard = boardRepository.findById(boardNo).orElse(null);
        if (foundBoard == null) {
            return null; // 해당 게시물이 존재하지 않으면 null 반환
        }

        BoardDTO boardDTO = modelMapper.map(foundBoard, BoardDTO.class);

        // 게시물에 첨부된 파일 다운로드 링크 생성
        List<BoardFile> boardFiles = findBoardFilesByBoardNo(boardNo);
        List<BoardFileDTO> boardFileDTOs = new ArrayList<>();
        for (BoardFile boardFile : boardFiles) {
            BoardFileDTO boardFileDTO = modelMapper.map(boardFile, BoardFileDTO.class);

            // 파일의 다운로드 경로를 클라이언트에 전달
            boardFileDTO.setBoardFilePath(boardFile.getBoardFilePath());

            boardFileDTOs.add(boardFileDTO);
        }
        boardDTO.setBoardFiles(boardFileDTOs);

        return boardDTO;
    }

    public List<BoardFile> findBoardFilesByBoardNo(int boardNo) {
        return boardFileRepository.findByBoardNo(boardNo);
    }

    /* 부서별 자료실 게시물 수정 */
    @Transactional
    public String updateBoard(int boardNo, BoardDTO boardInfo, String memberNo) {

        try {
            Board foundBoard = boardRepository.findById(boardNo).orElseThrow(IllegalArgumentException::new);

            if(foundBoard.getMemberNo().equals(memberNo)) {

                foundBoard.boardTitle(boardInfo.getBoardTitle());
                foundBoard.boardContent(boardInfo.getBoardContent());
                foundBoard.boardUpdateDttm(new Timestamp(System.currentTimeMillis()));

                // 변경된 엔티티를 저장하고 결과를 확인합니다.
                Board savedBoard = boardRepository.save(foundBoard);
                if (savedBoard.getBoardNo() == boardNo) {
                    return "게시물 수정 성공";
                } else {
                    return "게시물 수정 실패: 엔티티가 잘못 저장되었습니다.";
                }

            } else {
                return "게시물 수정 권한이 없습니다.";
            }
        } catch (Exception e) {
            // 예외가 발생하면 로그에 기록합니다.
            log.error("게시물 수정 중 오류 발생: " + e.getMessage(), e);
            return "게시물 수정 중 오류가 발생했습니다.";
        }
    }

    @Transactional
    public String updateBoardWithFile(int boardNo, BoardDTO boardDTO, List<MultipartFile> files, String memberNo) {

        Board foundBoard = boardRepository.findById(boardNo).orElseThrow(IllegalArgumentException::new);

        if (foundBoard.getMemberNo().equals(memberNo)) {
            foundBoard.boardTitle(boardDTO.getBoardTitle());
            foundBoard.boardContent(boardDTO.getBoardContent());
            foundBoard.boardUpdateDttm(new Timestamp(System.currentTimeMillis()));

            Board updatedBoard = boardRepository.save(foundBoard);
            try {
                if (files != null && !files.isEmpty()) {
                    for (MultipartFile file : files) {

                        String fileName = file.getOriginalFilename();
                        String fileType = file.getContentType();
                        String savedFilePath = fileUtils.saveFile(IMAGE_DIR, fileName, file);
                        String filePath = savedFilePath + "\\" + fileName;

                        // 파일을 저장할 디렉토리 생성 (만약 디렉토리가 없다면)
                        File uploadDir = new File(IMAGE_DIR);
                        if (!uploadDir.exists()) {
                            uploadDir.mkdirs();
                        }

                        File newFile = new File(filePath);
                        file.transferTo(newFile);

                        BoardFileDTO boardFileDTO = new BoardFileDTO();
                        boardFileDTO.setBoardNo(updatedBoard.getBoardNo());
                        boardFileDTO.setBoardFileName(fileName);
                        boardFileDTO.setBoardFilePath(filePath);
                        boardFileDTO.setBoardOriginName(fileName);
                        boardFileDTO.setBoardFileSize(file.getSize());
                        boardFileDTO.setFileType(fileType);

                        BoardFile boardFile = modelMapper.map(boardFileDTO, BoardFile.class);
                        boardFileRepository.save(boardFile);
                    }
                }
            } catch (IOException e) {
                log.error("Failed to save or update board file: " + e.getMessage());
                return "파일 저장에 실패했습니다.";
            }
            return "게시물 수정 성공";
        } else {
            return "게시물 수정 권한이 없습니다.";
        }
    }

    /* 부서별 자료실 게시물 삭제 */
    @Transactional
    public boolean deleteBoard(int boardNo, String memberNo) {
        Board board = boardRepository.findById(boardNo).orElseThrow(IllegalArgumentException::new);

        // 게시물 작성자와 삭제 요청자가 일치하는지 확인
        if (!board.getMemberNo().equals(memberNo)) {
            log.error("삭제 권한이 없습니다.");
            return false;
        }

        // 게시물에 첨부된 파일 삭제
        List<BoardFile> boardFiles = boardFileRepository.findByBoardNo(boardNo);
        for (BoardFile boardFile : boardFiles) {
            String filePath = boardFile.getBoardFilePath();

            boolean isFileDeleted = fileUtils.deleteFile(IMAGE_DIR, FilenameUtils.getName(filePath));

            if (!isFileDeleted) {
                log.error("파일 삭제에 실패했습니다: {}", filePath);
            }
            boardFileRepository.delete(boardFile);
        }

        // 게시물 삭제
        boardRepository.delete(board);
        return true;
    }



}
