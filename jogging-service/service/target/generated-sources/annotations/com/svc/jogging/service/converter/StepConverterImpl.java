package com.svc.jogging.service.converter;

import com.svc.jogging.model.dto.StepDto;
import com.svc.jogging.model.req.StepCountReq;
import com.svc.jogging.repository.entity.StepEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-07-31T16:34:20+0700",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 11.0.6 (JetBrains s.r.o)"
)
@Component
public class StepConverterImpl implements StepConverter {

    @Override
    public StepEntity fromReq(StepCountReq src) {
        if ( src == null ) {
            return null;
        }

        StepEntity stepEntity = new StepEntity();

        stepEntity.setUserId( src.getUserId() );
        stepEntity.setSteps( src.getSteps() );
        stepEntity.setFromLocation( src.getFromLocation() );
        stepEntity.setToLocation( src.getToLocation() );
        stepEntity.setRecordedDate( src.getRecordedDate() );
        stepEntity.setZoneId( src.getZoneId() );

        return stepEntity;
    }

    @Override
    public void updateStep(StepCountReq src, StepEntity target) {
        if ( src == null ) {
            return;
        }

        if ( src.getUserId() != null ) {
            target.setUserId( src.getUserId() );
        }
        target.setSteps( src.getSteps() );
        if ( src.getFromLocation() != null ) {
            target.setFromLocation( src.getFromLocation() );
        }
        if ( src.getToLocation() != null ) {
            target.setToLocation( src.getToLocation() );
        }
        if ( src.getRecordedDate() != null ) {
            target.setRecordedDate( src.getRecordedDate() );
        }
        if ( src.getZoneId() != null ) {
            target.setZoneId( src.getZoneId() );
        }
    }

    @Override
    public StepDto toDto(StepEntity src) {
        if ( src == null ) {
            return null;
        }

        StepDto stepDto = new StepDto();

        stepDto.set_id( src.get_id() );
        stepDto.setUserId( src.getUserId() );
        stepDto.setSteps( src.getSteps() );
        stepDto.setFromLocation( src.getFromLocation() );
        stepDto.setToLocation( src.getToLocation() );
        stepDto.setRecordedDate( src.getRecordedDate() );
        stepDto.setZoneId( src.getZoneId() );
        stepDto.setStartedAt( src.getStartedAt() );
        stepDto.setEndedAt( src.getEndedAt() );

        return stepDto;
    }

    @Override
    public List<StepDto> toDto(List<StepEntity> src) {
        if ( src == null ) {
            return null;
        }

        List<StepDto> list = new ArrayList<StepDto>( src.size() );
        for ( StepEntity stepEntity : src ) {
            list.add( toDto( stepEntity ) );
        }

        return list;
    }
}
