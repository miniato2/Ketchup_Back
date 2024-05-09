package com.devsplan.ketchup.rsc.service;

import com.devsplan.ketchup.mail.entity.Mail;
import com.devsplan.ketchup.mail.entity.Receiver;
import com.devsplan.ketchup.mail.repository.MailRepository;
import com.devsplan.ketchup.mail.repository.ReceiverRepository;
import com.devsplan.ketchup.reserve.entity.Reserve;
import com.devsplan.ketchup.reserve.repository.ReserveRepository;
import com.devsplan.ketchup.rsc.dto.ResourceDTO;
import com.devsplan.ketchup.rsc.entity.Resource;
import com.devsplan.ketchup.rsc.repository.RscRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RscService {
    private final RscRepository rscRepository;
    private final ReserveRepository reserveRepository;
    private final MailRepository mailRepository;
    private final ReceiverRepository receiverRepository;

    public RscService(RscRepository rscRepository, ReserveRepository reserveRepository, MailRepository mailRepository, ReceiverRepository receiverRepository) {
        this.rscRepository = rscRepository;
        this.reserveRepository = reserveRepository;
        this.mailRepository = mailRepository;
        this.receiverRepository = receiverRepository;
    }

    @Transactional
    public Object insertResource(ResourceDTO rscDTO) {
        Resource resource = new Resource(
                rscDTO.getRscCategory(),
                rscDTO.getRscName(),
                rscDTO.getRscInfo(),
                rscDTO.getRscCap()
        );

        return rscRepository.save(resource);
    }

    public List<ResourceDTO> selectRscList(String part) {
        List<Resource> rscList = rscRepository.findByRscCategory(part);

        return rscList.stream()
                .map(rsc -> new ResourceDTO(
                        rsc.getRscNo(),
                        rsc.getRscCategory(),
                        rsc.getRscName(),
                        rsc.getRscInfo(),
                        rsc.getRscCap(),
                        rsc.isRscIsAvailable(),
                        rsc.getRscDescr()
                )).toList();
    }

    public ResourceDTO selectResourceDetail(int rscNo) {
        Resource rscDetail = rscRepository.findByRscNo(rscNo);

        return new ResourceDTO(
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
    public int updateResource(String memberNo, int rscNo, ResourceDTO updateRsc) {
        Resource rsc = rscRepository.findByRscNo(rscNo);

        int result = 0;

        // 자원 사용 가능 여부가 원래의 정보와 다른 경우
        if(rsc.isRscIsAvailable() != updateRsc.isRscIsAvailable()) {
            rsc.rscIsAvailable(updateRsc.isRscIsAvailable());
            result += 1;

            // 사용 가능 불가로 변경되는 경우(true) - 예약 일정 삭제
            if(updateRsc.isRscIsAvailable()) {
                // 해당 자원에 대한 예약자 조회
                List<Reserve> reserveList = reserveRepository.findByResources(rsc);

                // 예약 일정이 존재하는 경우
                if(!reserveList.isEmpty()) {
                    for(Reserve list : reserveList) {
                        System.out.println("예약 일정 삭제");
                        result += reserveRepository.deleteByRsvNo(list.getRsvNo());

                        System.out.println("예약자 예약 취소 메일 전송");
                        Mail rsvCancelMail;
                        if(list.getResources().getRscCategory().equals("회의실")) {
                            rsvCancelMail = new Mail(
                                    memberNo,
                                    "회의실 예약 취소 안내",
                                    "불가피한 사정으로 회의실의 사용이 불가하여 예약이 취소되었음을 안내드립니다.",
                                    'N',
                                    'N'
                            );
                        }else {
                            rsvCancelMail = new Mail(
                                     memberNo,
                                     "차량 예약 취소 안내",
                                     "불가피한 사정으로 차량의 사용이 불가하여 예약이 취소되었음을 안내드립니다.",
                                     'N',
                                     'N'
                            );
                        }

                        Mail rscMail = mailRepository.save(rsvCancelMail);

                        Receiver mailReceive = new Receiver(
                                rscMail.getMailNo(),
                                list.getMemberNo(),
                                'N'
                        );
                        receiverRepository.save(mailReceive);

                        result += 1;
                    }
                }
            }
        }

        if(updateRsc.getRscDescr() != null) {
            rsc.rscDescr(updateRsc.getRscDescr());
            result += 1;
        }

        return result;
    }

    @Transactional
    public int deleteResource(String memberNo, int rscNo) {
        Resource rsc = rscRepository.findByRscNo(rscNo);
        int result = 0;

        List<Reserve> reserveList = reserveRepository.findByResources(rsc);

        // 예약 일정이 존재하는 경우
        if(!reserveList.isEmpty()) {
            for(Reserve list : reserveList) {
                System.out.println("예약 일정 삭제");
                result += reserveRepository.deleteByRsvNo(list.getRsvNo());

                System.out.println("예약자 예약 취소 메일 전송");
                Mail rsvCancelMail;
                if(list.getResources().getRscCategory().equals("회의실")) {
                    rsvCancelMail = new Mail(
                            memberNo,
                            "회의실 예약 취소 안내",
                            "불가피한 사정으로 회의실의 사용이 불가하여 예약이 취소되었음을 안내드립니다.",
                            'N',
                            'N'
                    );
                }else {
                    rsvCancelMail = new Mail(
                            memberNo,
                            "차량 예약 취소 안내",
                            "불가피한 사정으로 차량의 사용이 불가하여 예약이 취소되었음을 안내드립니다.",
                            'N',
                            'N'
                    );
                }

                Mail rscMail = mailRepository.save(rsvCancelMail);

                Receiver mailReceive = new Receiver(
                        rscMail.getMailNo(),
                        list.getMemberNo(),
                        'N'
                );
                receiverRepository.save(mailReceive);

                result += 1;
            }
        }

        return rscRepository.deleteByRscNo(rscNo);
    }
}
