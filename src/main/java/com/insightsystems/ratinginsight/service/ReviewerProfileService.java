package com.insightsystems.ratinginsight.service;

import com.insightsystems.ratinginsight.domain.ReviewerProfile;
import com.insightsystems.ratinginsight.repository.ReviewerProfileRepository;
import com.insightsystems.ratinginsight.repository.search.ReviewerProfileSearchRepository;
import com.insightsystems.ratinginsight.service.dto.ReviewerProfileDTO;
import com.insightsystems.ratinginsight.service.mapper.ReviewerProfileMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ReviewerProfile.
 */
@Service
@Transactional
public class ReviewerProfileService {

    private final Logger log = LoggerFactory.getLogger(ReviewerProfileService.class);

    private final ReviewerProfileRepository reviewerProfileRepository;

    private final ReviewerProfileMapper reviewerProfileMapper;

    private final ReviewerProfileSearchRepository reviewerProfileSearchRepository;

    public ReviewerProfileService(ReviewerProfileRepository reviewerProfileRepository, ReviewerProfileMapper reviewerProfileMapper, ReviewerProfileSearchRepository reviewerProfileSearchRepository) {
        this.reviewerProfileRepository = reviewerProfileRepository;
        this.reviewerProfileMapper = reviewerProfileMapper;
        this.reviewerProfileSearchRepository = reviewerProfileSearchRepository;
    }

    /**
     * Save a reviewerProfile.
     *
     * @param reviewerProfileDTO the entity to save
     * @return the persisted entity
     */
    public ReviewerProfileDTO save(ReviewerProfileDTO reviewerProfileDTO) {
        log.debug("Request to save ReviewerProfile : {}", reviewerProfileDTO);
        ReviewerProfile reviewerProfile = reviewerProfileMapper.toEntity(reviewerProfileDTO);
        reviewerProfile = reviewerProfileRepository.save(reviewerProfile);
        ReviewerProfileDTO result = reviewerProfileMapper.toDto(reviewerProfile);
        reviewerProfileSearchRepository.save(reviewerProfile);
        return result;
    }

    /**
     * Get all the reviewerProfiles.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ReviewerProfileDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ReviewerProfiles");
        return reviewerProfileRepository.findAll(pageable)
            .map(reviewerProfileMapper::toDto);
    }



    /**
     *  get all the reviewerProfiles where Reviewer is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<ReviewerProfileDTO> findAllWhereReviewerIsNull() {
        log.debug("Request to get all reviewerProfiles where Reviewer is null");
        return StreamSupport
            .stream(reviewerProfileRepository.findAll().spliterator(), false)
            .filter(reviewerProfile -> reviewerProfile.getReviewer() == null)
            .map(reviewerProfileMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one reviewerProfile by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ReviewerProfileDTO> findOne(Long id) {
        log.debug("Request to get ReviewerProfile : {}", id);
        return reviewerProfileRepository.findById(id)
            .map(reviewerProfileMapper::toDto);
    }

    /**
     * Delete the reviewerProfile by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ReviewerProfile : {}", id);
        reviewerProfileRepository.deleteById(id);
        reviewerProfileSearchRepository.deleteById(id);
    }

    /**
     * Search for the reviewerProfile corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ReviewerProfileDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ReviewerProfiles for query {}", query);
        return reviewerProfileSearchRepository.search(queryStringQuery(query), pageable)
            .map(reviewerProfileMapper::toDto);
    }
}
