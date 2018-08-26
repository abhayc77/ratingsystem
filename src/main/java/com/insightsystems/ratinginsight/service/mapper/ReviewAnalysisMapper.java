package com.insightsystems.ratinginsight.service.mapper;

import com.insightsystems.ratinginsight.domain.*;
import com.insightsystems.ratinginsight.service.dto.ReviewAnalysisDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ReviewAnalysis and its DTO ReviewAnalysisDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ReviewAnalysisMapper extends EntityMapper<ReviewAnalysisDTO, ReviewAnalysis> {



    default ReviewAnalysis fromId(Long id) {
        if (id == null) {
            return null;
        }
        ReviewAnalysis reviewAnalysis = new ReviewAnalysis();
        reviewAnalysis.setId(id);
        return reviewAnalysis;
    }
}
