package com.insightsystems.ratinginsight.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.insightsystems.ratinginsight.service.ReviewerProfileService;
import com.insightsystems.ratinginsight.web.rest.errors.BadRequestAlertException;
import com.insightsystems.ratinginsight.web.rest.util.HeaderUtil;
import com.insightsystems.ratinginsight.web.rest.util.PaginationUtil;
import com.insightsystems.ratinginsight.service.dto.ReviewerProfileDTO;
import com.insightsystems.ratinginsight.service.dto.ReviewerProfileCriteria;
import com.insightsystems.ratinginsight.service.ReviewerProfileQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ReviewerProfile.
 */
@RestController
@RequestMapping("/api")
public class ReviewerProfileResource {

    private final Logger log = LoggerFactory.getLogger(ReviewerProfileResource.class);

    private static final String ENTITY_NAME = "reviewerProfile";

    private final ReviewerProfileService reviewerProfileService;

    private final ReviewerProfileQueryService reviewerProfileQueryService;

    public ReviewerProfileResource(ReviewerProfileService reviewerProfileService, ReviewerProfileQueryService reviewerProfileQueryService) {
        this.reviewerProfileService = reviewerProfileService;
        this.reviewerProfileQueryService = reviewerProfileQueryService;
    }

    /**
     * POST  /reviewer-profiles : Create a new reviewerProfile.
     *
     * @param reviewerProfileDTO the reviewerProfileDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new reviewerProfileDTO, or with status 400 (Bad Request) if the reviewerProfile has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/reviewer-profiles")
    @Timed
    public ResponseEntity<ReviewerProfileDTO> createReviewerProfile(@RequestBody ReviewerProfileDTO reviewerProfileDTO) throws URISyntaxException {
        log.debug("REST request to save ReviewerProfile : {}", reviewerProfileDTO);
        if (reviewerProfileDTO.getId() != null) {
            throw new BadRequestAlertException("A new reviewerProfile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReviewerProfileDTO result = reviewerProfileService.save(reviewerProfileDTO);
        return ResponseEntity.created(new URI("/api/reviewer-profiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /reviewer-profiles : Updates an existing reviewerProfile.
     *
     * @param reviewerProfileDTO the reviewerProfileDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated reviewerProfileDTO,
     * or with status 400 (Bad Request) if the reviewerProfileDTO is not valid,
     * or with status 500 (Internal Server Error) if the reviewerProfileDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/reviewer-profiles")
    @Timed
    public ResponseEntity<ReviewerProfileDTO> updateReviewerProfile(@RequestBody ReviewerProfileDTO reviewerProfileDTO) throws URISyntaxException {
        log.debug("REST request to update ReviewerProfile : {}", reviewerProfileDTO);
        if (reviewerProfileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ReviewerProfileDTO result = reviewerProfileService.save(reviewerProfileDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, reviewerProfileDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /reviewer-profiles : get all the reviewerProfiles.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of reviewerProfiles in body
     */
    @GetMapping("/reviewer-profiles")
    @Timed
    public ResponseEntity<List<ReviewerProfileDTO>> getAllReviewerProfiles(ReviewerProfileCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ReviewerProfiles by criteria: {}", criteria);
        Page<ReviewerProfileDTO> page = reviewerProfileQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/reviewer-profiles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /reviewer-profiles/:id : get the "id" reviewerProfile.
     *
     * @param id the id of the reviewerProfileDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the reviewerProfileDTO, or with status 404 (Not Found)
     */
    @GetMapping("/reviewer-profiles/{id}")
    @Timed
    public ResponseEntity<ReviewerProfileDTO> getReviewerProfile(@PathVariable Long id) {
        log.debug("REST request to get ReviewerProfile : {}", id);
        Optional<ReviewerProfileDTO> reviewerProfileDTO = reviewerProfileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reviewerProfileDTO);
    }

    /**
     * DELETE  /reviewer-profiles/:id : delete the "id" reviewerProfile.
     *
     * @param id the id of the reviewerProfileDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/reviewer-profiles/{id}")
    @Timed
    public ResponseEntity<Void> deleteReviewerProfile(@PathVariable Long id) {
        log.debug("REST request to delete ReviewerProfile : {}", id);
        reviewerProfileService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/reviewer-profiles?query=:query : search for the reviewerProfile corresponding
     * to the query.
     *
     * @param query the query of the reviewerProfile search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/reviewer-profiles")
    @Timed
    public ResponseEntity<List<ReviewerProfileDTO>> searchReviewerProfiles(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ReviewerProfiles for query {}", query);
        Page<ReviewerProfileDTO> page = reviewerProfileService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/reviewer-profiles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
