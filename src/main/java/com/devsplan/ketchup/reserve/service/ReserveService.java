package com.devsplan.ketchup.reserve.service;

import com.devsplan.ketchup.reserve.dto.ReserveDTO;
import com.devsplan.ketchup.reserve.entity.Reserve;
import com.devsplan.ketchup.reserve.repository.ReserveRepository;
import com.devsplan.ketchup.rsc.dto.ResourceDTO;
import com.devsplan.ketchup.rsc.entity.Resource;
import com.devsplan.ketchup.reserve.repository.ResourceRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReserveService {

    private final ReserveRepository reserveRepository;
    private final ResourceRepository resourceRepository;
    private final ModelMapper modelMapper;

    public ReserveService(ReserveRepository reserveRepository, ResourceRepository resourceRepository, ModelMapper modelMapper) {
        this.reserveRepository = reserveRepository;
        this.resourceRepository = resourceRepository;
        this.modelMapper = modelMapper;
    }

    public List<ReserveDTO> selectReserveList(String rscCategory, LocalDate rsvDate) {
        LocalDateTime startOfDay = rsvDate.atStartOfDay();
        LocalDateTime endOfDay = rsvDate.atTime(23, 59, 59);

//        // 예약 조회건만 가져오고, 빈 달력을 조회해오지 못함.
//        List<Reserve> reserveList = reserveRepository.findByResourcesRscCategoryAndRsvStartDttmBetween(rscCategory, startOfDay, endOfDay);
//
//        return reserveList.stream()
//                .map(reserve -> {
//                    ReserveDTO reserveDTO = new ReserveDTO();
//                    reserveDTO.setReserver(reserve.getMemberNo());
//                    reserveDTO.setRsvNo(reserve.getRsvNo());
//                    reserveDTO.setRsvDescr(reserve.getRsvDescr());
//                    reserveDTO.setRsvStartDttm(reserve.getRsvStartDttm());
//                    reserveDTO.setRsvEndDttm(reserve.getRsvEndDttm());
//                    reserveDTO.setResources(convertToResourceDTO(reserve.getResources()));
//                    return reserveDTO;
//                }).collect(Collectors.toList());

        // 예약이 없는 날짜도 자원별로 달력을 생성하지만, 예약 정보를 조회해오지 않기때문에 모두 빈 달력으로 출력됨.
//        List<Resource> resources = resourceRepository.findByRscCategory(rscCategory);
//
//        List<ReserveDTO> reserveDTOs = new ArrayList<>();
//
//        for (Resource resource : resources) {
//            ReserveDTO reserveDTO = new ReserveDTO();
//            reserveDTO.setResources(convertToResourceDTO(resource));
//            reserveDTOs.add(reserveDTO);
//        }
//
//        return reserveDTOs;

        // rscNo별 달력에 모든 일정이 출력되고, 예약이 없는 날짜는 아예 달력이 출력되지 않음.
//        List<Resource> resources = resourceRepository.findByRscCategory(rscCategory);
//
//        List<ReserveDTO> reserveDTOs = new ArrayList<>();
//
//        for (Resource resource : resources) {
//            List<Reserve> reserveList = reserveRepository.findByResourcesRscCategoryAndRsvStartDttmBetween(rscCategory, startOfDay, endOfDay);
//
//            for (Reserve reserve : reserveList) {
//                ReserveDTO reserveDTO = new ReserveDTO();
//                reserveDTO.setReserver(reserve.getMemberNo());
//                reserveDTO.setRsvNo(reserve.getRsvNo());
//                reserveDTO.setRsvDescr(reserve.getRsvDescr());
//                reserveDTO.setRsvStartDttm(reserve.getRsvStartDttm());
//                reserveDTO.setRsvEndDttm(reserve.getRsvEndDttm());
//                reserveDTO.setResources(convertToResourceDTO(resource));
//                reserveDTOs.add(reserveDTO);
//            }
//        }
//
//        return reserveDTOs;

        // 아우터 조인 메소드 작성
        EntityManager entityManager = null;
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Reserve> query = builder.createQuery(Reserve.class);
        Root<Reserve> root = query.from(Reserve.class);

        // 아우터 조인 설정
        root.fetch("resources", JoinType.LEFT);

        Predicate categoryPredicate = builder.equal(root.get("resources").get("rscCategory"), rscCategory);
        Predicate datePredicate = builder.between(root.get("rsvStartDttm"), startOfDay, endOfDay);
        Predicate finalPredicate = builder.and(categoryPredicate, datePredicate);

        query.select(root).where(finalPredicate);

        List<Reserve> reserveList = entityManager.createQuery(query).getResultList();

        return reserveList.stream()
                .map(reserve -> {
                    ReserveDTO reserveDTO = new ReserveDTO();
                    reserveDTO.setReserver(reserve.getMemberNo());
                    reserveDTO.setRsvNo(reserve.getRsvNo());
                    reserveDTO.setRsvDescr(reserve.getRsvDescr());
                    reserveDTO.setRsvStartDttm(reserve.getRsvStartDttm());
                    reserveDTO.setRsvEndDttm(reserve.getRsvEndDttm());
                    reserveDTO.setResources(convertToResourceDTO(reserve.getResources()));
                    return reserveDTO;
                }).collect(Collectors.toList());
    }

    


    // 자원 예약 목록 조회
    private ResourceDTO convertToResourceDTO(Resource resource) {
            if (resource != null) {
            return new ResourceDTO(
                    resource.getRscNo(),
                    resource.getRscCategory(),
                    resource.getRscName(),
                    resource.getRscInfo(),
                    resource.getRscCap(),
                    resource.isRscIsAvailable(),
                    resource.getRscDescr()
            );
        }
        return null;
    }

    // 자원 예약 상세 조회
    public ReserveDTO selectReserveDetail(int rsvNo) {
        Reserve reserve = reserveRepository.findById((long) rsvNo).orElseThrow(() -> new IllegalArgumentException("해당하는 예약 번호를 찾을 수 없습니다."));

        ReserveDTO reserveDTO = new ReserveDTO();
        reserveDTO.setRsvNo(reserve.getRsvNo());
        reserveDTO.setRsvDescr(reserve.getRsvDescr());
        reserveDTO.setRsvStartDttm(reserve.getRsvStartDttm());
        reserveDTO.setRsvEndDttm(reserve.getRsvEndDttm());
        reserveDTO.setReserver(reserve.getMemberNo());
        reserveDTO.setResources(convertToResourceDTO(reserve.getResources()));

        return reserveDTO;
    }

    // 자원 등록
    @Transactional
    public void insertResource(ResourceDTO resourceDTO) {
        Resource resource = new Resource(
                resourceDTO.getRscNo(),
                resourceDTO.getRscCategory(),
                resourceDTO.getRscName(),
                resourceDTO.getRscInfo(),
                resourceDTO.getRscCap(),
                resourceDTO.isRscIsAvailable(),
                resourceDTO.getRscDescr()
        );
        resourceRepository.save(resource);
    }

    public Resource findResourceById(int rscNo) {
        return resourceRepository.findById((long) rscNo).orElse(null);
    }

    // 자원 예약 등록
    @Transactional
    public void insertReserve(ReserveDTO newReserve, Resource resource) {
        Reserve reserve = new Reserve(
                newReserve.getRsvNo(),
                newReserve.getRsvStartDttm(),
                newReserve.getRsvEndDttm(),
                newReserve.getRsvDescr(),
                newReserve.getReserver(),
                resource
        );
        reserveRepository.save(reserve);
    }

    public Resource getResourceFromDatabase(int rscNo) {
        Optional<Resource> optionalResource = resourceRepository.findById((long) rscNo);
        return optionalResource.orElse(null);
    }

    // 자원 예약 수정
    @Transactional
    public void updateReserve(int rsvNo, String memberNo, ReserveDTO updateReserve) {
        Reserve foundReserve = reserveRepository.findById((long) rsvNo).orElseThrow(IllegalArgumentException::new);

        String reserver = foundReserve.getMemberNo();

        if (!memberNo.equals(reserver)) {
            throw new IllegalArgumentException("예약 수정 권한이 없습니다.");
        }

        if (updateReserve.getRsvStartDttm() != null) {
            foundReserve.rsvStartDttm(updateReserve.getRsvStartDttm());
        }
        if (updateReserve.getRsvEndDttm() != null) {
            foundReserve.rsvEndDttm(updateReserve.getRsvEndDttm());
        }
        if (updateReserve.getRsvDescr() != null) {
            foundReserve.rsvDescr(updateReserve.getRsvDescr());
        }

        reserveRepository.save(foundReserve.builder());
    }

    // 자원 예약 삭제
    @Transactional
    public void deleteById(int rsvNo, String memberNo) {
        Reserve reserve = reserveRepository.findById((long) rsvNo).orElseThrow(() -> new IllegalArgumentException("해당하는 예약 번호를 찾을 수 없습니다."));

        String reserver = reserve.getMemberNo();

        if (!memberNo.equals(reserver)) {
            throw new IllegalStateException("예약 삭제 권한이 없습니다.");
        }

        reserveRepository.delete(reserve);
    }


}
