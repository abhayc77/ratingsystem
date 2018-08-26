package com.insightsystems.ratinginsight.web.rest;

import com.insightsystems.ratinginsight.RatingSystemApp;

import com.insightsystems.ratinginsight.domain.ReviewAnalysis;
import com.insightsystems.ratinginsight.repository.ReviewAnalysisRepository;
import com.insightsystems.ratinginsight.repository.search.ReviewAnalysisSearchRepository;
import com.insightsystems.ratinginsight.service.ReviewAnalysisService;
import com.insightsystems.ratinginsight.service.dto.ReviewAnalysisDTO;
import com.insightsystems.ratinginsight.service.mapper.ReviewAnalysisMapper;
import com.insightsystems.ratinginsight.web.rest.errors.ExceptionTranslator;
import com.insightsystems.ratinginsight.service.dto.ReviewAnalysisCriteria;
import com.insightsystems.ratinginsight.service.ReviewAnalysisQueryService;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;


import static com.insightsystems.ratinginsight.web.rest.TestUtil.sameInstant;
import static com.insightsystems.ratinginsight.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.insightsystems.ratinginsight.domain.enumeration.ReviewSentiment;
import com.insightsystems.ratinginsight.domain.enumeration.ReviewInsight;
/**
 * Test class for the ReviewAnalysisResource REST controller.
 *
 * @see ReviewAnalysisResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RatingSystemApp.class)
public class ReviewAnalysisResourceIntTest {

    private static final String DEFAULT_UID = "AAAAAAAAAA";
    private static final String UPDATED_UID = "BBBBBBBBBB";

    private static final ReviewSentiment DEFAULT_SENTIMENT = ReviewSentiment.POSITIVE;
    private static final ReviewSentiment UPDATED_SENTIMENT = ReviewSentiment.NEUTRAL;

    private static final Float DEFAULT_SENTIMENT_VALUE = 1F;
    private static final Float UPDATED_SENTIMENT_VALUE = 2F;

    private static final ZonedDateTime DEFAULT_REVIEW_ANALYSIS_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_REVIEW_ANALYSIS_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ReviewInsight DEFAULT_INSIGHT = ReviewInsight.UNKKOWN;
    private static final ReviewInsight UPDATED_INSIGHT = ReviewInsight.GENUINE;

    private static final String DEFAULT_REVIEW_INSIGHT_DATA = "AAAAAAAAAA";
    private static final String UPDATED_REVIEW_INSIGHT_DATA = "BBBBBBBBBB";

    @Autowired
    private ReviewAnalysisRepository reviewAnalysisRepository;


    @Autowired
    private ReviewAnalysisMapper reviewAnalysisMapper;
    

    @Autowired
    private ReviewAnalysisService reviewAnalysisService;

    /**
     * This repository is mocked in the com.insightsystems.ratinginsight.repository.search test package.
     *
     * @see com.insightsystems.ratinginsight.repository.search.ReviewAnalysisSearchRepositoryMockConfiguration
     */
    @Autowired
    private ReviewAnalysisSearchRepository mockReviewAnalysisSearchRepository;

    @Autowired
    private ReviewAnalysisQueryService reviewAnalysisQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restReviewAnalysisMockMvc;

    private ReviewAnalysis reviewAnalysis;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ReviewAnalysisResource reviewAnalysisResource = new ReviewAnalysisResource(reviewAnalysisService, reviewAnalysisQueryService);
        this.restReviewAnalysisMockMvc = MockMvcBuilders.standaloneSetup(reviewAnalysisResource)
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
    public static ReviewAnalysis createEntity(EntityManager em) {
        ReviewAnalysis reviewAnalysis = new ReviewAnalysis()
            .uid(DEFAULT_UID)
            .sentiment(DEFAULT_SENTIMENT)
            .sentimentValue(DEFAULT_SENTIMENT_VALUE)
            .reviewAnalysisDateTime(DEFAULT_REVIEW_ANALYSIS_DATE_TIME)
            .insight(DEFAULT_INSIGHT)
            .reviewInsightData(DEFAULT_REVIEW_INSIGHT_DATA);
        return reviewAnalysis;
    }

    @Before
    public void initTest() {
        reviewAnalysis = createEntity(em);
    }

    @Test
    @Transactional
    public void createReviewAnalysis() throws Exception {
        int databaseSizeBeforeCreate = reviewAnalysisRepository.findAll().size();

        // Create the ReviewAnalysis
        ReviewAnalysisDTO reviewAnalysisDTO = reviewAnalysisMapper.toDto(reviewAnalysis);
        restReviewAnalysisMockMvc.perform(post("/api/review-analyses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reviewAnalysisDTO)))
            .andExpect(status().isCreated());

        // Validate the ReviewAnalysis in the database
        List<ReviewAnalysis> reviewAnalysisList = reviewAnalysisRepository.findAll();
        assertThat(reviewAnalysisList).hasSize(databaseSizeBeforeCreate + 1);
        ReviewAnalysis testReviewAnalysis = reviewAnalysisList.get(reviewAnalysisList.size() - 1);
        assertThat(testReviewAnalysis.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testReviewAnalysis.getSentiment()).isEqualTo(DEFAULT_SENTIMENT);
        assertThat(testReviewAnalysis.getSentimentValue()).isEqualTo(DEFAULT_SENTIMENT_VALUE);
        assertThat(testReviewAnalysis.getReviewAnalysisDateTime()).isEqualTo(DEFAULT_REVIEW_ANALYSIS_DATE_TIME);
        assertThat(testReviewAnalysis.getInsight()).isEqualTo(DEFAULT_INSIGHT);
        assertThat(testReviewAnalysis.getReviewInsightData()).isEqualTo(DEFAULT_REVIEW_INSIGHT_DATA);

        // Validate the ReviewAnalysis in Elasticsearch
        verify(mockReviewAnalysisSearchRepository, times(1)).save(testReviewAnalysis);
    }

    @Test
    @Transactional
    public void createReviewAnalysisWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reviewAnalysisRepository.findAll().size();

        // Create the ReviewAnalysis with an existing ID
        reviewAnalysis.setId(1L);
        ReviewAnalysisDTO reviewAnalysisDTO = reviewAnalysisMapper.toDto(reviewAnalysis);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReviewAnalysisMockMvc.perform(post("/api/review-analyses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reviewAnalysisDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReviewAnalysis in the database
        List<ReviewAnalysis> reviewAnalysisList = reviewAnalysisRepository.findAll();
        assertThat(reviewAnalysisList).hasSize(databaseSizeBeforeCreate);

        // Validate the ReviewAnalysis in Elasticsearch
        verify(mockReviewAnalysisSearchRepository, times(0)).save(reviewAnalysis);
    }

    @Test
    @Transactional
    public void getAllReviewAnalyses() throws Exception {
        // Initialize the database
        reviewAnalysisRepository.saveAndFlush(reviewAnalysis);

        // Get all the reviewAnalysisList
        restReviewAnalysisMockMvc.perform(get("/api/review-analyses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reviewAnalysis.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
            .andExpect(jsonPath("$.[*].sentiment").value(hasItem(DEFAULT_SENTIMENT.toString())))
            .andExpect(jsonPath("$.[*].sentimentValue").value(hasItem(DEFAULT_SENTIMENT_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].reviewAnalysisDateTime").value(hasItem(sameInstant(DEFAULT_REVIEW_ANALYSIS_DATE_TIME))))
            .andExpect(jsonPath("$.[*].insight").value(hasItem(DEFAULT_INSIGHT.toString())))
            .andExpect(jsonPath("$.[*].reviewInsightData").value(hasItem(DEFAULT_REVIEW_INSIGHT_DATA.toString())));
    }
    

    @Test
    @Transactional
    public void getReviewAnalysis() throws Exception {
        // Initialize the database
        reviewAnalysisRepository.saveAndFlush(reviewAnalysis);

        // Get the reviewAnalysis
        restReviewAnalysisMockMvc.perform(get("/api/review-analyses/{id}", reviewAnalysis.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reviewAnalysis.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID.toString()))
            .andExpect(jsonPath("$.sentiment").value(DEFAULT_SENTIMENT.toString()))
            .andExpect(jsonPath("$.sentimentValue").value(DEFAULT_SENTIMENT_VALUE.doubleValue()))
            .andExpect(jsonPath("$.reviewAnalysisDateTime").value(sameInstant(DEFAULT_REVIEW_ANALYSIS_DATE_TIME)))
            .andExpect(jsonPath("$.insight").value(DEFAULT_INSIGHT.toString()))
            .andExpect(jsonPath("$.reviewInsightData").value(DEFAULT_REVIEW_INSIGHT_DATA.toString()));
    }

    @Test
    @Transactional
    public void getAllReviewAnalysesByUidIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewAnalysisRepository.saveAndFlush(reviewAnalysis);

        // Get all the reviewAnalysisList where uid equals to DEFAULT_UID
        defaultReviewAnalysisShouldBeFound("uid.equals=" + DEFAULT_UID);

        // Get all the reviewAnalysisList where uid equals to UPDATED_UID
        defaultReviewAnalysisShouldNotBeFound("uid.equals=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllReviewAnalysesByUidIsInShouldWork() throws Exception {
        // Initialize the database
        reviewAnalysisRepository.saveAndFlush(reviewAnalysis);

        // Get all the reviewAnalysisList where uid in DEFAULT_UID or UPDATED_UID
        defaultReviewAnalysisShouldBeFound("uid.in=" + DEFAULT_UID + "," + UPDATED_UID);

        // Get all the reviewAnalysisList where uid equals to UPDATED_UID
        defaultReviewAnalysisShouldNotBeFound("uid.in=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllReviewAnalysesByUidIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewAnalysisRepository.saveAndFlush(reviewAnalysis);

        // Get all the reviewAnalysisList where uid is not null
        defaultReviewAnalysisShouldBeFound("uid.specified=true");

        // Get all the reviewAnalysisList where uid is null
        defaultReviewAnalysisShouldNotBeFound("uid.specified=false");
    }

    @Test
    @Transactional
    public void getAllReviewAnalysesBySentimentIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewAnalysisRepository.saveAndFlush(reviewAnalysis);

        // Get all the reviewAnalysisList where sentiment equals to DEFAULT_SENTIMENT
        defaultReviewAnalysisShouldBeFound("sentiment.equals=" + DEFAULT_SENTIMENT);

        // Get all the reviewAnalysisList where sentiment equals to UPDATED_SENTIMENT
        defaultReviewAnalysisShouldNotBeFound("sentiment.equals=" + UPDATED_SENTIMENT);
    }

    @Test
    @Transactional
    public void getAllReviewAnalysesBySentimentIsInShouldWork() throws Exception {
        // Initialize the database
        reviewAnalysisRepository.saveAndFlush(reviewAnalysis);

        // Get all the reviewAnalysisList where sentiment in DEFAULT_SENTIMENT or UPDATED_SENTIMENT
        defaultReviewAnalysisShouldBeFound("sentiment.in=" + DEFAULT_SENTIMENT + "," + UPDATED_SENTIMENT);

        // Get all the reviewAnalysisList where sentiment equals to UPDATED_SENTIMENT
        defaultReviewAnalysisShouldNotBeFound("sentiment.in=" + UPDATED_SENTIMENT);
    }

    @Test
    @Transactional
    public void getAllReviewAnalysesBySentimentIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewAnalysisRepository.saveAndFlush(reviewAnalysis);

        // Get all the reviewAnalysisList where sentiment is not null
        defaultReviewAnalysisShouldBeFound("sentiment.specified=true");

        // Get all the reviewAnalysisList where sentiment is null
        defaultReviewAnalysisShouldNotBeFound("sentiment.specified=false");
    }

    @Test
    @Transactional
    public void getAllReviewAnalysesBySentimentValueIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewAnalysisRepository.saveAndFlush(reviewAnalysis);

        // Get all the reviewAnalysisList where sentimentValue equals to DEFAULT_SENTIMENT_VALUE
        defaultReviewAnalysisShouldBeFound("sentimentValue.equals=" + DEFAULT_SENTIMENT_VALUE);

        // Get all the reviewAnalysisList where sentimentValue equals to UPDATED_SENTIMENT_VALUE
        defaultReviewAnalysisShouldNotBeFound("sentimentValue.equals=" + UPDATED_SENTIMENT_VALUE);
    }

    @Test
    @Transactional
    public void getAllReviewAnalysesBySentimentValueIsInShouldWork() throws Exception {
        // Initialize the database
        reviewAnalysisRepository.saveAndFlush(reviewAnalysis);

        // Get all the reviewAnalysisList where sentimentValue in DEFAULT_SENTIMENT_VALUE or UPDATED_SENTIMENT_VALUE
        defaultReviewAnalysisShouldBeFound("sentimentValue.in=" + DEFAULT_SENTIMENT_VALUE + "," + UPDATED_SENTIMENT_VALUE);

        // Get all the reviewAnalysisList where sentimentValue equals to UPDATED_SENTIMENT_VALUE
        defaultReviewAnalysisShouldNotBeFound("sentimentValue.in=" + UPDATED_SENTIMENT_VALUE);
    }

    @Test
    @Transactional
    public void getAllReviewAnalysesBySentimentValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewAnalysisRepository.saveAndFlush(reviewAnalysis);

        // Get all the reviewAnalysisList where sentimentValue is not null
        defaultReviewAnalysisShouldBeFound("sentimentValue.specified=true");

        // Get all the reviewAnalysisList where sentimentValue is null
        defaultReviewAnalysisShouldNotBeFound("sentimentValue.specified=false");
    }

    @Test
    @Transactional
    public void getAllReviewAnalysesByReviewAnalysisDateTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewAnalysisRepository.saveAndFlush(reviewAnalysis);

        // Get all the reviewAnalysisList where reviewAnalysisDateTime equals to DEFAULT_REVIEW_ANALYSIS_DATE_TIME
        defaultReviewAnalysisShouldBeFound("reviewAnalysisDateTime.equals=" + DEFAULT_REVIEW_ANALYSIS_DATE_TIME);

        // Get all the reviewAnalysisList where reviewAnalysisDateTime equals to UPDATED_REVIEW_ANALYSIS_DATE_TIME
        defaultReviewAnalysisShouldNotBeFound("reviewAnalysisDateTime.equals=" + UPDATED_REVIEW_ANALYSIS_DATE_TIME);
    }

    @Test
    @Transactional
    public void getAllReviewAnalysesByReviewAnalysisDateTimeIsInShouldWork() throws Exception {
        // Initialize the database
        reviewAnalysisRepository.saveAndFlush(reviewAnalysis);

        // Get all the reviewAnalysisList where reviewAnalysisDateTime in DEFAULT_REVIEW_ANALYSIS_DATE_TIME or UPDATED_REVIEW_ANALYSIS_DATE_TIME
        defaultReviewAnalysisShouldBeFound("reviewAnalysisDateTime.in=" + DEFAULT_REVIEW_ANALYSIS_DATE_TIME + "," + UPDATED_REVIEW_ANALYSIS_DATE_TIME);

        // Get all the reviewAnalysisList where reviewAnalysisDateTime equals to UPDATED_REVIEW_ANALYSIS_DATE_TIME
        defaultReviewAnalysisShouldNotBeFound("reviewAnalysisDateTime.in=" + UPDATED_REVIEW_ANALYSIS_DATE_TIME);
    }

    @Test
    @Transactional
    public void getAllReviewAnalysesByReviewAnalysisDateTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewAnalysisRepository.saveAndFlush(reviewAnalysis);

        // Get all the reviewAnalysisList where reviewAnalysisDateTime is not null
        defaultReviewAnalysisShouldBeFound("reviewAnalysisDateTime.specified=true");

        // Get all the reviewAnalysisList where reviewAnalysisDateTime is null
        defaultReviewAnalysisShouldNotBeFound("reviewAnalysisDateTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllReviewAnalysesByReviewAnalysisDateTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reviewAnalysisRepository.saveAndFlush(reviewAnalysis);

        // Get all the reviewAnalysisList where reviewAnalysisDateTime greater than or equals to DEFAULT_REVIEW_ANALYSIS_DATE_TIME
        defaultReviewAnalysisShouldBeFound("reviewAnalysisDateTime.greaterOrEqualThan=" + DEFAULT_REVIEW_ANALYSIS_DATE_TIME);

        // Get all the reviewAnalysisList where reviewAnalysisDateTime greater than or equals to UPDATED_REVIEW_ANALYSIS_DATE_TIME
        defaultReviewAnalysisShouldNotBeFound("reviewAnalysisDateTime.greaterOrEqualThan=" + UPDATED_REVIEW_ANALYSIS_DATE_TIME);
    }

    @Test
    @Transactional
    public void getAllReviewAnalysesByReviewAnalysisDateTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        reviewAnalysisRepository.saveAndFlush(reviewAnalysis);

        // Get all the reviewAnalysisList where reviewAnalysisDateTime less than or equals to DEFAULT_REVIEW_ANALYSIS_DATE_TIME
        defaultReviewAnalysisShouldNotBeFound("reviewAnalysisDateTime.lessThan=" + DEFAULT_REVIEW_ANALYSIS_DATE_TIME);

        // Get all the reviewAnalysisList where reviewAnalysisDateTime less than or equals to UPDATED_REVIEW_ANALYSIS_DATE_TIME
        defaultReviewAnalysisShouldBeFound("reviewAnalysisDateTime.lessThan=" + UPDATED_REVIEW_ANALYSIS_DATE_TIME);
    }


    @Test
    @Transactional
    public void getAllReviewAnalysesByInsightIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewAnalysisRepository.saveAndFlush(reviewAnalysis);

        // Get all the reviewAnalysisList where insight equals to DEFAULT_INSIGHT
        defaultReviewAnalysisShouldBeFound("insight.equals=" + DEFAULT_INSIGHT);

        // Get all the reviewAnalysisList where insight equals to UPDATED_INSIGHT
        defaultReviewAnalysisShouldNotBeFound("insight.equals=" + UPDATED_INSIGHT);
    }

    @Test
    @Transactional
    public void getAllReviewAnalysesByInsightIsInShouldWork() throws Exception {
        // Initialize the database
        reviewAnalysisRepository.saveAndFlush(reviewAnalysis);

        // Get all the reviewAnalysisList where insight in DEFAULT_INSIGHT or UPDATED_INSIGHT
        defaultReviewAnalysisShouldBeFound("insight.in=" + DEFAULT_INSIGHT + "," + UPDATED_INSIGHT);

        // Get all the reviewAnalysisList where insight equals to UPDATED_INSIGHT
        defaultReviewAnalysisShouldNotBeFound("insight.in=" + UPDATED_INSIGHT);
    }

    @Test
    @Transactional
    public void getAllReviewAnalysesByInsightIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewAnalysisRepository.saveAndFlush(reviewAnalysis);

        // Get all the reviewAnalysisList where insight is not null
        defaultReviewAnalysisShouldBeFound("insight.specified=true");

        // Get all the reviewAnalysisList where insight is null
        defaultReviewAnalysisShouldNotBeFound("insight.specified=false");
    }

    @Test
    @Transactional
    public void getAllReviewAnalysesByReviewInsightDataIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewAnalysisRepository.saveAndFlush(reviewAnalysis);

        // Get all the reviewAnalysisList where reviewInsightData equals to DEFAULT_REVIEW_INSIGHT_DATA
        defaultReviewAnalysisShouldBeFound("reviewInsightData.equals=" + DEFAULT_REVIEW_INSIGHT_DATA);

        // Get all the reviewAnalysisList where reviewInsightData equals to UPDATED_REVIEW_INSIGHT_DATA
        defaultReviewAnalysisShouldNotBeFound("reviewInsightData.equals=" + UPDATED_REVIEW_INSIGHT_DATA);
    }

    @Test
    @Transactional
    public void getAllReviewAnalysesByReviewInsightDataIsInShouldWork() throws Exception {
        // Initialize the database
        reviewAnalysisRepository.saveAndFlush(reviewAnalysis);

        // Get all the reviewAnalysisList where reviewInsightData in DEFAULT_REVIEW_INSIGHT_DATA or UPDATED_REVIEW_INSIGHT_DATA
        defaultReviewAnalysisShouldBeFound("reviewInsightData.in=" + DEFAULT_REVIEW_INSIGHT_DATA + "," + UPDATED_REVIEW_INSIGHT_DATA);

        // Get all the reviewAnalysisList where reviewInsightData equals to UPDATED_REVIEW_INSIGHT_DATA
        defaultReviewAnalysisShouldNotBeFound("reviewInsightData.in=" + UPDATED_REVIEW_INSIGHT_DATA);
    }

    @Test
    @Transactional
    public void getAllReviewAnalysesByReviewInsightDataIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewAnalysisRepository.saveAndFlush(reviewAnalysis);

        // Get all the reviewAnalysisList where reviewInsightData is not null
        defaultReviewAnalysisShouldBeFound("reviewInsightData.specified=true");

        // Get all the reviewAnalysisList where reviewInsightData is null
        defaultReviewAnalysisShouldNotBeFound("reviewInsightData.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultReviewAnalysisShouldBeFound(String filter) throws Exception {
        restReviewAnalysisMockMvc.perform(get("/api/review-analyses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reviewAnalysis.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
            .andExpect(jsonPath("$.[*].sentiment").value(hasItem(DEFAULT_SENTIMENT.toString())))
            .andExpect(jsonPath("$.[*].sentimentValue").value(hasItem(DEFAULT_SENTIMENT_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].reviewAnalysisDateTime").value(hasItem(sameInstant(DEFAULT_REVIEW_ANALYSIS_DATE_TIME))))
            .andExpect(jsonPath("$.[*].insight").value(hasItem(DEFAULT_INSIGHT.toString())))
            .andExpect(jsonPath("$.[*].reviewInsightData").value(hasItem(DEFAULT_REVIEW_INSIGHT_DATA.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultReviewAnalysisShouldNotBeFound(String filter) throws Exception {
        restReviewAnalysisMockMvc.perform(get("/api/review-analyses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingReviewAnalysis() throws Exception {
        // Get the reviewAnalysis
        restReviewAnalysisMockMvc.perform(get("/api/review-analyses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReviewAnalysis() throws Exception {
        // Initialize the database
        reviewAnalysisRepository.saveAndFlush(reviewAnalysis);

        int databaseSizeBeforeUpdate = reviewAnalysisRepository.findAll().size();

        // Update the reviewAnalysis
        ReviewAnalysis updatedReviewAnalysis = reviewAnalysisRepository.findById(reviewAnalysis.getId()).get();
        // Disconnect from session so that the updates on updatedReviewAnalysis are not directly saved in db
        em.detach(updatedReviewAnalysis);
        updatedReviewAnalysis
            .uid(UPDATED_UID)
            .sentiment(UPDATED_SENTIMENT)
            .sentimentValue(UPDATED_SENTIMENT_VALUE)
            .reviewAnalysisDateTime(UPDATED_REVIEW_ANALYSIS_DATE_TIME)
            .insight(UPDATED_INSIGHT)
            .reviewInsightData(UPDATED_REVIEW_INSIGHT_DATA);
        ReviewAnalysisDTO reviewAnalysisDTO = reviewAnalysisMapper.toDto(updatedReviewAnalysis);

        restReviewAnalysisMockMvc.perform(put("/api/review-analyses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reviewAnalysisDTO)))
            .andExpect(status().isOk());

        // Validate the ReviewAnalysis in the database
        List<ReviewAnalysis> reviewAnalysisList = reviewAnalysisRepository.findAll();
        assertThat(reviewAnalysisList).hasSize(databaseSizeBeforeUpdate);
        ReviewAnalysis testReviewAnalysis = reviewAnalysisList.get(reviewAnalysisList.size() - 1);
        assertThat(testReviewAnalysis.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testReviewAnalysis.getSentiment()).isEqualTo(UPDATED_SENTIMENT);
        assertThat(testReviewAnalysis.getSentimentValue()).isEqualTo(UPDATED_SENTIMENT_VALUE);
        assertThat(testReviewAnalysis.getReviewAnalysisDateTime()).isEqualTo(UPDATED_REVIEW_ANALYSIS_DATE_TIME);
        assertThat(testReviewAnalysis.getInsight()).isEqualTo(UPDATED_INSIGHT);
        assertThat(testReviewAnalysis.getReviewInsightData()).isEqualTo(UPDATED_REVIEW_INSIGHT_DATA);

        // Validate the ReviewAnalysis in Elasticsearch
        verify(mockReviewAnalysisSearchRepository, times(1)).save(testReviewAnalysis);
    }

    @Test
    @Transactional
    public void updateNonExistingReviewAnalysis() throws Exception {
        int databaseSizeBeforeUpdate = reviewAnalysisRepository.findAll().size();

        // Create the ReviewAnalysis
        ReviewAnalysisDTO reviewAnalysisDTO = reviewAnalysisMapper.toDto(reviewAnalysis);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restReviewAnalysisMockMvc.perform(put("/api/review-analyses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reviewAnalysisDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReviewAnalysis in the database
        List<ReviewAnalysis> reviewAnalysisList = reviewAnalysisRepository.findAll();
        assertThat(reviewAnalysisList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReviewAnalysis in Elasticsearch
        verify(mockReviewAnalysisSearchRepository, times(0)).save(reviewAnalysis);
    }

    @Test
    @Transactional
    public void deleteReviewAnalysis() throws Exception {
        // Initialize the database
        reviewAnalysisRepository.saveAndFlush(reviewAnalysis);

        int databaseSizeBeforeDelete = reviewAnalysisRepository.findAll().size();

        // Get the reviewAnalysis
        restReviewAnalysisMockMvc.perform(delete("/api/review-analyses/{id}", reviewAnalysis.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ReviewAnalysis> reviewAnalysisList = reviewAnalysisRepository.findAll();
        assertThat(reviewAnalysisList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ReviewAnalysis in Elasticsearch
        verify(mockReviewAnalysisSearchRepository, times(1)).deleteById(reviewAnalysis.getId());
    }

    @Test
    @Transactional
    public void searchReviewAnalysis() throws Exception {
        // Initialize the database
        reviewAnalysisRepository.saveAndFlush(reviewAnalysis);
        when(mockReviewAnalysisSearchRepository.search(queryStringQuery("id:" + reviewAnalysis.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(reviewAnalysis), PageRequest.of(0, 1), 1));
        // Search the reviewAnalysis
        restReviewAnalysisMockMvc.perform(get("/api/_search/review-analyses?query=id:" + reviewAnalysis.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reviewAnalysis.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
            .andExpect(jsonPath("$.[*].sentiment").value(hasItem(DEFAULT_SENTIMENT.toString())))
            .andExpect(jsonPath("$.[*].sentimentValue").value(hasItem(DEFAULT_SENTIMENT_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].reviewAnalysisDateTime").value(hasItem(sameInstant(DEFAULT_REVIEW_ANALYSIS_DATE_TIME))))
            .andExpect(jsonPath("$.[*].insight").value(hasItem(DEFAULT_INSIGHT.toString())))
            .andExpect(jsonPath("$.[*].reviewInsightData").value(hasItem(DEFAULT_REVIEW_INSIGHT_DATA.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReviewAnalysis.class);
        ReviewAnalysis reviewAnalysis1 = new ReviewAnalysis();
        reviewAnalysis1.setId(1L);
        ReviewAnalysis reviewAnalysis2 = new ReviewAnalysis();
        reviewAnalysis2.setId(reviewAnalysis1.getId());
        assertThat(reviewAnalysis1).isEqualTo(reviewAnalysis2);
        reviewAnalysis2.setId(2L);
        assertThat(reviewAnalysis1).isNotEqualTo(reviewAnalysis2);
        reviewAnalysis1.setId(null);
        assertThat(reviewAnalysis1).isNotEqualTo(reviewAnalysis2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReviewAnalysisDTO.class);
        ReviewAnalysisDTO reviewAnalysisDTO1 = new ReviewAnalysisDTO();
        reviewAnalysisDTO1.setId(1L);
        ReviewAnalysisDTO reviewAnalysisDTO2 = new ReviewAnalysisDTO();
        assertThat(reviewAnalysisDTO1).isNotEqualTo(reviewAnalysisDTO2);
        reviewAnalysisDTO2.setId(reviewAnalysisDTO1.getId());
        assertThat(reviewAnalysisDTO1).isEqualTo(reviewAnalysisDTO2);
        reviewAnalysisDTO2.setId(2L);
        assertThat(reviewAnalysisDTO1).isNotEqualTo(reviewAnalysisDTO2);
        reviewAnalysisDTO1.setId(null);
        assertThat(reviewAnalysisDTO1).isNotEqualTo(reviewAnalysisDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(reviewAnalysisMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(reviewAnalysisMapper.fromId(null)).isNull();
    }
}
