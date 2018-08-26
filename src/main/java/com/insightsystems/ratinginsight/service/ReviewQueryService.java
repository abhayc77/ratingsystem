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

import com.insightsystems.ratinginsight.domain.Review;
import com.insightsystems.ratinginsight.domain.*; // for static metamodels
import com.insightsystems.ratinginsight.repository.ReviewRepository;
import com.insightsystems.ratinginsight.repository.search.ReviewSearchRepository;
import com.insightsystems.ratinginsight.service.dto.ReviewCriteria;

import com.insightsystems.ratinginsight.service.dto.ReviewDTO;
import com.insightsystems.ratinginsight.service.mapper.ReviewMapper;

/**
 * Service for executing complex queries for Review entities in the database.
 * The main input is a {@link ReviewCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ReviewDTO} or a {@link Page} of {@link ReviewDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReviewQueryService extends QueryService<Review> {

    private final Logger log = LoggerFactory.getLogger(ReviewQueryService.class);

    private final ReviewRepository reviewRepository;

    private final ReviewMapper reviewMapper;

    private final ReviewSearchRepository reviewSearchRepository;

    public ReviewQueryService(ReviewRepository reviewRepository, ReviewMapper reviewMapper, ReviewSearchRepository reviewSearchRepository) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
        this.reviewSearchRepository = reviewSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ReviewDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ReviewDTO> findByCriteria(ReviewCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Review> specification = createSpecification(criteria);
        return reviewMapper.toDto(reviewRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ReviewDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReviewDTO> findByCriteria(ReviewCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Review> specification = createSpecification(criteria);
        return reviewRepository.findAll(specification, page)
            .map(reviewMapper::toDto);
    }

    /**
     * Function to convert ReviewCriteria to a {@link Specification}
     */
    private Specification<Review> createSpecification(ReviewCriteria criteria) {
        Specification<Review> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Review_.id));
            }
            if (criteria.getProductID() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProductID(), Review_.productID));
            }
            if (criteria.getUid() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUid(), Review_.uid));
            }
            if (criteria.getProductURL() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProductURL(), Review_.productURL));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Review_.title));
            }
            if (criteria.getReviewContent() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReviewContent(), Review_.reviewContent));
            }
            if (criteria.getLanguage() != null) {
                specification = specification.and(buildSpecification(criteria.getLanguage(), Review_.language));
            }
            if (criteria.getReviewDateTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReviewDateTime(), Review_.reviewDateTime));
            }
            if (criteria.getRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRating(), Review_.rating));
            }
            if (criteria.getFullRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFullRating(), Review_.fullRating));
            }
            if (criteria.getReviewStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getReviewStatus(), Review_.reviewStatus));
            }
            if (criteria.getHelpfulVotes() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHelpfulVotes(), Review_.helpfulVotes));
            }
            if (criteria.getTotalVotes() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalVotes(), Review_.totalVotes));
            }
            if (criteria.getVerifiedPurchase() != null) {
                specification = specification.and(buildSpecification(criteria.getVerifiedPurchase(), Review_.verifiedPurchase));
            }
            if (criteria.getRealName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRealName(), Review_.realName));
            }
            if (criteria.getReviewAnalysisId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getReviewAnalysisId(), Review_.reviewAnalysis, ReviewAnalysis_.id));
            }
            if (criteria.getReviewerId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getReviewerId(), Review_.reviewers, Reviewer_.id));
            }
            if (criteria.getProductId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProductId(), Review_.products, Product_.id));
            }
        }
        return specification;
    }

}
