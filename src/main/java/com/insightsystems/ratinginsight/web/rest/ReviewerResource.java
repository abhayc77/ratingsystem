package com.insightsystems.ratinginsight.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.insightsystems.ratinginsight.service.ReviewerService;
import com.insightsystems.ratinginsight.web.rest.errors.BadRequestAlertException;
import com.insightsystems.ratinginsight.web.rest.util.HeaderUtil;
import com.insightsystems.ratinginsight.web.rest.util.PaginationUtil;
import com.insightsystems.ratinginsight.service.dto.ReviewerDTO;
import com.insightsystems.ratinginsight.service.dto.ReviewerCriteria;
import com.insightsystems.ratinginsight.service.ReviewerQueryService;
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
 * REST controller for managing Reviewer.
 */
@RestController
@RequestMapping("/api")
public class ReviewerResource {

    private final Logger log = LoggerFactory.getLogger(ReviewerResource.class);

    private static final String ENTITY_NAME = "reviewer";

    private final ReviewerService reviewerService;

    private final ReviewerQueryService reviewerQueryService;

    public ReviewerResource(ReviewerService reviewerService, ReviewerQueryService reviewerQueryService) {
        this.reviewerService = reviewerService;
        this.reviewerQueryService = reviewerQueryService;
    }

    /**
     * POST  /reviewers : Create a new reviewer.
     *
     * @param reviewerDTO the reviewerDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new reviewerDTO, or with status 400 (Bad Request) if the reviewer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/reviewers")
    @Timed
    public ResponseEntity<ReviewerDTO> createReviewer(@RequestBody ReviewerDTO reviewerDTO) throws URISyntaxException {
        log.debug("REST request to save Reviewer : {}", reviewerDTO);
        if (reviewerDTO.getId() != null) {
            throw new BadRequestAlertException("A new reviewer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReviewerDTO result = reviewerService.save(reviewerDTO);
        return ResponseEntity.created(new URI("/api/reviewers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /reviewers : Updates an existing reviewer.
     *
     * @param reviewerDTO the reviewerDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated reviewerDTO,
     * or with status 400 (Bad Request) if the reviewerDTO is not valid,
     * or with status 500 (Internal Server Error) if the reviewerDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/reviewers")
    @Timed
    public ResponseEntity<ReviewerDTO> updateReviewer(@RequestBody ReviewerDTO reviewerDTO) throws URISyntaxException {
        log.debug("REST request to update Reviewer : {}", reviewerDTO);
        if (reviewerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ReviewerDTO result = reviewerService.save(reviewerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, reviewerDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /reviewers : get all the reviewers.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of reviewers in body
     */
    @GetMapping("/reviewers")
    @Timed
    public ResponseEntity<List<ReviewerDTO>> getAllReviewers(ReviewerCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Reviewers by criteria: {}", criteria);
        Page<ReviewerDTO> page = reviewerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/reviewers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /reviewers/:id : get the "id" reviewer.
     *
     * @param id the id of the reviewerDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the reviewerDTO, or with status 404 (Not Found)
     */
    @GetMapping("/reviewers/{id}")
    @Timed
    public ResponseEntity<ReviewerDTO> getReviewer(@PathVariable Long id) {
        log.debug("REST request to get Reviewer : {}", id);
        Optional<ReviewerDTO> reviewerDTO = reviewerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reviewerDTO);
    }

    /**
     * DELETE  /reviewers/:id : delete the "id" reviewer.
     *
     * @param id the id of the reviewerDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/reviewers/{id}")
    @Timed
    public ResponseEntity<Void> deleteReviewer(@PathVariable Long id) {
        log.debug("REST request to delete Reviewer : {}", id);
        reviewerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/reviewers?query=:query : search for the reviewer corresponding
     * to the query.
     *
     * @param query the query of the reviewer search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/reviewers")
    @Timed
    public ResponseEntity<List<ReviewerDTO>> searchReviewers(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Reviewers for query {}", query);
        Page<ReviewerDTO> page = reviewerService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/reviewers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
