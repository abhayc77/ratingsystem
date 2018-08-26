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

import com.insightsystems.ratinginsight.domain.ReviewAnalysis;
import com.insightsystems.ratinginsight.domain.*; // for static metamodels
import com.insightsystems.ratinginsight.repository.ReviewAnalysisRepository;
import com.insightsystems.ratinginsight.repository.search.ReviewAnalysisSearchRepository;
import com.insightsystems.ratinginsight.service.dto.ReviewAnalysisCriteria;

import com.insightsystems.ratinginsight.service.dto.ReviewAnalysisDTO;
import com.insightsystems.ratinginsight.service.mapper.ReviewAnalysisMapper;

/**
 * Service for executing complex queries for ReviewAnalysis entities in the database.
 * The main input is a {@link ReviewAnalysisCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ReviewAnalysisDTO} or a {@link Page} of {@link ReviewAnalysisDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReviewAnalysisQueryService extends QueryService<ReviewAnalysis> {

    private final Logger log = LoggerFactory.getLogger(ReviewAnalysisQueryService.class);

    private final ReviewAnalysisRepository reviewAnalysisRepository;

    private final ReviewAnalysisMapper reviewAnalysisMapper;

    private final ReviewAnalysisSearchRepository reviewAnalysisSearchRepository;

    public ReviewAnalysisQueryService(ReviewAnalysisRepository reviewAnalysisRepository, ReviewAnalysisMapper reviewAnalysisMapper, ReviewAnalysisSearchRepository reviewAnalysisSearchRepository) {
        this.reviewAnalysisRepository = reviewAnalysisRepository;
        this.reviewAnalysisMapper = reviewAnalysisMapper;
        this.reviewAnalysisSearchRepository = reviewAnalysisSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ReviewAnalysisDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ReviewAnalysisDTO> findByCriteria(ReviewAnalysisCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ReviewAnalysis> specification = createSpecification(criteria);
        return reviewAnalysisMapper.toDto(reviewAnalysisRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ReviewAnalysisDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReviewAnalysisDTO> findByCriteria(ReviewAnalysisCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ReviewAnalysis> specification = createSpecification(criteria);
        return reviewAnalysisRepository.findAll(specification, page)
            .map(reviewAnalysisMapper::toDto);
    }

    /**
     * Function to convert ReviewAnalysisCriteria to a {@link Specification}
     */
    private Specification<ReviewAnalysis> createSpecification(ReviewAnalysisCriteria criteria) {
        Specification<ReviewAnalysis> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ReviewAnalysis_.id));
            }
            if (criteria.getUid() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUid(), ReviewAnalysis_.uid));
            }
            if (criteria.getSentiment() != null) {
                specification = specification.and(buildSpecification(criteria.getSentiment(), ReviewAnalysis_.sentiment));
            }
            if (criteria.getSentimentValue() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSentimentValue(), ReviewAnalysis_.sentimentValue));
            }
            if (criteria.getReviewAnalysisDateTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReviewAnalysisDateTime(), ReviewAnalysis_.reviewAnalysisDateTime));
            }
            if (criteria.getInsight() != null) {
                specification = specification.and(buildSpecification(criteria.getInsight(), ReviewAnalysis_.insight));
            }
            if (criteria.getReviewInsightData() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReviewInsightData(), ReviewAnalysis_.reviewInsightData));
            }
        }
        return specification;
    }

}
