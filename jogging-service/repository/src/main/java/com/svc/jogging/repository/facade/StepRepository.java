package com.svc.jogging.repository.facade;

import com.svc.jogging.repository.entity.StepEntity;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StepRepository extends MongoRepository<StepEntity, String>, StepRepositoryCustom {

    String CACHE_NAME = "steps";

    @Cacheable(value = CACHE_NAME, key = "#userId + '_' + #recordedDate")
    Optional<StepEntity> findByUserIdAndRecordedDateEquals(String userId, String recordedDate);

    @CachePut(value = CACHE_NAME, key = "#entity.userId + '_' + #entity.recordedDate")
    <S extends StepEntity> S save(S entity);
}
