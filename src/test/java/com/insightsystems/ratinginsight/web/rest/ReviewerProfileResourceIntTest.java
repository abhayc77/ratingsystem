package com.insightsystems.ratinginsight.web.rest;

import com.insightsystems.ratinginsight.RatingSystemApp;

import com.insightsystems.ratinginsight.domain.ReviewerProfile;
import com.insightsystems.ratinginsight.domain.Reviewer;
import com.insightsystems.ratinginsight.repository.ReviewerProfileRepository;
import com.insightsystems.ratinginsight.repository.search.ReviewerProfileSearchRepository;
import com.insightsystems.ratinginsight.service.ReviewerProfileService;
import com.insightsystems.ratinginsight.service.dto.ReviewerProfileDTO;
import com.insightsystems.ratinginsight.service.mapper.ReviewerProfileMapper;
import com.insightsystems.ratinginsight.web.rest.errors.ExceptionTranslator;
import com.insightsystems.ratinginsight.service.dto.ReviewerProfileCriteria;
import com.insightsystems.ratinginsight.service.ReviewerProfileQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static com.insightsystems.ratinginsight.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ReviewerProfileResource REST controller.
 *
 * @see ReviewerProfileResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RatingSystemApp.class)
public class ReviewerProfileResourceIntTest {

    private static final Long DEFAULT_TOTAL_REVIEWS = 1L;
    private static final Long UPDATED_TOTAL_REVIEWS = 2L;

    private static final Float DEFAULT_REVIEWER_RANKING = 1F;
    private static final Float UPDATED_REVIEWER_RANKING = 2F;

    private static final Long DEFAULT_TOTAL_HELPFUL_VOTES = 1L;
    private static final Long UPDATED_TOTAL_HELPFUL_VOTES = 2L;

    private static final String DEFAULT_RECENT_RATING = "AAAAAAAAAA";
    private static final String UPDATED_RECENT_RATING = "BBBBBBBBBB";

    @Autowired
    private ReviewerProfileRepository reviewerProfileRepository;


    @Autowired
    private ReviewerProfileMapper reviewerProfileMapper;
    

    @Autowired
    private ReviewerProfileService reviewerProfileService;

    /**
     * This repository is mocked in the com.insightsystems.ratinginsight.repository.search test package.
     *
     * @see com.insightsystems.ratinginsight.repository.search.ReviewerProfileSearchRepositoryMockConfiguration
     */
    @Autowired
    private ReviewerProfileSearchRepository mockReviewerProfileSearchRepository;

    @Autowired
    private ReviewerProfileQueryService reviewerProfileQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restReviewerProfileMockMvc;

