package com.svc.jogging.service.converter;

import com.svc.jogging.model.dto.StepDto;
import com.svc.jogging.model.req.StepCountReq;
import com.svc.jogging.repository.entity.StepEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StepConverter {
    StepEntity fromReq(StepCountReq src);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    void updateStep(StepCountReq src, @MappingTarget StepEntity target);

    StepDto toDto(StepEntity src);

    List<StepDto> toDto(List<StepEntity> src);
}
