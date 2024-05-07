package com.devsplan.ketchup.rsc.service;

import com.devsplan.ketchup.mail.entity.Mail;
import com.devsplan.ketchup.mail.repository.MailRepository;
import com.devsplan.ketchup.reserve.entity.Reserve;
import com.devsplan.ketchup.reserve.repository.ReserveRepository;
import com.devsplan.ketchup.rsc.dto.RscDTO;
import com.devsplan.ketchup.rsc.entity.Rsc;
import com.devsplan.ketchup.rsc.repository.RscRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RscService {
    private final RscRepository rscRepository;
    private final ReserveRepository reserveRepository;
    private final MailRepository mailRepository;

    public RscService(RscRepository rscRepository, ReserveRepository reserveRepository, MailRepository mailRepository) {
        this.rscRepository = rscRepository;
        this.reserveRepository = reserveRepository;
        this.mailRepository = mailRepository;
    }

    @Transactional
    public Object insertResource(RscDTO rscDTO) {
        Rsc resource = new Rsc(
                rscDTO.getRscCategory(),
                rscDTO.getRscName(),
                rscDTO.getRscInfo(),
                rscDTO.getRscCap()
        );

        return rscRepository.save(resource);
    }

    public List<RscDTO> selectRscList(String part) {
        List<Rsc> rscList = rscRepository.findByRscCategory(part);

        return rscList.stream()
                .map(rsc -> new RscDTO(
                        rsc.getRscNo(),
                        rsc.getRscCategory(),
                        rsc.getRscName(),
                        rsc.getRscInfo(),
                        rsc.getRscCap(),
                        rsc.isRscIsAvailable(),
                        rsc.getRscDescr()
                )).toList();
    }

    public RscDTO selectResourceDetail(int rscNo) {
        Rsc rscDetail = rscRepository.findByRscNo(rscNo);

        return new RscDTO(
                rscDetail.getRscNo(),
                rscDetail.getRscCategory(),
                rscDetail.getRscName(),
                rscDetail.getRscInfo(),
                rscDetail.getRscCap(),
                rscDetail.isRscIsAvailable(),
                rscDetail.getRscDescr()
        );
    }

    @Transactional
    public int updateResource(int rscNo, RscDTO updateRsc) {
        Rsc rsc = rscRepository.findByRscNo(rscNo);

        int result = 0;

        // 자원 사용 가능 여부가 원래의 정보와 다른 경우
        if(rsc.isRscIsAvailable() != updateRsc.isRscIsAvailable()) {
            rsc.rscIsAvailable(updateRsc.isRscIsAvailable());
            result += 1;

            // 사용 가능 불가로 변경되는 경우(true) - 예약 일정 삭제
//            if(updateRsc.isRscIsAvailable()) {
                // 해당 자원에 대한 예약자 조회
//                List<Reserve> reserveList = reserveRepository.findByRscNo(updateRsc.getRscNo());

                // 예약 일정이 존재하는 경우
//                if(reserveList.size() > 0) {
//                    for(int i = 0; i < reserveList.size(); i++) {
//                        System.out.println("예약 일정 삭제!!");
//                        reserveRepository.deleteByRsv(reserveList.get(i).getRsvNo());
//                        result += 1;
//
//                        System.out.println("예약자 예약 취소 메일 전송");
//                        Mail rsvCancelMail;
//                        if(/*회의실인 경우*/) {
//                            rsvCancelMail = new Mail(
//                                    /*
//                                     * 작성자,
//                                     * "회의실 예약 취소 안내",
//                                     * "불가피한 사정으로 회의실의 사용이 불가하여 예약이 취소되었음을 안내드립니다.",
//                                     * 'N',
//                                     * 'N'
//                                     */
//                            );
//                        }else {
//                            rsvCancelMail = new Mail(
//                                    /*
//                                     * 작성자,
//                                     * "차량 예약 취소 안내",
//                                     * "불가피한 사정으로 차량의 사용이 불가하여 예약이 취소되었음을 안내드립니다.",
//                                     * 'N',
//                                     * 'N'
//                                     */
//                            );
//                        }
//                        mailRepository.save(rsvCancelMail);
//                        result += 1;
//                    }
//                }
//            }
        }

        if(updateRsc.getRscDescr() != null) {
            rsc.rscDescr(updateRsc.getRscDescr());
            result += 1;
        }

        return result;
    }

    @Transactional
    public int deleteResource(int rscNo) {
        // 삭제하려는 자원에 대한 예약 조회
//        List<Reserve> reserveList = reserveRepository.findByRscNo(rscNo);

        // 예약 일정이 존재하는 경우
//        if(reserveList.size() > 0) {
//            for(int i = 0; i < reserveList.size(); i++) {
//                System.out.println("예약 일정 삭제!!");
//                reserveRepository.deleteByRsv(reserveList.get(i).getRsvNo());
//
//                System.out.println("예약자 예약 취소 메일 전송");
//                Mail rsvCancelMail;
//                if(/*회의실인 경우*/) {
//                    rsvCancelMail = new Mail(
//                        /*
//                        * 작성자,
//                        * "회의실 예약 취소 안내",
//                        * "불가피한 사정으로 회의실의 사용이 불가하여 예약이 취소되었음을 안내드립니다.",
//                        * 'N',
//                        * 'N'
//                        */
//                    );
//                }else {
//                    rsvCancelMail = new Mail(
//                        /*
//                        * 작성자,
//                        * "차량 예약 취소 안내",
//                        * "불가피한 사정으로 차량의 사용이 불가하여 예약이 취소되었음을 안내드립니다.",
//                        * 'N',
//                        * 'N'
//                        */
//                    );
//                }
//
//                mailRepository.save(rsvCancelMail);
//            }
//        }

        return rscRepository.deleteByRscNo(rscNo);
    }
}
