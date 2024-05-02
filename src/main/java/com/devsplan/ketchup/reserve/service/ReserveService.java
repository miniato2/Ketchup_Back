package com.devsplan.ketchup.reserve.service;

import com.devsplan.ketchup.reserve.dto.ReserveDTO;
import com.devsplan.ketchup.reserve.entity.Reserve;
import com.devsplan.ketchup.reserve.repository.ReserveRepository;
import com.devsplan.ketchup.reserve.dto.ResourceDTO;
import com.devsplan.ketchup.reserve.entity.Resource;
import com.devsplan.ketchup.reserve.repository.ResourceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReserveService {

    private final ReserveRepository reserveRepository;
    private final ResourceRepository resourceRepository;
    private final ModelMapper modelMapper;

    public ReserveService(ReserveRepository reserveRepository, ResourceRepository resourceRepository, ModelMapper modelMapper) {
        this.reserveRepository = reserveRepository;
        this.resourceRepository = resourceRepository;
        this.modelMapper = modelMapper;
    }

    public List<ReserveDTO> selectReserveList(String rscCategory, LocalDateTime rsvStartDttm) {
        List<Reserve> reserveList = reserveRepository.findByResourcesRscCategoryAndRsvStartDttm(rscCategory, rsvStartDttm);
        return reserveList.stream()
                .map(reserve -> {
                    ReserveDTO reserveDTO = new ReserveDTO();
                    reserveDTO.setRsvNo(reserve.getRsvNo());
                    reserveDTO.setRsvDescr(reserve.getRsvDescr());
                    reserveDTO.setRsvStartDttm(reserve.getRsvStartDttm());
                    reserveDTO.setRsvEndDttm(reserve.getRsvEndDttm());
                    reserveDTO.setResources(convertToResourceDTO(reserve.getResources()));
                    return reserveDTO;
                }).collect(Collectors.toList());
    }

    private ResourceDTO convertToResourceDTO(Resource resource) {
        if (resource != null) {
            return new ResourceDTO(
                    resource.getRsc_no(),
                    resource.getRscCategory(),
                    resource.getRscName(),
                    resource.getRscInfo(),
                    resource.getRscCap(),
                    resource.getRscIsAvailable(),
                    resource.getRscDescr()
            );
        }
        return null;
    }

    // 원래 하나의 로직안에서 두 개의 등록을 진행하려고 했었음.
//    @Transactional
//    public void insertReserve(ReserveDTO newReserve) {
//        ResourceDTO resourceDTO = newReserve.getResources();
//        Resource resource = new Resource(resourceDTO.getRscNo(), resourceDTO.getRscCategory(), resourceDTO.getRscName(), resourceDTO.getRscInfo(), resourceDTO.getRscCap(), resourceDTO.getRscIsAvailable(), resourceDTO.getRscDescr());
//        System.out.println("어떤 자원을 등록할 건지 = " + resource);
//        resourceRepository.save(resource);
//
//        Reserve reserve = new Reserve(
//                newReserve.getRsvNo(),
//                newReserve.getRsvDescr(),
//                newReserve.getRsvStartDttm(),
//                newReserve.getRsvEndDttm()
//        );
//        System.out.println("어떤 내용으로 자원 예약할 건지 = " + reserve);
//        reserveRepository.save(reserve);
//    }

    @Transactional
    public void insertResource(ResourceDTO resourceDTO) {
        Resource resource = new Resource(
                resourceDTO.getRscNo(),
                resourceDTO.getRscCategory(),
                resourceDTO.getRscName(),
                resourceDTO.getRscInfo(),
                resourceDTO.getRscCap(),
                resourceDTO.getRscIsAvailable(),
                resourceDTO.getRscDescr()
        );
        resourceRepository.save(resource);
    }

    @Transactional
    public void insertReserve(ReserveDTO newReserve, Resource resource) {
        Reserve reserve = new Reserve(
                newReserve.getRsvNo(),
                newReserve.getRsvDescr(),
                newReserve.getRsvStartDttm(),
                newReserve.getRsvEndDttm(),
                resource
        );
        reserveRepository.save(reserve);
    }

    public Resource getResourceFromDatabase(int rscNo) {
        Optional<Resource> optionalResource = resourceRepository.findById((long) rscNo);
        return optionalResource.orElse(null);
    }

}
