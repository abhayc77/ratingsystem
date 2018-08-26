package com.insightsystems.ratinginsight.service;

import com.insightsystems.ratinginsight.domain.Reviewer;
import com.insightsystems.ratinginsight.repository.ReviewerRepository;
import com.insightsystems.ratinginsight.repository.search.ReviewerSearchRepository;
import com.insightsystems.ratinginsight.service.dto.ReviewerDTO;
import com.insightsystems.ratinginsight.service.mapper.ReviewerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Reviewer.
 */
@Service
@Transactional
public class ReviewerService {

    private final Logger log = LoggerFactory.getLogger(ReviewerService.class);

    private final ReviewerRepository reviewerRepository;

    private final ReviewerMapper reviewerMapper;

    private final ReviewerSearchRepository reviewerSearchRepository;

    public ReviewerService(ReviewerRepository reviewerRepository, ReviewerMapper reviewerMapper, ReviewerSearchRepository reviewerSearchRepository) {
        this.reviewerRepository = reviewerRepository;
        this.reviewerMapper = reviewerMapper;
        this.reviewerSearchRepository = reviewerSearchRepository;
    }

    /**
     * Save a reviewer.
     *
     * @param reviewerDTO the entity to save
     * @return the persisted entity
     */
    public ReviewerDTO save(ReviewerDTO reviewerDTO) {
        log.debug("Request to save Reviewer : {}", reviewerDTO);
        Reviewer reviewer = reviewerMapper.toEntity(reviewerDTO);
        reviewer = reviewerRepository.save(reviewer);
        ReviewerDTO result = reviewerMapper.toDto(reviewer);
        reviewerSearchRepository.save(reviewer);
        return result;
    }

    /**
     * Get all the reviewers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ReviewerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Reviewers");
        return reviewerRepository.findAll(pageable)
            .map(reviewerMapper::toDto);
    }

    /**
     * Get all the Reviewer with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<ReviewerDTO> findAllWithEagerRelationships(Pageable pageable) {
        return reviewerRepository.findAllWithEagerRelationships(pageable).map(reviewerMapper::toDto);
    }
    

    /**
     * Get one reviewer by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ReviewerDTO> findOne(Long id) {
        log.debug("Request to get Reviewer : {}", id);
        return reviewerRepository.findOneWithEagerRelationships(id)
            .map(reviewerMapper::toDto);
    }

    /**
     * Delete the reviewer by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Reviewer : {}", id);
        reviewerRepository.deleteById(id);
        reviewerSearchRepository.deleteById(id);
    }

    /**
     * Search for the reviewer corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ReviewerDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Reviewers for query {}", query);
        return reviewerSearchRepository.search(queryStringQuery(query), pageable)
            .map(reviewerMapper::toDto);
    }
}