    private ReviewerProfile reviewerProfile;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ReviewerProfileResource reviewerProfileResource = new ReviewerProfileResource(reviewerProfileService, reviewerProfileQueryService);
        this.restReviewerProfileMockMvc = MockMvcBuilders.standaloneSetup(reviewerProfileResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReviewerProfile createEntity(EntityManager em) {
        ReviewerProfile reviewerProfile = new ReviewerProfile()
            .totalReviews(DEFAULT_TOTAL_REVIEWS)
            .reviewerRanking(DEFAULT_REVIEWER_RANKING)
            .totalHelpfulVotes(DEFAULT_TOTAL_HELPFUL_VOTES)
            .recentRating(DEFAULT_RECENT_RATING);
        return reviewerProfile;
    }

    @Before
    public void initTest() {
        reviewerProfile = createEntity(em);
    }

    @Test
    @Transactional
    public void createReviewerProfile() throws Exception {
        int databaseSizeBeforeCreate = reviewerProfileRepository.findAll().size();

        // Create the ReviewerProfile
        ReviewerProfileDTO reviewerProfileDTO = reviewerProfileMapper.toDto(reviewerProfile);
        restReviewerProfileMockMvc.perform(post("/api/reviewer-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reviewerProfileDTO)))
            .andExpect(status().isCreated());

        // Validate the ReviewerProfile in the database
        List<ReviewerProfile> reviewerProfileList = reviewerProfileRepository.findAll();
        assertThat(reviewerProfileList).hasSize(databaseSizeBeforeCreate + 1);
        ReviewerProfile testReviewerProfile = reviewerProfileList.get(reviewerProfileList.size() - 1);
        assertThat(testReviewerProfile.getTotalReviews()).isEqualTo(DEFAULT_TOTAL_REVIEWS);
        assertThat(testReviewerProfile.getReviewerRanking()).isEqualTo(DEFAULT_REVIEWER_RANKING);
        assertThat(testReviewerProfile.getTotalHelpfulVotes()).isEqualTo(DEFAULT_TOTAL_HELPFUL_VOTES);
        assertThat(testReviewerProfile.getRecentRating()).isEqualTo(DEFAULT_RECENT_RATING);

        // Validate the ReviewerProfile in Elasticsearch
        verify(mockReviewerProfileSearchRepository, times(1)).save(testReviewerProfile);
    }

    @Test
    @Transactional
    public void createReviewerProfileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reviewerProfileRepository.findAll().size();

        // Create the ReviewerProfile with an existing ID
        reviewerProfile.setId(1L);
        ReviewerProfileDTO reviewerProfileDTO = reviewerProfileMapper.toDto(reviewerProfile);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReviewerProfileMockMvc.perform(post("/api/reviewer-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reviewerProfileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReviewerProfile in the database
        List<ReviewerProfile> reviewerProfileList = reviewerProfileRepository.findAll();
        assertThat(reviewerProfileList).hasSize(databaseSizeBeforeCreate);

        // Validate the ReviewerProfile in Elasticsearch
        verify(mockReviewerProfileSearchRepository, times(0)).save(reviewerProfile);
    }

    @Test
    @Transactional
    public void getAllReviewerProfiles() throws Exception {
        // Initialize the database
        reviewerProfileRepository.saveAndFlush(reviewerProfile);

        // Get all the reviewerProfileList
        restReviewerProfileMockMvc.perform(get("/api/reviewer-profiles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reviewerProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].totalReviews").value(hasItem(DEFAULT_TOTAL_REVIEWS.intValue())))
            .andExpect(jsonPath("$.[*].reviewerRanking").value(hasItem(DEFAULT_REVIEWER_RANKING.doubleValue())))
            .andExpect(jsonPath("$.[*].totalHelpfulVotes").value(hasItem(DEFAULT_TOTAL_HELPFUL_VOTES.intValue())))
            .andExpect(jsonPath("$.[*].recentRating").value(hasItem(DEFAULT_RECENT_RATING.toString())));
    }
    

    @Test
    @Transactional
    public void getReviewerProfile() throws Exception {
        // Initialize the database
        reviewerProfileRepository.saveAndFlush(reviewerProfile);

        // Get the reviewerProfile
        restReviewerProfileMockMvc.perform(get("/api/reviewer-profiles/{id}", reviewerProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reviewerProfile.getId().intValue()))
            .andExpect(jsonPath("$.totalReviews").value(DEFAULT_TOTAL_REVIEWS.intValue()))
            .andExpect(jsonPath("$.reviewerRanking").value(DEFAULT_REVIEWER_RANKING.doubleValue()))
            .andExpect(jsonPath("$.totalHelpfulVotes").value(DEFAULT_TOTAL_HELPFUL_VOTES.intValue()))
            .andExpect(jsonPath("$.recentRating").value(DEFAULT_RECENT_RATING.toString()));
    }

    @Test
    @Transactional
    public void getAllReviewerProfilesByTotalReviewsIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewerProfileRepository.saveAndFlush(reviewerProfile);

        // Get all the reviewerProfileList where totalReviews equals to DEFAULT_TOTAL_REVIEWS
        defaultReviewerProfileShouldBeFound("totalReviews.equals=" + DEFAULT_TOTAL_REVIEWS);

        // Get all the reviewerProfileList where totalReviews equals to UPDATED_TOTAL_REVIEWS
        defaultReviewerProfileShouldNotBeFound("totalReviews.equals=" + UPDATED_TOTAL_REVIEWS);
    }

    @Test
    @Transactional
    public void getAllReviewerProfilesByTotalReviewsIsInShouldWork() throws Exception {
        // Initialize the database
        reviewerProfileRepository.saveAndFlush(reviewerProfile);

        // Get all the reviewerProfileList where totalReviews in DEFAULT_TOTAL_REVIEWS or UPDATED_TOTAL_REVIEWS
        defaultReviewerProfileShouldBeFound("totalReviews.in=" + DEFAULT_TOTAL_REVIEWS + "," + UPDATED_TOTAL_REVIEWS);

        // Get all the reviewerProfileList where totalReviews equals to UPDATED_TOTAL_REVIEWS
        defaultReviewerProfileShouldNotBeFound("totalReviews.in=" + UPDATED_TOTAL_REVIEWS);
    }

    @Test
    @Transactional
    public void getAllReviewerProfilesByTotalReviewsIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewerProfileRepository.saveAndFlush(reviewerProfile);

        // Get all the reviewerProfileList where totalReviews is not null
        defaultReviewerProfileShouldBeFound("totalReviews.specified=true");

        // Get all the reviewerProfileList where totalReviews is null
        defaultReviewerProfileShouldNotBeFound("totalReviews.specified=false");
    }

    @Test
    @Transactional
    public void getAllReviewerProfilesByTotalReviewsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reviewerProfileRepository.saveAndFlush(reviewerProfile);

        // Get all the reviewerProfileList where totalReviews greater than or equals to DEFAULT_TOTAL_REVIEWS
        defaultReviewerProfileShouldBeFound("totalReviews.greaterOrEqualThan=" + DEFAULT_TOTAL_REVIEWS);

        // Get all the reviewerProfileList where totalReviews greater than or equals to UPDATED_TOTAL_REVIEWS
        defaultReviewerProfileShouldNotBeFound("totalReviews.greaterOrEqualThan=" + UPDATED_TOTAL_REVIEWS);
    }

    @Test
    @Transactional
    public void getAllReviewerProfilesByTotalReviewsIsLessThanSomething() throws Exception {
        // Initialize the database
        reviewerProfileRepository.saveAndFlush(reviewerProfile);

        // Get all the reviewerProfileList where totalReviews less than or equals to DEFAULT_TOTAL_REVIEWS
        defaultReviewerProfileShouldNotBeFound("totalReviews.lessThan=" + DEFAULT_TOTAL_REVIEWS);

        // Get all the reviewerProfileList where totalReviews less than or equals to UPDATED_TOTAL_REVIEWS
        defaultReviewerProfileShouldBeFound("totalReviews.lessThan=" + UPDATED_TOTAL_REVIEWS);
    }


    @Test
    @Transactional
    public void getAllReviewerProfilesByReviewerRankingIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewerProfileRepository.saveAndFlush(reviewerProfile);

        // Get all the reviewerProfileList where reviewerRanking equals to DEFAULT_REVIEWER_RANKING
        defaultReviewerProfileShouldBeFound("reviewerRanking.equals=" + DEFAULT_REVIEWER_RANKING);

        // Get all the reviewerProfileList where reviewerRanking equals to UPDATED_REVIEWER_RANKING
        defaultReviewerProfileShouldNotBeFound("reviewerRanking.equals=" + UPDATED_REVIEWER_RANKING);
    }

