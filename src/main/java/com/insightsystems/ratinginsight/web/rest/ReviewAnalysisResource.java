package com.insightsystems.ratinginsight.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.insightsystems.ratinginsight.service.ReviewAnalysisService;
import com.insightsystems.ratinginsight.web.rest.errors.BadRequestAlertException;
import com.insightsystems.ratinginsight.web.rest.util.HeaderUtil;
import com.insightsystems.ratinginsight.web.rest.util.PaginationUtil;
import com.insightsystems.ratinginsight.service.dto.ReviewAnalysisDTO;
import com.insightsystems.ratinginsight.service.dto.ReviewAnalysisCriteria;
import com.insightsystems.ratinginsight.service.ReviewAnalysisQueryService;
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
 * REST controller for managing ReviewAnalysis.
 */
@RestController
@RequestMapping("/api")
public class ReviewAnalysisResource {

    private final Logger log = LoggerFactory.getLogger(ReviewAnalysisResource.class);

    private static final String ENTITY_NAME = "reviewAnalysis";

    private final ReviewAnalysisService reviewAnalysisService;

    private final ReviewAnalysisQueryService reviewAnalysisQueryService;

    public ReviewAnalysisResource(ReviewAnalysisService reviewAnalysisService, ReviewAnalysisQueryService reviewAnalysisQueryService) {
        this.reviewAnalysisService = reviewAnalysisService;
        this.reviewAnalysisQueryService = reviewAnalysisQueryService;
    }

    /**
     * POST  /review-analyses : Create a new reviewAnalysis.
     *
     * @param reviewAnalysisDTO the reviewAnalysisDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new reviewAnalysisDTO, or with status 400 (Bad Request) if the reviewAnalysis has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/review-analyses")
    @Timed
    public ResponseEntity<ReviewAnalysisDTO> createReviewAnalysis(@RequestBody ReviewAnalysisDTO reviewAnalysisDTO) throws URISyntaxException {
        log.debug("REST request to save ReviewAnalysis : {}", reviewAnalysisDTO);
        if (reviewAnalysisDTO.getId() != null) {
            throw new BadRequestAlertException("A new reviewAnalysis cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReviewAnalysisDTO result = reviewAnalysisService.save(reviewAnalysisDTO);
        return ResponseEntity.created(new URI("/api/review-analyses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /review-analyses : Updates an existing reviewAnalysis.
     *
     * @param reviewAnalysisDTO the reviewAnalysisDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated reviewAnalysisDTO,
     * or with status 400 (Bad Request) if the reviewAnalysisDTO is not valid,
     * or with status 500 (Internal Server Error) if the reviewAnalysisDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/review-analyses")
    @Timed
    public ResponseEntity<ReviewAnalysisDTO> updateReviewAnalysis(@RequestBody ReviewAnalysisDTO reviewAnalysisDTO) throws URISyntaxException {
        log.debug("REST request to update ReviewAnalysis : {}", reviewAnalysisDTO);
        if (reviewAnalysisDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ReviewAnalysisDTO result = reviewAnalysisService.save(reviewAnalysisDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, reviewAnalysisDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /review-analyses : get all the reviewAnalyses.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of reviewAnalyses in body
     */
    @GetMapping("/review-analyses")
    @Timed
    public ResponseEntity<List<ReviewAnalysisDTO>> getAllReviewAnalyses(ReviewAnalysisCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ReviewAnalyses by criteria: {}", criteria);
        Page<ReviewAnalysisDTO> page = reviewAnalysisQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/review-analyses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /review-analyses/:id : get the "id" reviewAnalysis.
     *
     * @param id the id of the reviewAnalysisDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the reviewAnalysisDTO, or with status 404 (Not Found)
     */
    @GetMapping("/review-analyses/{id}")
    @Timed
    public ResponseEntity<ReviewAnalysisDTO> getReviewAnalysis(@PathVariable Long id) {
        log.debug("REST request to get ReviewAnalysis : {}", id);
        Optional<ReviewAnalysisDTO> reviewAnalysisDTO = reviewAnalysisService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reviewAnalysisDTO);
    }

    /**
     * DELETE  /review-analyses/:id : delete the "id" reviewAnalysis.
     *
     * @param id the id of the reviewAnalysisDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/review-analyses/{id}")
    @Timed
    public ResponseEntity<Void> deleteReviewAnalysis(@PathVariable Long id) {
        log.debug("REST request to delete ReviewAnalysis : {}", id);
        reviewAnalysisService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/review-analyses?query=:query : search for the reviewAnalysis corresponding
     * to the query.
     *
     * @param query the query of the reviewAnalysis search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/review-analyses")
    @Timed
    public ResponseEntity<List<ReviewAnalysisDTO>> searchReviewAnalyses(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ReviewAnalyses for query {}", query);
        Page<ReviewAnalysisDTO> page = reviewAnalysisService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/review-analyses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
