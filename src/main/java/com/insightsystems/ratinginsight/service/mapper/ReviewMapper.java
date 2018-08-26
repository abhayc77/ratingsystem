package com.insightsystems.ratinginsight.service.mapper;

import com.insightsystems.ratinginsight.domain.*;
import com.insightsystems.ratinginsight.service.dto.ReviewDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Review and its DTO ReviewDTO.
 */
@Mapper(componentModel = "spring", uses = {ReviewAnalysisMapper.class})
public interface ReviewMapper extends EntityMapper<ReviewDTO, Review> {

    @Mapping(source = "reviewAnalysis.id", target = "reviewAnalysisId")
    ReviewDTO toDto(Review review);

    @Mapping(source = "reviewAnalysisId", target = "reviewAnalysis")
    @Mapping(target = "reviewers", ignore = true)
    @Mapping(target = "products", ignore = true)
    Review toEntity(ReviewDTO reviewDTO);

    default Review fromId(Long id) {
        if (id == null) {
            return null;
        }
        Review review = new Review();
        review.setId(id);
        return review;
    }
}
