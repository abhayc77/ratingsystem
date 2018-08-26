package com.insightsystems.ratinginsight.service.mapper;

import com.insightsystems.ratinginsight.domain.*;
import com.insightsystems.ratinginsight.service.dto.ReviewerProfileDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ReviewerProfile and its DTO ReviewerProfileDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ReviewerProfileMapper extends EntityMapper<ReviewerProfileDTO, ReviewerProfile> {


    @Mapping(target = "reviewer", ignore = true)
    ReviewerProfile toEntity(ReviewerProfileDTO reviewerProfileDTO);

    default ReviewerProfile fromId(Long id) {
        if (id == null) {
            return null;
        }
        ReviewerProfile reviewerProfile = new ReviewerProfile();
        reviewerProfile.setId(id);
        return reviewerProfile;
    }
}
