package com.devsplan.ketchup.board.service;

import com.devsplan.ketchup.board.dto.BoardDTO;
import com.devsplan.ketchup.board.dto.BoardFileDTO;
import com.devsplan.ketchup.board.entity.Board;
import com.devsplan.ketchup.board.entity.BoardFile;
import com.devsplan.ketchup.board.repository.BoardFileRepository;
import com.devsplan.ketchup.board.repository.BoardRepository;
import com.devsplan.ketchup.common.Criteria;
import com.devsplan.ketchup.util.FileUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
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

    /* 이미지 저장 할 위치 및 응답 할 이미지 주소 */
    @Value("${image.image-dir}")
    private String IMAGE_DIR;

    @Value("${image.image-url}")
    private String IMAGE_URL;

    @Autowired
    public BoardService(BoardRepository boardRepository, ModelMapper modelMapper, BoardFileRepository boardFileRepository) {
        this.boardRepository = boardRepository;
        this.boardFileRepository = boardFileRepository;
        this.modelMapper = modelMapper;
    }

    /* 부서별 자료실 게시물 등록 */
    @Transactional
    public Object insertBoard(BoardDTO boardDTO/*, String memberNo, int depNo*/) {
        log.info("[BoardService] insertBoard Start ===================");

        try {
            // 게시글 엔티티 생성
            boardDTO.setBoardCreateDttm(new Timestamp(System.currentTimeMillis()));
//            boardDTO.setMemberNo(memberNo);
//            boardDTO.setDepartmentNo(depNo);

            Board board = modelMapper.map(boardDTO, Board.class);

            // 게시글 저장
            Board savedBoard = boardRepository.save(board);

            log.info("[BoardService] insertBoard End ===================");
            System.out.println("savedBoard : " + savedBoard.getMemberNo());
            return savedBoard.getBoardNo();
        } catch (Exception e) {
            log.error("게시물 등록 실패: {}", e.getMessage(), e);
            // 예외 발생 시 롤백 요청
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return null;
        }
    }

    /* 부서별 자료실 게시물 등록(첨부파일) */
    @Transactional
    public Object insertBoardWithFile(BoardDTO boardDTO, List<MultipartFile> files/*, String memberNo, int depNo*/) {

        Map<String, Object> result = new HashMap<>();
//        boardDTO.setDepartmentNo(depNo);

        try {
            boardDTO.setBoardCreateDttm(new Timestamp(System.currentTimeMillis()));
//            boardDTO.setMemberNo(memberNo);

            Board savedBoard = modelMapper.map(boardDTO, Board.class);
            boardRepository.save(savedBoard);

            // 파일이 있으면 각 파일을 저장하고, BoardFile 엔티티를 생성하여 연결
            if (files != null) {
                int fileCount = 0; // 등록된 파일 수를 세는 변수 추가
                for (MultipartFile file : files) {
                    if (fileCount >= 5) { // 등록된 파일이 5개 이상이면 더 이상 파일을 등록하지 않음
                        break;
                    }
                    String fileName = file.getOriginalFilename();
                    String newFileName = UUID.randomUUID().toString().replace("-", "");
//                    String newFileName = UUID.randomUUID().toString().replace("-", "")+ "." + FilenameUtils.getExtension(fileName);;
                    String savedFilePath = FileUtils.saveFile(IMAGE_DIR, newFileName, file);
                    String filePath = savedFilePath + "/" + fileName;

                    // 파일을 저장할 디렉토리 생성 (만약 디렉토리가 없다면)
                    File newFile = new File(savedFilePath);
                    file.transferTo(newFile);

                    BoardFileDTO boardFileDTO = new BoardFileDTO();
                    boardFileDTO.setBoardNo(savedBoard.getBoardNo());
                    boardFileDTO.setBoardFileName(newFileName);
                    boardFileDTO.setBoardFilePath(filePath);
                    boardFileDTO.setBoardFileOriName(fileName);

                    BoardFile boardFile = modelMapper.map(boardFileDTO, BoardFile.class);
                    boardFileRepository.save(boardFile);
                    fileCount++;
                }
            }
            log.info("게시물 등록 성공");
            System.out.println("savedBoard : " + savedBoard.getBoardNo());
            return savedBoard.getBoardNo();

        } catch (IOException e) {
            log.error("첨부파일 등록 실패: " + e.getMessage());
            result.put("result", false);
            throw new RuntimeException(e);

        } catch (Exception e) {
            log.error("공지 등록 실패: {}", e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return result;
    }

    /* 부서별 자료실 게시물 목록조회 & 페이징 & 목록 제목검색 조회 */
    public Page<BoardDTO> selectBoardList(int departmentNo, Criteria cri, String title) {

        int page = cri.getPageNum() - 1;
        int size = cri.getAmount();
        Pageable paging = PageRequest.of(page, size, Sort.by("boardNo").descending());

        Page<Board> boardList = null;
        if (title != null && !title.isEmpty()) {
            boardList = boardRepository.findByDepartmentNoAndBoardTitleContainingIgnoreCase(departmentNo, paging, title);
        } else {
            boardList = boardRepository.findByDepartmentNo(departmentNo, paging);
        }

        Page<BoardDTO> boardDTOList = boardList.map(board -> modelMapper.map(board, BoardDTO.class));

        return boardDTOList;
//        return new PageImpl<>(boardDTOList, paging, boardList.getTotalElements());}
    }

    /* 부서 전체 게시물 목록조회(권한자-대표) */
    public Page<BoardDTO> selectAllBoards(Criteria cri, String title) {

        int index = cri.getPageNum() -1;
        int count = cri.getAmount();

        Pageable paging = PageRequest.of(index, count, Sort.by("boardNo").descending());


        try {
            Page<Board> boardList;
            if (title != null && !title.isEmpty()) {
                boardList = boardRepository.findByBoardTitleContaining(paging, title);
            } else {
                boardList = boardRepository.findAll(paging);
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

        List<BoardFile> boardFiles = findBoardFilesByBoardNo(boardNo);
        List<BoardFileDTO> boardFileDTOList = new ArrayList<>();

        for (BoardFile boardFile : boardFiles) {
            BoardFileDTO boardFileDTO = modelMapper.map(boardFile, BoardFileDTO.class);
            boardFileDTOList.add(boardFileDTO);
        }

        boardDTO.setBoardFileList(boardFileDTOList);
        log.info("boardDTO : " + boardDTO);

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
            log.error("게시물 수정 중 오류 발생: " + e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "게시물 수정 중 오류가 발생했습니다.";
        }
    }

    @Transactional
    public String updateBoardWithFile(int boardNo, BoardDTO boardDTO, List<MultipartFile> files, String memberNo) {

        Board foundBoard = boardRepository.findById(boardNo).orElseThrow(IllegalArgumentException::new);
        try {
            if (foundBoard.getMemberNo().equals(memberNo)) {
                foundBoard.boardTitle(boardDTO.getBoardTitle());
                foundBoard.boardContent(boardDTO.getBoardContent());
                foundBoard.boardUpdateDttm(new Timestamp(System.currentTimeMillis()));

                Board updatedBoard = boardRepository.save(foundBoard);

                if (files != null && !files.isEmpty()) {
                    int fileCount = 0; // 등록된 파일 수를 세는 변수 추가
                    for (MultipartFile file : files) {
                        if (fileCount >= 5) { // 등록된 파일이 5개 이상이면 더 이상 파일을 등록하지 않음
                            break;
                        }
                        String fileName = file.getOriginalFilename();
                        String newFileName = UUID.randomUUID().toString().replace("-", "") + "." + fileName;
                        String savedFilePath = FileUtils.saveFile(IMAGE_DIR, newFileName, file);
                        String filePath = savedFilePath + "//" + fileName;

                        // 파일을 저장할 디렉토리 생성 (만약 디렉토리가 없다면)
                        File newFile = new File(savedFilePath);
                        file.transferTo(newFile);

                        BoardFileDTO boardFileDTO = new BoardFileDTO();
                        boardFileDTO.setBoardNo(updatedBoard.getBoardNo());
                        boardFileDTO.setBoardFileName(newFileName);
                        boardFileDTO.setBoardFilePath(filePath);
                        boardFileDTO.setBoardFileOriName(fileName);

                        BoardFile boardFile = modelMapper.map(boardFileDTO, BoardFile.class);
                        boardFileRepository.save(boardFile);
                        fileCount++;
                    }

                    return "게시물 수정 성공";
                } else {
                    return "첨부파일이 없음";
                }
            } else {
                return "게시물 수정 권한이 없습니다.";
            }
        } catch (Exception e) {
            log.error("게시물 수정 중 오류 발생: " + e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "게시물 수정 중 오류가 발생했습니다.";
        }
    }

    /* 부서별 자료실 게시물 삭제 */
    @Transactional
    public boolean deleteBoard(int boardNo, String memberNo) {
        try {
            Board board = boardRepository.findById(boardNo).orElseThrow(IllegalArgumentException::new);

            // 게시물 작성자와 삭제 요청자가 일치하는지 확인
            if (!board.getMemberNo().equals(memberNo)) {
                log.error("삭제 권한이 없습니다.");
                return false;
            }

            // 게시물에 첨부된 파일 삭제
            List<BoardFile> boardFiles = boardFileRepository.findByBoardNo(boardNo);
            boardFileRepository.deleteAll(boardFiles);

            // 게시물 삭제
            boardRepository.delete(board);
            return true;
        } catch (Exception e) {
            log.error("게시물 삭제 중 오류 발생: " + e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false; // 삭제 중 오류 발생
        }
    }

    public BoardDTO getBoardById(int boardNo) {
        Board board = boardRepository.findById(boardNo).orElse(null);
        if (board != null) {
            return  modelMapper.map(board, BoardDTO.class);
        } else  {
            return null;
        }
    }
}
