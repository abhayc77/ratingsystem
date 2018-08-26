package com.insightsystems.ratinginsight.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.insightsystems.ratinginsight.domain.ReviewerProfile;
import com.insightsystems.ratinginsight.domain.*; // for static metamodels
import com.insightsystems.ratinginsight.repository.ReviewerProfileRepository;
import com.insightsystems.ratinginsight.repository.search.ReviewerProfileSearchRepository;
import com.insightsystems.ratinginsight.service.dto.ReviewerProfileCriteria;

import com.insightsystems.ratinginsight.service.dto.ReviewerProfileDTO;
import com.insightsystems.ratinginsight.service.mapper.ReviewerProfileMapper;

/**
 * Service for executing complex queries for ReviewerProfile entities in the database.
 * The main input is a {@link ReviewerProfileCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ReviewerProfileDTO} or a {@link Page} of {@link ReviewerProfileDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReviewerProfileQueryService extends QueryService<ReviewerProfile> {

    private final Logger log = LoggerFactory.getLogger(ReviewerProfileQueryService.class);

    private final ReviewerProfileRepository reviewerProfileRepository;

    private final ReviewerProfileMapper reviewerProfileMapper;

    private final ReviewerProfileSearchRepository reviewerProfileSearchRepository;

    public ReviewerProfileQueryService(ReviewerProfileRepository reviewerProfileRepository, ReviewerProfileMapper reviewerProfileMapper, ReviewerProfileSearchRepository reviewerProfileSearchRepository) {
        this.reviewerProfileRepository = reviewerProfileRepository;
        this.reviewerProfileMapper = reviewerProfileMapper;
        this.reviewerProfileSearchRepository = reviewerProfileSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ReviewerProfileDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ReviewerProfileDTO> findByCriteria(ReviewerProfileCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ReviewerProfile> specification = createSpecification(criteria);
        return reviewerProfileMapper.toDto(reviewerProfileRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ReviewerProfileDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReviewerProfileDTO> findByCriteria(ReviewerProfileCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ReviewerProfile> specification = createSpecification(criteria);
        return reviewerProfileRepository.findAll(specification, page)
            .map(reviewerProfileMapper::toDto);
    }

    /**
     * Function to convert ReviewerProfileCriteria to a {@link Specification}
     */
    private Specification<ReviewerProfile> createSpecification(ReviewerProfileCriteria criteria) {
        Specification<ReviewerProfile> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ReviewerProfile_.id));
            }
            if (criteria.getTotalReviews() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalReviews(), ReviewerProfile_.totalReviews));
            }
            if (criteria.getReviewerRanking() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReviewerRanking(), ReviewerProfile_.reviewerRanking));
            }
            if (criteria.getTotalHelpfulVotes() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalHelpfulVotes(), ReviewerProfile_.totalHelpfulVotes));
            }
            if (criteria.getRecentRating() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRecentRating(), ReviewerProfile_.recentRating));
            }
            if (criteria.getReviewerId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getReviewerId(), ReviewerProfile_.reviewer, Reviewer_.id));
            }
        }
        return specification;
    }

}
