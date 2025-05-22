package com.svc.jogging.repository.impl;

import com.svc.jogging.repository.entity.StepEntity;
import com.svc.jogging.repository.facade.StepRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class StepRepositoryImpl implements StepRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<StepEntity> findTopUsersByTimeRange(int limit, Instant startTime, Instant endTime) {
        MatchOperation match = Aggregation.match(Criteria.where("endedAt").gte(startTime).lt(endTime));
        GroupOperation group = Aggregation.group("userId").sum("steps").as("steps")
                .first("userId").as("userId")
                .first("startedAt").as("startedAt")
                .first("endedAt").as("endedAt");
        SortOperation sort = Aggregation.sort(Sort.Direction.DESC, "steps");
        LimitOperation limitOp = Aggregation.limit(limit);

        Aggregation agg = Aggregation.newAggregation(match, group, sort, limitOp);
        AggregationResults<StepEntity> results = mongoTemplate.aggregate(agg, StepEntity.COLLECTION_NAME, StepEntity.class);

        return results.getMappedResults();
    }

    @Override
    public long countStepsByUserAndTimeRange(String userId, Instant startTime, Instant endTime) {
        MatchOperation match = Aggregation.match(Criteria.where("userId").is(userId).andOperator(Criteria.where("endedAt").gte(startTime).lte(endTime)));
        Aggregation aggregation = Aggregation.newAggregation(
                match,
                Aggregation.group("userId").sum("steps").as("totalSteps")
        );

        AggregationResults<Document> results = mongoTemplate.aggregate(
                aggregation, StepEntity.COLLECTION_NAME, Document.class);

        return results.getUniqueMappedResult() != null ? results.getUniqueMappedResult().getLong("totalSteps") : 0;
    }
}
