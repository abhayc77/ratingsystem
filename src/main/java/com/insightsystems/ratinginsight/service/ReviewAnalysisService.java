package com.insightsystems.ratinginsight.service;

import com.insightsystems.ratinginsight.domain.ReviewAnalysis;
import com.insightsystems.ratinginsight.repository.ReviewAnalysisRepository;
import com.insightsystems.ratinginsight.repository.search.ReviewAnalysisSearchRepository;
import com.insightsystems.ratinginsight.service.dto.ReviewAnalysisDTO;
import com.insightsystems.ratinginsight.service.mapper.ReviewAnalysisMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ReviewAnalysis.
 */
@Service
@Transactional
public class ReviewAnalysisService {

    private final Logger log = LoggerFactory.getLogger(ReviewAnalysisService.class);

    private final ReviewAnalysisRepository reviewAnalysisRepository;

    private final ReviewAnalysisMapper reviewAnalysisMapper;

    private final ReviewAnalysisSearchRepository reviewAnalysisSearchRepository;

    public ReviewAnalysisService(ReviewAnalysisRepository reviewAnalysisRepository, ReviewAnalysisMapper reviewAnalysisMapper, ReviewAnalysisSearchRepository reviewAnalysisSearchRepository) {
        this.reviewAnalysisRepository = reviewAnalysisRepository;
        this.reviewAnalysisMapper = reviewAnalysisMapper;
        this.reviewAnalysisSearchRepository = reviewAnalysisSearchRepository;
    }

    /**
     * Save a reviewAnalysis.
     *
     * @param reviewAnalysisDTO the entity to save
     * @return the persisted entity
     */
    public ReviewAnalysisDTO save(ReviewAnalysisDTO reviewAnalysisDTO) {
        log.debug("Request to save ReviewAnalysis : {}", reviewAnalysisDTO);
        ReviewAnalysis reviewAnalysis = reviewAnalysisMapper.toEntity(reviewAnalysisDTO);
        reviewAnalysis = reviewAnalysisRepository.save(reviewAnalysis);
        ReviewAnalysisDTO result = reviewAnalysisMapper.toDto(reviewAnalysis);
        reviewAnalysisSearchRepository.save(reviewAnalysis);
        return result;
    }

    /**
     * Get all the reviewAnalyses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ReviewAnalysisDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ReviewAnalyses");
        return reviewAnalysisRepository.findAll(pageable)
            .map(reviewAnalysisMapper::toDto);
    }


    /**
     * Get one reviewAnalysis by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ReviewAnalysisDTO> findOne(Long id) {
        log.debug("Request to get ReviewAnalysis : {}", id);
        return reviewAnalysisRepository.findById(id)
            .map(reviewAnalysisMapper::toDto);
    }

    /**
     * Delete the reviewAnalysis by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ReviewAnalysis : {}", id);
        reviewAnalysisRepository.deleteById(id);
        reviewAnalysisSearchRepository.deleteById(id);
    }

    /**
     * Search for the reviewAnalysis corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ReviewAnalysisDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ReviewAnalyses for query {}", query);
        return reviewAnalysisSearchRepository.search(queryStringQuery(query), pageable)
            .map(reviewAnalysisMapper::toDto);
    }
}
