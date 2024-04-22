package com.devsplan.ketchup.board.service;

import com.devsplan.ketchup.board.dto.BoardDTO;
import com.devsplan.ketchup.board.entity.Board;
import com.devsplan.ketchup.board.repository.BoardRepository;
import com.devsplan.ketchup.util.FileUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;
    private final ModelMapper modelMapper;


    /* 이미지 저장 할 위치 및 응답 할 이미지 주소 */
    @Value("${image.image-dir}")
    private String IMAGE_DIR;

    @Value("${image.image-url}")
    private String IMAGE_URL;

    public BoardService(BoardRepository boardRepository, ModelMapper modelMapper) {
        this.boardRepository = boardRepository;
        this.modelMapper = modelMapper;
    }

/*    public List<Board> selectMenuByBindingName(String title) {
//        String jpql = "SELECT m FROM Section02Menu m WHERE m.menuName = :menuName";     // :(콜론)파라미터 이름(이름 기준 파라미터 바인딩)
//        List<Board> resultBoardList = manager.createQuery(jpql, Menu.class)
//                .setParameter("menuName", menuName)     // 파라미터 세팅 필요 (:파라미터 사용 시)
//                .getResultList();

        return boardRepository.selectMenuByBindingName(title);
    }*/

    public Object selectBoardList(String title) {

//        List<Board> boardListByTitle = boardRepository.findByBoardNameContaining(title);

//        List<BoardDTO> boardDTOList = boardListByTitle.stream()
//                                                .map(board -> modelMapper.map(board, BoardDTO.class))
//                                                .collect(Collectors.toList());


//        return boardDTOList;
        return null;
    }


    @Transactional
    public String insertBoard(BoardDTO boardDTO, MultipartFile boardFile) {
        log.info("[BoardService] insertBoard Start ===================");
        log.info("[BoardService] BoardDTO : " + boardDTO);

        String imageName = UUID.randomUUID().toString().replace("-", "");
        String replaceFileName = null;
        int result = 0; // 결과에 따른 값을 구분하기위한 용도의 변수

        try{
            replaceFileName = FileUtils.saveFile(IMAGE_DIR, imageName, boardFile);

            boardDTO.setBoardfileUrl(replaceFileName);

            log.info("[BoardService] insert Image Name : ", replaceFileName);

            // 저장을 위해서 일반 DTO객체를 Entity객체로 변경
            Board insertProduct = modelMapper.map(boardDTO, Board.class);

            boardRepository.save(insertProduct);

            result = 1;
        } catch (Exception e){
            System.out.println("check");
            FileUtils.deleteFile(IMAGE_DIR, replaceFileName);
            throw new RuntimeException(e);
        }


        log.info("[BoardService] insertBoard End ===================");
        return (result > 0)? "게시글 입력 성공" : "게시글 입력 실패";
    }

    @Transactional
    public Object updateBoard(BoardDTO boardDTO) {
        log.info("[BoardService] updateBoard Start ==============================");

        int result = 0;
        log.info("[BoardService] updateBoard End ==============================");

        return (result > 0) ? "게시글 수정 성공" : "게시글 수정 실패" ;
    }



}
