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

import com.insightsystems.ratinginsight.domain.Reviewer;
import com.insightsystems.ratinginsight.domain.*; // for static metamodels
import com.insightsystems.ratinginsight.repository.ReviewerRepository;
import com.insightsystems.ratinginsight.repository.search.ReviewerSearchRepository;
import com.insightsystems.ratinginsight.service.dto.ReviewerCriteria;

import com.insightsystems.ratinginsight.service.dto.ReviewerDTO;
import com.insightsystems.ratinginsight.service.mapper.ReviewerMapper;

/**
 * Service for executing complex queries for Reviewer entities in the database.
 * The main input is a {@link ReviewerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ReviewerDTO} or a {@link Page} of {@link ReviewerDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReviewerQueryService extends QueryService<Reviewer> {

    private final Logger log = LoggerFactory.getLogger(ReviewerQueryService.class);

    private final ReviewerRepository reviewerRepository;

    private final ReviewerMapper reviewerMapper;

    private final ReviewerSearchRepository reviewerSearchRepository;

    public ReviewerQueryService(ReviewerRepository reviewerRepository, ReviewerMapper reviewerMapper, ReviewerSearchRepository reviewerSearchRepository) {
        this.reviewerRepository = reviewerRepository;
        this.reviewerMapper = reviewerMapper;
        this.reviewerSearchRepository = reviewerSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ReviewerDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ReviewerDTO> findByCriteria(ReviewerCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Reviewer> specification = createSpecification(criteria);
        return reviewerMapper.toDto(reviewerRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ReviewerDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReviewerDTO> findByCriteria(ReviewerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Reviewer> specification = createSpecification(criteria);
        return reviewerRepository.findAll(specification, page)
            .map(reviewerMapper::toDto);
    }

    /**
     * Function to convert ReviewerCriteria to a {@link Specification}
     */
    private Specification<Reviewer> createSpecification(ReviewerCriteria criteria) {
        Specification<Reviewer> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Reviewer_.id));
            }
            if (criteria.getReviewerID() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReviewerID(), Reviewer_.reviewerID));
            }
            if (criteria.getUid() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUid(), Reviewer_.uid));
            }
            if (criteria.getUsername() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUsername(), Reviewer_.username));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Reviewer_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Reviewer_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Reviewer_.email));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), Reviewer_.phoneNumber));
            }
            if (criteria.getStreetAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStreetAddress(), Reviewer_.streetAddress));
            }
            if (criteria.getPostalCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPostalCode(), Reviewer_.postalCode));
            }
            if (criteria.getCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCity(), Reviewer_.city));
            }
            if (criteria.getStateProvince() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStateProvince(), Reviewer_.stateProvince));
            }
            if (criteria.getProfileId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProfileId(), Reviewer_.profile, ReviewerProfile_.id));
            }
            if (criteria.getReviewId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getReviewId(), Reviewer_.reviews, Review_.id));
            }
        }
        return specification;
    }

}
