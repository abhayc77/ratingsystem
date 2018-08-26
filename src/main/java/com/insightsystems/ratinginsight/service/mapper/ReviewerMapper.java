package com.insightsystems.ratinginsight.service.mapper;

import com.insightsystems.ratinginsight.domain.*;
import com.insightsystems.ratinginsight.service.dto.ReviewerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Reviewer and its DTO ReviewerDTO.
 */
@Mapper(componentModel = "spring", uses = {ReviewerProfileMapper.class, ReviewMapper.class})
public interface ReviewerMapper extends EntityMapper<ReviewerDTO, Reviewer> {

    @Mapping(source = "profile.id", target = "profileId")
    ReviewerDTO toDto(Reviewer reviewer);

    @Mapping(source = "profileId", target = "profile")
    Reviewer toEntity(ReviewerDTO reviewerDTO);

    default Reviewer fromId(Long id) {
        if (id == null) {
            return null;
        }
        Reviewer reviewer = new Reviewer();
        reviewer.setId(id);
        return reviewer;
    }
}