    @Test
    @Transactional
    public void getAllReviewerProfilesByReviewerRankingIsInShouldWork() throws Exception {
        // Initialize the database
        reviewerProfileRepository.saveAndFlush(reviewerProfile);

        // Get all the reviewerProfileList where reviewerRanking in DEFAULT_REVIEWER_RANKING or UPDATED_REVIEWER_RANKING
        defaultReviewerProfileShouldBeFound("reviewerRanking.in=" + DEFAULT_REVIEWER_RANKING + "," + UPDATED_REVIEWER_RANKING);

        // Get all the reviewerProfileList where reviewerRanking equals to UPDATED_REVIEWER_RANKING
        defaultReviewerProfileShouldNotBeFound("reviewerRanking.in=" + UPDATED_REVIEWER_RANKING);
    }

    @Test
    @Transactional
    public void getAllReviewerProfilesByReviewerRankingIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewerProfileRepository.saveAndFlush(reviewerProfile);

        // Get all the reviewerProfileList where reviewerRanking is not null
        defaultReviewerProfileShouldBeFound("reviewerRanking.specified=true");

        // Get all the reviewerProfileList where reviewerRanking is null
        defaultReviewerProfileShouldNotBeFound("reviewerRanking.specified=false");
    }

    @Test
    @Transactional
    public void getAllReviewerProfilesByTotalHelpfulVotesIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewerProfileRepository.saveAndFlush(reviewerProfile);

        // Get all the reviewerProfileList where totalHelpfulVotes equals to DEFAULT_TOTAL_HELPFUL_VOTES
        defaultReviewerProfileShouldBeFound("totalHelpfulVotes.equals=" + DEFAULT_TOTAL_HELPFUL_VOTES);

        // Get all the reviewerProfileList where totalHelpfulVotes equals to UPDATED_TOTAL_HELPFUL_VOTES
        defaultReviewerProfileShouldNotBeFound("totalHelpfulVotes.equals=" + UPDATED_TOTAL_HELPFUL_VOTES);
    }

    @Test
    @Transactional
    public void getAllReviewerProfilesByTotalHelpfulVotesIsInShouldWork() throws Exception {
        // Initialize the database
        reviewerProfileRepository.saveAndFlush(reviewerProfile);

        // Get all the reviewerProfileList where totalHelpfulVotes in DEFAULT_TOTAL_HELPFUL_VOTES or UPDATED_TOTAL_HELPFUL_VOTES
        defaultReviewerProfileShouldBeFound("totalHelpfulVotes.in=" + DEFAULT_TOTAL_HELPFUL_VOTES + "," + UPDATED_TOTAL_HELPFUL_VOTES);

        // Get all the reviewerProfileList where totalHelpfulVotes equals to UPDATED_TOTAL_HELPFUL_VOTES
        defaultReviewerProfileShouldNotBeFound("totalHelpfulVotes.in=" + UPDATED_TOTAL_HELPFUL_VOTES);
    }

    @Test
    @Transactional
    public void getAllReviewerProfilesByTotalHelpfulVotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewerProfileRepository.saveAndFlush(reviewerProfile);

        // Get all the reviewerProfileList where totalHelpfulVotes is not null
        defaultReviewerProfileShouldBeFound("totalHelpfulVotes.specified=true");

        // Get all the reviewerProfileList where totalHelpfulVotes is null
        defaultReviewerProfileShouldNotBeFound("totalHelpfulVotes.specified=false");
    }

    @Test
    @Transactional
    public void getAllReviewerProfilesByTotalHelpfulVotesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reviewerProfileRepository.saveAndFlush(reviewerProfile);

        // Get all the reviewerProfileList where totalHelpfulVotes greater than or equals to DEFAULT_TOTAL_HELPFUL_VOTES
        defaultReviewerProfileShouldBeFound("totalHelpfulVotes.greaterOrEqualThan=" + DEFAULT_TOTAL_HELPFUL_VOTES);

        // Get all the reviewerProfileList where totalHelpfulVotes greater than or equals to UPDATED_TOTAL_HELPFUL_VOTES
        defaultReviewerProfileShouldNotBeFound("totalHelpfulVotes.greaterOrEqualThan=" + UPDATED_TOTAL_HELPFUL_VOTES);
    }

    @Test
    @Transactional
    public void getAllReviewerProfilesByTotalHelpfulVotesIsLessThanSomething() throws Exception {
        // Initialize the database
        reviewerProfileRepository.saveAndFlush(reviewerProfile);

        // Get all the reviewerProfileList where totalHelpfulVotes less than or equals to DEFAULT_TOTAL_HELPFUL_VOTES
        defaultReviewerProfileShouldNotBeFound("totalHelpfulVotes.lessThan=" + DEFAULT_TOTAL_HELPFUL_VOTES);

        // Get all the reviewerProfileList where totalHelpfulVotes less than or equals to UPDATED_TOTAL_HELPFUL_VOTES
        defaultReviewerProfileShouldBeFound("totalHelpfulVotes.lessThan=" + UPDATED_TOTAL_HELPFUL_VOTES);
    }


    @Test
    @Transactional
    public void getAllReviewerProfilesByRecentRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewerProfileRepository.saveAndFlush(reviewerProfile);

        // Get all the reviewerProfileList where recentRating equals to DEFAULT_RECENT_RATING
        defaultReviewerProfileShouldBeFound("recentRating.equals=" + DEFAULT_RECENT_RATING);

        // Get all the reviewerProfileList where recentRating equals to UPDATED_RECENT_RATING
        defaultReviewerProfileShouldNotBeFound("recentRating.equals=" + UPDATED_RECENT_RATING);
    }

    @Test
    @Transactional
    public void getAllReviewerProfilesByRecentRatingIsInShouldWork() throws Exception {
        // Initialize the database
        reviewerProfileRepository.saveAndFlush(reviewerProfile);

        // Get all the reviewerProfileList where recentRating in DEFAULT_RECENT_RATING or UPDATED_RECENT_RATING
        defaultReviewerProfileShouldBeFound("recentRating.in=" + DEFAULT_RECENT_RATING + "," + UPDATED_RECENT_RATING);

        // Get all the reviewerProfileList where recentRating equals to UPDATED_RECENT_RATING
        defaultReviewerProfileShouldNotBeFound("recentRating.in=" + UPDATED_RECENT_RATING);
    }

    @Test
    @Transactional
    public void getAllReviewerProfilesByRecentRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewerProfileRepository.saveAndFlush(reviewerProfile);

        // Get all the reviewerProfileList where recentRating is not null
        defaultReviewerProfileShouldBeFound("recentRating.specified=true");

        // Get all the reviewerProfileList where recentRating is null
        defaultReviewerProfileShouldNotBeFound("recentRating.specified=false");
    }

    @Test
    @Transactional
    public void getAllReviewerProfilesByReviewerIsEqualToSomething() throws Exception {
        // Initialize the database
        Reviewer reviewer = ReviewerResourceIntTest.createEntity(em);
        em.persist(reviewer);
        em.flush();
        reviewerProfile.setReviewer(reviewer);
        reviewer.setProfile(reviewerProfile);
        reviewerProfileRepository.saveAndFlush(reviewerProfile);
        Long reviewerId = reviewer.getId();

        // Get all the reviewerProfileList where reviewer equals to reviewerId
        defaultReviewerProfileShouldBeFound("reviewerId.equals=" + reviewerId);

        // Get all the reviewerProfileList where reviewer equals to reviewerId + 1
        defaultReviewerProfileShouldNotBeFound("reviewerId.equals=" + (reviewerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultReviewerProfileShouldBeFound(String filter) throws Exception {
        restReviewerProfileMockMvc.perform(get("/api/reviewer-profiles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reviewerProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].totalReviews").value(hasItem(DEFAULT_TOTAL_REVIEWS.intValue())))
            .andExpect(jsonPath("$.[*].reviewerRanking").value(hasItem(DEFAULT_REVIEWER_RANKING.doubleValue())))
            .andExpect(jsonPath("$.[*].totalHelpfulVotes").value(hasItem(DEFAULT_TOTAL_HELPFUL_VOTES.intValue())))
            .andExpect(jsonPath("$.[*].recentRating").value(hasItem(DEFAULT_RECENT_RATING.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultReviewerProfileShouldNotBeFound(String filter) throws Exception {
        restReviewerProfileMockMvc.perform(get("/api/reviewer-profiles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingReviewerProfile() throws Exception {
        // Get the reviewerProfile
        restReviewerProfileMockMvc.perform(get("/api/reviewer-profiles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReviewerProfile() throws Exception {
        // Initialize the database
        reviewerProfileRepository.saveAndFlush(reviewerProfile);

        int databaseSizeBeforeUpdate = reviewerProfileRepository.findAll().size();

        // Update the reviewerProfile
        ReviewerProfile updatedReviewerProfile = reviewerProfileRepository.findById(reviewerProfile.getId()).get();
        // Disconnect from session so that the updates on updatedReviewerProfile are not directly saved in db
        em.detach(updatedReviewerProfile);
        updatedReviewerProfile
            .totalReviews(UPDATED_TOTAL_REVIEWS)
            .reviewerRanking(UPDATED_REVIEWER_RANKING)
            .totalHelpfulVotes(UPDATED_TOTAL_HELPFUL_VOTES)
            .recentRating(UPDATED_RECENT_RATING);
        ReviewerProfileDTO reviewerProfileDTO = reviewerProfileMapper.toDto(updatedReviewerProfile);

        restReviewerProfileMockMvc.perform(put("/api/reviewer-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reviewerProfileDTO)))
            .andExpect(status().isOk());

        // Validate the ReviewerProfile in the database
        List<ReviewerProfile> reviewerProfileList = reviewerProfileRepository.findAll();
        assertThat(reviewerProfileList).hasSize(databaseSizeBeforeUpdate);
        ReviewerProfile testReviewerProfile = reviewerProfileList.get(reviewerProfileList.size() - 1);
        assertThat(testReviewerProfile.getTotalReviews()).isEqualTo(UPDATED_TOTAL_REVIEWS);
        assertThat(testReviewerProfile.getReviewerRanking()).isEqualTo(UPDATED_REVIEWER_RANKING);
        assertThat(testReviewerProfile.getTotalHelpfulVotes()).isEqualTo(UPDATED_TOTAL_HELPFUL_VOTES);
        assertThat(testReviewerProfile.getRecentRating()).isEqualTo(UPDATED_RECENT_RATING);

        // Validate the ReviewerProfile in Elasticsearch
        verify(mockReviewerProfileSearchRepository, times(1)).save(testReviewerProfile);
    }

    @Test
    @Transactional
    public void updateNonExistingReviewerProfile() throws Exception {
        int databaseSizeBeforeUpdate = reviewerProfileRepository.findAll().size();

        // Create the ReviewerProfile
        ReviewerProfileDTO reviewerProfileDTO = reviewerProfileMapper.toDto(reviewerProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restReviewerProfileMockMvc.perform(put("/api/reviewer-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reviewerProfileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReviewerProfile in the database
        List<ReviewerProfile> reviewerProfileList = reviewerProfileRepository.findAll();
        assertThat(reviewerProfileList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReviewerProfile in Elasticsearch
        verify(mockReviewerProfileSearchRepository, times(0)).save(reviewerProfile);
    }

    @Test
    @Transactional
    public void deleteReviewerProfile() throws Exception {
        // Initialize the database
        reviewerProfileRepository.saveAndFlush(reviewerProfile);

        int databaseSizeBeforeDelete = reviewerProfileRepository.findAll().size();

        // Get the reviewerProfile
        restReviewerProfileMockMvc.perform(delete("/api/reviewer-profiles/{id}", reviewerProfile.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ReviewerProfile> reviewerProfileList = reviewerProfileRepository.findAll();
        assertThat(reviewerProfileList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ReviewerProfile in Elasticsearch
        verify(mockReviewerProfileSearchRepository, times(1)).deleteById(reviewerProfile.getId());
    }

    @Test
    @Transactional
    public void searchReviewerProfile() throws Exception {
        // Initialize the database
        reviewerProfileRepository.saveAndFlush(reviewerProfile);
        when(mockReviewerProfileSearchRepository.search(queryStringQuery("id:" + reviewerProfile.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(reviewerProfile), PageRequest.of(0, 1), 1));
        // Search the reviewerProfile
        restReviewerProfileMockMvc.perform(get("/api/_search/reviewer-profiles?query=id:" + reviewerProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reviewerProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].totalReviews").value(hasItem(DEFAULT_TOTAL_REVIEWS.intValue())))
            .andExpect(jsonPath("$.[*].reviewerRanking").value(hasItem(DEFAULT_REVIEWER_RANKING.doubleValue())))
            .andExpect(jsonPath("$.[*].totalHelpfulVotes").value(hasItem(DEFAULT_TOTAL_HELPFUL_VOTES.intValue())))
            .andExpect(jsonPath("$.[*].recentRating").value(hasItem(DEFAULT_RECENT_RATING.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReviewerProfile.class);
        ReviewerProfile reviewerProfile1 = new ReviewerProfile();
        reviewerProfile1.setId(1L);
        ReviewerProfile reviewerProfile2 = new ReviewerProfile();
        reviewerProfile2.setId(reviewerProfile1.getId());
        assertThat(reviewerProfile1).isEqualTo(reviewerProfile2);
        reviewerProfile2.setId(2L);
        assertThat(reviewerProfile1).isNotEqualTo(reviewerProfile2);
        reviewerProfile1.setId(null);
        assertThat(reviewerProfile1).isNotEqualTo(reviewerProfile2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReviewerProfileDTO.class);
        ReviewerProfileDTO reviewerProfileDTO1 = new ReviewerProfileDTO();
        reviewerProfileDTO1.setId(1L);
        ReviewerProfileDTO reviewerProfileDTO2 = new ReviewerProfileDTO();
        assertThat(reviewerProfileDTO1).isNotEqualTo(reviewerProfileDTO2);
        reviewerProfileDTO2.setId(reviewerProfileDTO1.getId());
        assertThat(reviewerProfileDTO1).isEqualTo(reviewerProfileDTO2);
        reviewerProfileDTO2.setId(2L);
        assertThat(reviewerProfileDTO1).isNotEqualTo(reviewerProfileDTO2);
        reviewerProfileDTO1.setId(null);
        assertThat(reviewerProfileDTO1).isNotEqualTo(reviewerProfileDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(reviewerProfileMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(reviewerProfileMapper.fromId(null)).isNull();
    }
}
