package com.devsplan.ketchup.rsc.controller;

import com.devsplan.ketchup.common.ResponseDTO;
import com.devsplan.ketchup.mail.dto.MailDTO;
import com.devsplan.ketchup.rsc.dto.RscDTO;
import com.devsplan.ketchup.rsc.service.RscService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/resources")
public class RscController {
    @Value("${jwt.key}")
    private String jwtSecret;
    private final RscService rscService;

    public RscController(RscService rscService) {
        this.rscService = rscService;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> insertResource( @RequestBody RscDTO rscDto) {

        return ResponseEntity.ok().body(
                new ResponseDTO(HttpStatus.OK, "자원 등록 성공",
                        rscService.insertResource(rscDto))
        );
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> selectResource(@RequestHeader("Authorization") String token,
                                                      @RequestParam("part") String partValue) {
        // 부서
        String jwtToken = token.substring(7);
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwtToken).getBody();
        Integer depNo = claims.get("depNo", Integer.class);


        String rscCate = "";
        if(partValue.equals("conferences")) {
            rscCate = "회의실";
        }else if(partValue.equals("vehicles")) {
            rscCate = "차량";
        }

        List<RscDTO> rscList = rscService.selectRscList(rscCate);

        return ResponseEntity.ok().body(
                new ResponseDTO(HttpStatus.OK, "자원 목록 조회",
                        rscList)
        );
    }

    @GetMapping("/{rscNo}")
    public ResponseEntity<ResponseDTO> selectResourceDetail(@PathVariable int rscNo) {
        RscDTO selectRsc = rscService.selectResourceDetail(rscNo);

        return ResponseEntity.ok().body(
                new ResponseDTO(HttpStatus.OK, "자원 상세 조회",
                        selectRsc)
        );
    }

    @PutMapping("/{rscNo}")
    public ResponseEntity<ResponseDTO> updateResource(@PathVariable int rscNo,
                                                      @RequestBody RscDTO updateRscDto) {

        return ResponseEntity.ok().body(
                new ResponseDTO(HttpStatus.OK, "자원 수정",
                        rscService.updateResource(rscNo, updateRscDto))
        );
    }

    @DeleteMapping("/{rscNo}")
    public ResponseEntity<ResponseDTO> deleteResource(@PathVariable int rscNo) {

        return ResponseEntity.ok().body(
                new ResponseDTO(HttpStatus.OK, "자원 삭제",
                        rscService.deleteResource(rscNo))
        );
    }
}
