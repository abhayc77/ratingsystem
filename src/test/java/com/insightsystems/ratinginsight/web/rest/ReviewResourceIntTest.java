package com.insightsystems.ratinginsight.web.rest;

import com.insightsystems.ratinginsight.RatingSystemApp;

import com.insightsystems.ratinginsight.domain.Review;
import com.insightsystems.ratinginsight.domain.ReviewAnalysis;
import com.insightsystems.ratinginsight.domain.Reviewer;
import com.insightsystems.ratinginsight.domain.Product;
import com.insightsystems.ratinginsight.repository.ReviewRepository;
import com.insightsystems.ratinginsight.repository.search.ReviewSearchRepository;
import com.insightsystems.ratinginsight.service.ReviewService;
import com.insightsystems.ratinginsight.service.dto.ReviewDTO;
import com.insightsystems.ratinginsight.service.mapper.ReviewMapper;
import com.insightsystems.ratinginsight.web.rest.errors.ExceptionTranslator;
import com.insightsystems.ratinginsight.service.dto.ReviewCriteria;
import com.insightsystems.ratinginsight.service.ReviewQueryService;

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

import com.insightsystems.ratinginsight.domain.enumeration.Language;
import com.insightsystems.ratinginsight.domain.enumeration.ReviewStatus;
/**
 * Test class for the ReviewResource REST controller.
 *
 * @see ReviewResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RatingSystemApp.class)
public class ReviewResourceIntTest {

    private static final String DEFAULT_PRODUCT_ID = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_UID = "AAAAAAAAAA";
    private static final String UPDATED_UID = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_URL = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_URL = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_REVIEW_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_REVIEW_CONTENT = "BBBBBBBBBB";

    private static final Language DEFAULT_LANGUAGE = Language.FRENCH;
    private static final Language UPDATED_LANGUAGE = Language.ENGLISH;

    private static final ZonedDateTime DEFAULT_REVIEW_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_REVIEW_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;

    private static final Integer DEFAULT_FULL_RATING = 1;
    private static final Integer UPDATED_FULL_RATING = 2;

    private static final ReviewStatus DEFAULT_REVIEW_STATUS = ReviewStatus.UNKNOWN;
    private static final ReviewStatus UPDATED_REVIEW_STATUS = ReviewStatus.NEW_REVIEW;

    private static final Integer DEFAULT_HELPFUL_VOTES = 1;
    private static final Integer UPDATED_HELPFUL_VOTES = 2;

    private static final Integer DEFAULT_TOTAL_VOTES = 1;
    private static final Integer UPDATED_TOTAL_VOTES = 2;

    private static final Boolean DEFAULT_VERIFIED_PURCHASE = false;
    private static final Boolean UPDATED_VERIFIED_PURCHASE = true;

    private static final String DEFAULT_REAL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_REAL_NAME = "BBBBBBBBBB";

    @Autowired
    private ReviewRepository reviewRepository;


    @Autowired
    private ReviewMapper reviewMapper;
    

    @Autowired
    private ReviewService reviewService;

    /**
     * This repository is mocked in the com.insightsystems.ratinginsight.repository.search test package.
     *
     * @see com.insightsystems.ratinginsight.repository.search.ReviewSearchRepositoryMockConfiguration
     */
    @Autowired
    private ReviewSearchRepository mockReviewSearchRepository;

    @Autowired
    private ReviewQueryService reviewQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restReviewMockMvc;

    private Review review;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ReviewResource reviewResource = new ReviewResource(reviewService, reviewQueryService);
        this.restReviewMockMvc = MockMvcBuilders.standaloneSetup(reviewResource)
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
    public static Review createEntity(EntityManager em) {
        Review review = new Review()
            .productID(DEFAULT_PRODUCT_ID)
            .uid(DEFAULT_UID)
            .productURL(DEFAULT_PRODUCT_URL)
            .title(DEFAULT_TITLE)
            .reviewContent(DEFAULT_REVIEW_CONTENT)
            .language(DEFAULT_LANGUAGE)
            .reviewDateTime(DEFAULT_REVIEW_DATE_TIME)
            .rating(DEFAULT_RATING)
            .fullRating(DEFAULT_FULL_RATING)
            .reviewStatus(DEFAULT_REVIEW_STATUS)
            .helpfulVotes(DEFAULT_HELPFUL_VOTES)
            .totalVotes(DEFAULT_TOTAL_VOTES)
            .verifiedPurchase(DEFAULT_VERIFIED_PURCHASE)
            .realName(DEFAULT_REAL_NAME);
        return review;
    }

    @Before
    public void initTest() {
        review = createEntity(em);
    }

    @Test
    @Transactional
    public void createReview() throws Exception {
        int databaseSizeBeforeCreate = reviewRepository.findAll().size();

        // Create the Review
        ReviewDTO reviewDTO = reviewMapper.toDto(review);
        restReviewMockMvc.perform(post("/api/reviews")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reviewDTO)))
            .andExpect(status().isCreated());

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeCreate + 1);
        Review testReview = reviewList.get(reviewList.size() - 1);
        assertThat(testReview.getProductID()).isEqualTo(DEFAULT_PRODUCT_ID);
        assertThat(testReview.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testReview.getProductURL()).isEqualTo(DEFAULT_PRODUCT_URL);
        assertThat(testReview.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testReview.getReviewContent()).isEqualTo(DEFAULT_REVIEW_CONTENT);
        assertThat(testReview.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testReview.getReviewDateTime()).isEqualTo(DEFAULT_REVIEW_DATE_TIME);
        assertThat(testReview.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testReview.getFullRating()).isEqualTo(DEFAULT_FULL_RATING);
        assertThat(testReview.getReviewStatus()).isEqualTo(DEFAULT_REVIEW_STATUS);
        assertThat(testReview.getHelpfulVotes()).isEqualTo(DEFAULT_HELPFUL_VOTES);
        assertThat(testReview.getTotalVotes()).isEqualTo(DEFAULT_TOTAL_VOTES);
        assertThat(testReview.isVerifiedPurchase()).isEqualTo(DEFAULT_VERIFIED_PURCHASE);
        assertThat(testReview.getRealName()).isEqualTo(DEFAULT_REAL_NAME);

        // Validate the Review in Elasticsearch
        verify(mockReviewSearchRepository, times(1)).save(testReview);
    }

    @Test
    @Transactional
    public void createReviewWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reviewRepository.findAll().size();

        // Create the Review with an existing ID
        review.setId(1L);
        ReviewDTO reviewDTO = reviewMapper.toDto(review);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReviewMockMvc.perform(post("/api/reviews")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reviewDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeCreate);

        // Validate the Review in Elasticsearch
        verify(mockReviewSearchRepository, times(0)).save(review);
    }

    @Test
    @Transactional
    public void getAllReviews() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList
        restReviewMockMvc.perform(get("/api/reviews?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(review.getId().intValue())))
            .andExpect(jsonPath("$.[*].productID").value(hasItem(DEFAULT_PRODUCT_ID.toString())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
            .andExpect(jsonPath("$.[*].productURL").value(hasItem(DEFAULT_PRODUCT_URL.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].reviewContent").value(hasItem(DEFAULT_REVIEW_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].reviewDateTime").value(hasItem(sameInstant(DEFAULT_REVIEW_DATE_TIME))))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].fullRating").value(hasItem(DEFAULT_FULL_RATING)))
            .andExpect(jsonPath("$.[*].reviewStatus").value(hasItem(DEFAULT_REVIEW_STATUS.toString())))
            .andExpect(jsonPath("$.[*].helpfulVotes").value(hasItem(DEFAULT_HELPFUL_VOTES)))
            .andExpect(jsonPath("$.[*].totalVotes").value(hasItem(DEFAULT_TOTAL_VOTES)))
            .andExpect(jsonPath("$.[*].verifiedPurchase").value(hasItem(DEFAULT_VERIFIED_PURCHASE.booleanValue())))
            .andExpect(jsonPath("$.[*].realName").value(hasItem(DEFAULT_REAL_NAME.toString())));
    }
    

    @Test
    @Transactional
    public void getReview() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get the review
        restReviewMockMvc.perform(get("/api/reviews/{id}", review.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(review.getId().intValue()))
            .andExpect(jsonPath("$.productID").value(DEFAULT_PRODUCT_ID.toString()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID.toString()))
            .andExpect(jsonPath("$.productURL").value(DEFAULT_PRODUCT_URL.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.reviewContent").value(DEFAULT_REVIEW_CONTENT.toString()))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()))
            .andExpect(jsonPath("$.reviewDateTime").value(sameInstant(DEFAULT_REVIEW_DATE_TIME)))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.fullRating").value(DEFAULT_FULL_RATING))
            .andExpect(jsonPath("$.reviewStatus").value(DEFAULT_REVIEW_STATUS.toString()))
            .andExpect(jsonPath("$.helpfulVotes").value(DEFAULT_HELPFUL_VOTES))
            .andExpect(jsonPath("$.totalVotes").value(DEFAULT_TOTAL_VOTES))
            .andExpect(jsonPath("$.verifiedPurchase").value(DEFAULT_VERIFIED_PURCHASE.booleanValue()))
            .andExpect(jsonPath("$.realName").value(DEFAULT_REAL_NAME.toString()));
    }

    @Test
    @Transactional
    public void getAllReviewsByProductIDIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where productID equals to DEFAULT_PRODUCT_ID
        defaultReviewShouldBeFound("productID.equals=" + DEFAULT_PRODUCT_ID);

        // Get all the reviewList where productID equals to UPDATED_PRODUCT_ID
        defaultReviewShouldNotBeFound("productID.equals=" + UPDATED_PRODUCT_ID);
    }

    @Test
    @Transactional
    public void getAllReviewsByProductIDIsInShouldWork() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where productID in DEFAULT_PRODUCT_ID or UPDATED_PRODUCT_ID
        defaultReviewShouldBeFound("productID.in=" + DEFAULT_PRODUCT_ID + "," + UPDATED_PRODUCT_ID);

        // Get all the reviewList where productID equals to UPDATED_PRODUCT_ID
        defaultReviewShouldNotBeFound("productID.in=" + UPDATED_PRODUCT_ID);
    }

    @Test
    @Transactional
    public void getAllReviewsByProductIDIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where productID is not null
        defaultReviewShouldBeFound("productID.specified=true");

        // Get all the reviewList where productID is null
        defaultReviewShouldNotBeFound("productID.specified=false");
    }

    @Test
    @Transactional
    public void getAllReviewsByUidIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where uid equals to DEFAULT_UID
        defaultReviewShouldBeFound("uid.equals=" + DEFAULT_UID);

        // Get all the reviewList where uid equals to UPDATED_UID
        defaultReviewShouldNotBeFound("uid.equals=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllReviewsByUidIsInShouldWork() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where uid in DEFAULT_UID or UPDATED_UID
        defaultReviewShouldBeFound("uid.in=" + DEFAULT_UID + "," + UPDATED_UID);

        // Get all the reviewList where uid equals to UPDATED_UID
        defaultReviewShouldNotBeFound("uid.in=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllReviewsByUidIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where uid is not null
        defaultReviewShouldBeFound("uid.specified=true");

        // Get all the reviewList where uid is null
        defaultReviewShouldNotBeFound("uid.specified=false");
    }

    @Test
    @Transactional
    public void getAllReviewsByProductURLIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where productURL equals to DEFAULT_PRODUCT_URL
        defaultReviewShouldBeFound("productURL.equals=" + DEFAULT_PRODUCT_URL);

        // Get all the reviewList where productURL equals to UPDATED_PRODUCT_URL
        defaultReviewShouldNotBeFound("productURL.equals=" + UPDATED_PRODUCT_URL);
    }

    @Test
    @Transactional
    public void getAllReviewsByProductURLIsInShouldWork() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where productURL in DEFAULT_PRODUCT_URL or UPDATED_PRODUCT_URL
        defaultReviewShouldBeFound("productURL.in=" + DEFAULT_PRODUCT_URL + "," + UPDATED_PRODUCT_URL);

        // Get all the reviewList where productURL equals to UPDATED_PRODUCT_URL
        defaultReviewShouldNotBeFound("productURL.in=" + UPDATED_PRODUCT_URL);
    }

    @Test
    @Transactional
    public void getAllReviewsByProductURLIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where productURL is not null
        defaultReviewShouldBeFound("productURL.specified=true");

        // Get all the reviewList where productURL is null
        defaultReviewShouldNotBeFound("productURL.specified=false");
    }

    @Test
    @Transactional
    public void getAllReviewsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where title equals to DEFAULT_TITLE
        defaultReviewShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the reviewList where title equals to UPDATED_TITLE
        defaultReviewShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllReviewsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultReviewShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the reviewList where title equals to UPDATED_TITLE
        defaultReviewShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllReviewsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where title is not null
        defaultReviewShouldBeFound("title.specified=true");

        // Get all the reviewList where title is null
        defaultReviewShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllReviewsByReviewContentIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where reviewContent equals to DEFAULT_REVIEW_CONTENT
        defaultReviewShouldBeFound("reviewContent.equals=" + DEFAULT_REVIEW_CONTENT);

        // Get all the reviewList where reviewContent equals to UPDATED_REVIEW_CONTENT
        defaultReviewShouldNotBeFound("reviewContent.equals=" + UPDATED_REVIEW_CONTENT);
    }

    @Test
    @Transactional
    public void getAllReviewsByReviewContentIsInShouldWork() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where reviewContent in DEFAULT_REVIEW_CONTENT or UPDATED_REVIEW_CONTENT
        defaultReviewShouldBeFound("reviewContent.in=" + DEFAULT_REVIEW_CONTENT + "," + UPDATED_REVIEW_CONTENT);

        // Get all the reviewList where reviewContent equals to UPDATED_REVIEW_CONTENT
        defaultReviewShouldNotBeFound("reviewContent.in=" + UPDATED_REVIEW_CONTENT);
    }

    @Test
    @Transactional
    public void getAllReviewsByReviewContentIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where reviewContent is not null
        defaultReviewShouldBeFound("reviewContent.specified=true");

        // Get all the reviewList where reviewContent is null
        defaultReviewShouldNotBeFound("reviewContent.specified=false");
    }

    @Test
    @Transactional
    public void getAllReviewsByLanguageIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where language equals to DEFAULT_LANGUAGE
        defaultReviewShouldBeFound("language.equals=" + DEFAULT_LANGUAGE);

        // Get all the reviewList where language equals to UPDATED_LANGUAGE
        defaultReviewShouldNotBeFound("language.equals=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    public void getAllReviewsByLanguageIsInShouldWork() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where language in DEFAULT_LANGUAGE or UPDATED_LANGUAGE
        defaultReviewShouldBeFound("language.in=" + DEFAULT_LANGUAGE + "," + UPDATED_LANGUAGE);

        // Get all the reviewList where language equals to UPDATED_LANGUAGE
        defaultReviewShouldNotBeFound("language.in=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    public void getAllReviewsByLanguageIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where language is not null
        defaultReviewShouldBeFound("language.specified=true");

        // Get all the reviewList where language is null
        defaultReviewShouldNotBeFound("language.specified=false");
    }

    @Test
    @Transactional
    public void getAllReviewsByReviewDateTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where reviewDateTime equals to DEFAULT_REVIEW_DATE_TIME
        defaultReviewShouldBeFound("reviewDateTime.equals=" + DEFAULT_REVIEW_DATE_TIME);

        // Get all the reviewList where reviewDateTime equals to UPDATED_REVIEW_DATE_TIME
        defaultReviewShouldNotBeFound("reviewDateTime.equals=" + UPDATED_REVIEW_DATE_TIME);
    }

    @Test
    @Transactional
    public void getAllReviewsByReviewDateTimeIsInShouldWork() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where reviewDateTime in DEFAULT_REVIEW_DATE_TIME or UPDATED_REVIEW_DATE_TIME
        defaultReviewShouldBeFound("reviewDateTime.in=" + DEFAULT_REVIEW_DATE_TIME + "," + UPDATED_REVIEW_DATE_TIME);

        // Get all the reviewList where reviewDateTime equals to UPDATED_REVIEW_DATE_TIME
        defaultReviewShouldNotBeFound("reviewDateTime.in=" + UPDATED_REVIEW_DATE_TIME);
    }

    @Test
    @Transactional
    public void getAllReviewsByReviewDateTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where reviewDateTime is not null
        defaultReviewShouldBeFound("reviewDateTime.specified=true");

        // Get all the reviewList where reviewDateTime is null
        defaultReviewShouldNotBeFound("reviewDateTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllReviewsByReviewDateTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where reviewDateTime greater than or equals to DEFAULT_REVIEW_DATE_TIME
        defaultReviewShouldBeFound("reviewDateTime.greaterOrEqualThan=" + DEFAULT_REVIEW_DATE_TIME);

        // Get all the reviewList where reviewDateTime greater than or equals to UPDATED_REVIEW_DATE_TIME
        defaultReviewShouldNotBeFound("reviewDateTime.greaterOrEqualThan=" + UPDATED_REVIEW_DATE_TIME);
    }

    @Test
    @Transactional
    public void getAllReviewsByReviewDateTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where reviewDateTime less than or equals to DEFAULT_REVIEW_DATE_TIME
        defaultReviewShouldNotBeFound("reviewDateTime.lessThan=" + DEFAULT_REVIEW_DATE_TIME);

        // Get all the reviewList where reviewDateTime less than or equals to UPDATED_REVIEW_DATE_TIME
        defaultReviewShouldBeFound("reviewDateTime.lessThan=" + UPDATED_REVIEW_DATE_TIME);
    }


    @Test
    @Transactional
    public void getAllReviewsByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where rating equals to DEFAULT_RATING
        defaultReviewShouldBeFound("rating.equals=" + DEFAULT_RATING);

        // Get all the reviewList where rating equals to UPDATED_RATING
        defaultReviewShouldNotBeFound("rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllReviewsByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where rating in DEFAULT_RATING or UPDATED_RATING
        defaultReviewShouldBeFound("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING);

        // Get all the reviewList where rating equals to UPDATED_RATING
        defaultReviewShouldNotBeFound("rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllReviewsByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where rating is not null
        defaultReviewShouldBeFound("rating.specified=true");

        // Get all the reviewList where rating is null
        defaultReviewShouldNotBeFound("rating.specified=false");
    }

    @Test
    @Transactional
    public void getAllReviewsByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where rating greater than or equals to DEFAULT_RATING
        defaultReviewShouldBeFound("rating.greaterOrEqualThan=" + DEFAULT_RATING);

        // Get all the reviewList where rating greater than or equals to UPDATED_RATING
        defaultReviewShouldNotBeFound("rating.greaterOrEqualThan=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllReviewsByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where rating less than or equals to DEFAULT_RATING
        defaultReviewShouldNotBeFound("rating.lessThan=" + DEFAULT_RATING);

        // Get all the reviewList where rating less than or equals to UPDATED_RATING
        defaultReviewShouldBeFound("rating.lessThan=" + UPDATED_RATING);
    }


    @Test
    @Transactional
    public void getAllReviewsByFullRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where fullRating equals to DEFAULT_FULL_RATING
        defaultReviewShouldBeFound("fullRating.equals=" + DEFAULT_FULL_RATING);

        // Get all the reviewList where fullRating equals to UPDATED_FULL_RATING
        defaultReviewShouldNotBeFound("fullRating.equals=" + UPDATED_FULL_RATING);
    }

    @Test
    @Transactional
    public void getAllReviewsByFullRatingIsInShouldWork() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where fullRating in DEFAULT_FULL_RATING or UPDATED_FULL_RATING
        defaultReviewShouldBeFound("fullRating.in=" + DEFAULT_FULL_RATING + "," + UPDATED_FULL_RATING);

        // Get all the reviewList where fullRating equals to UPDATED_FULL_RATING
        defaultReviewShouldNotBeFound("fullRating.in=" + UPDATED_FULL_RATING);
    }

    @Test
    @Transactional
    public void getAllReviewsByFullRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where fullRating is not null
        defaultReviewShouldBeFound("fullRating.specified=true");

        // Get all the reviewList where fullRating is null
        defaultReviewShouldNotBeFound("fullRating.specified=false");
    }

    @Test
    @Transactional
    public void getAllReviewsByFullRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where fullRating greater than or equals to DEFAULT_FULL_RATING
        defaultReviewShouldBeFound("fullRating.greaterOrEqualThan=" + DEFAULT_FULL_RATING);

        // Get all the reviewList where fullRating greater than or equals to UPDATED_FULL_RATING
        defaultReviewShouldNotBeFound("fullRating.greaterOrEqualThan=" + UPDATED_FULL_RATING);
    }

    @Test
    @Transactional
    public void getAllReviewsByFullRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where fullRating less than or equals to DEFAULT_FULL_RATING
        defaultReviewShouldNotBeFound("fullRating.lessThan=" + DEFAULT_FULL_RATING);

        // Get all the reviewList where fullRating less than or equals to UPDATED_FULL_RATING
        defaultReviewShouldBeFound("fullRating.lessThan=" + UPDATED_FULL_RATING);
    }


    @Test
    @Transactional
    public void getAllReviewsByReviewStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where reviewStatus equals to DEFAULT_REVIEW_STATUS
        defaultReviewShouldBeFound("reviewStatus.equals=" + DEFAULT_REVIEW_STATUS);

        // Get all the reviewList where reviewStatus equals to UPDATED_REVIEW_STATUS
        defaultReviewShouldNotBeFound("reviewStatus.equals=" + UPDATED_REVIEW_STATUS);
    }

    @Test
    @Transactional
    public void getAllReviewsByReviewStatusIsInShouldWork() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where reviewStatus in DEFAULT_REVIEW_STATUS or UPDATED_REVIEW_STATUS
        defaultReviewShouldBeFound("reviewStatus.in=" + DEFAULT_REVIEW_STATUS + "," + UPDATED_REVIEW_STATUS);

        // Get all the reviewList where reviewStatus equals to UPDATED_REVIEW_STATUS
        defaultReviewShouldNotBeFound("reviewStatus.in=" + UPDATED_REVIEW_STATUS);
    }

    @Test
    @Transactional
    public void getAllReviewsByReviewStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where reviewStatus is not null
        defaultReviewShouldBeFound("reviewStatus.specified=true");

        // Get all the reviewList where reviewStatus is null
        defaultReviewShouldNotBeFound("reviewStatus.specified=false");
    }

    @Test
    @Transactional
    public void getAllReviewsByHelpfulVotesIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where helpfulVotes equals to DEFAULT_HELPFUL_VOTES
        defaultReviewShouldBeFound("helpfulVotes.equals=" + DEFAULT_HELPFUL_VOTES);

        // Get all the reviewList where helpfulVotes equals to UPDATED_HELPFUL_VOTES
        defaultReviewShouldNotBeFound("helpfulVotes.equals=" + UPDATED_HELPFUL_VOTES);
    }

    @Test
    @Transactional
    public void getAllReviewsByHelpfulVotesIsInShouldWork() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where helpfulVotes in DEFAULT_HELPFUL_VOTES or UPDATED_HELPFUL_VOTES
        defaultReviewShouldBeFound("helpfulVotes.in=" + DEFAULT_HELPFUL_VOTES + "," + UPDATED_HELPFUL_VOTES);

        // Get all the reviewList where helpfulVotes equals to UPDATED_HELPFUL_VOTES
        defaultReviewShouldNotBeFound("helpfulVotes.in=" + UPDATED_HELPFUL_VOTES);
    }

    @Test
    @Transactional
    public void getAllReviewsByHelpfulVotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where helpfulVotes is not null
        defaultReviewShouldBeFound("helpfulVotes.specified=true");

        // Get all the reviewList where helpfulVotes is null
        defaultReviewShouldNotBeFound("helpfulVotes.specified=false");
    }

    @Test
    @Transactional
    public void getAllReviewsByHelpfulVotesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where helpfulVotes greater than or equals to DEFAULT_HELPFUL_VOTES
        defaultReviewShouldBeFound("helpfulVotes.greaterOrEqualThan=" + DEFAULT_HELPFUL_VOTES);

        // Get all the reviewList where helpfulVotes greater than or equals to UPDATED_HELPFUL_VOTES
        defaultReviewShouldNotBeFound("helpfulVotes.greaterOrEqualThan=" + UPDATED_HELPFUL_VOTES);
    }

    @Test
    @Transactional
    public void getAllReviewsByHelpfulVotesIsLessThanSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where helpfulVotes less than or equals to DEFAULT_HELPFUL_VOTES
        defaultReviewShouldNotBeFound("helpfulVotes.lessThan=" + DEFAULT_HELPFUL_VOTES);

        // Get all the reviewList where helpfulVotes less than or equals to UPDATED_HELPFUL_VOTES
        defaultReviewShouldBeFound("helpfulVotes.lessThan=" + UPDATED_HELPFUL_VOTES);
    }


    @Test
    @Transactional
    public void getAllReviewsByTotalVotesIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where totalVotes equals to DEFAULT_TOTAL_VOTES
        defaultReviewShouldBeFound("totalVotes.equals=" + DEFAULT_TOTAL_VOTES);

        // Get all the reviewList where totalVotes equals to UPDATED_TOTAL_VOTES
        defaultReviewShouldNotBeFound("totalVotes.equals=" + UPDATED_TOTAL_VOTES);
    }

    @Test
    @Transactional
    public void getAllReviewsByTotalVotesIsInShouldWork() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where totalVotes in DEFAULT_TOTAL_VOTES or UPDATED_TOTAL_VOTES
        defaultReviewShouldBeFound("totalVotes.in=" + DEFAULT_TOTAL_VOTES + "," + UPDATED_TOTAL_VOTES);

        // Get all the reviewList where totalVotes equals to UPDATED_TOTAL_VOTES
        defaultReviewShouldNotBeFound("totalVotes.in=" + UPDATED_TOTAL_VOTES);
    }

    @Test
    @Transactional
    public void getAllReviewsByTotalVotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where totalVotes is not null
        defaultReviewShouldBeFound("totalVotes.specified=true");

        // Get all the reviewList where totalVotes is null
        defaultReviewShouldNotBeFound("totalVotes.specified=false");
    }

    @Test
    @Transactional
    public void getAllReviewsByTotalVotesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where totalVotes greater than or equals to DEFAULT_TOTAL_VOTES
        defaultReviewShouldBeFound("totalVotes.greaterOrEqualThan=" + DEFAULT_TOTAL_VOTES);

        // Get all the reviewList where totalVotes greater than or equals to UPDATED_TOTAL_VOTES
        defaultReviewShouldNotBeFound("totalVotes.greaterOrEqualThan=" + UPDATED_TOTAL_VOTES);
    }

    @Test
    @Transactional
    public void getAllReviewsByTotalVotesIsLessThanSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where totalVotes less than or equals to DEFAULT_TOTAL_VOTES
        defaultReviewShouldNotBeFound("totalVotes.lessThan=" + DEFAULT_TOTAL_VOTES);

        // Get all the reviewList where totalVotes less than or equals to UPDATED_TOTAL_VOTES
        defaultReviewShouldBeFound("totalVotes.lessThan=" + UPDATED_TOTAL_VOTES);
    }


    @Test
    @Transactional
    public void getAllReviewsByVerifiedPurchaseIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where verifiedPurchase equals to DEFAULT_VERIFIED_PURCHASE
        defaultReviewShouldBeFound("verifiedPurchase.equals=" + DEFAULT_VERIFIED_PURCHASE);

        // Get all the reviewList where verifiedPurchase equals to UPDATED_VERIFIED_PURCHASE
        defaultReviewShouldNotBeFound("verifiedPurchase.equals=" + UPDATED_VERIFIED_PURCHASE);
    }

    @Test
    @Transactional
    public void getAllReviewsByVerifiedPurchaseIsInShouldWork() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where verifiedPurchase in DEFAULT_VERIFIED_PURCHASE or UPDATED_VERIFIED_PURCHASE
        defaultReviewShouldBeFound("verifiedPurchase.in=" + DEFAULT_VERIFIED_PURCHASE + "," + UPDATED_VERIFIED_PURCHASE);

        // Get all the reviewList where verifiedPurchase equals to UPDATED_VERIFIED_PURCHASE
        defaultReviewShouldNotBeFound("verifiedPurchase.in=" + UPDATED_VERIFIED_PURCHASE);
    }

    @Test
    @Transactional
    public void getAllReviewsByVerifiedPurchaseIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where verifiedPurchase is not null
        defaultReviewShouldBeFound("verifiedPurchase.specified=true");

        // Get all the reviewList where verifiedPurchase is null
        defaultReviewShouldNotBeFound("verifiedPurchase.specified=false");
    }

    @Test
    @Transactional
    public void getAllReviewsByRealNameIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where realName equals to DEFAULT_REAL_NAME
        defaultReviewShouldBeFound("realName.equals=" + DEFAULT_REAL_NAME);

        // Get all the reviewList where realName equals to UPDATED_REAL_NAME
        defaultReviewShouldNotBeFound("realName.equals=" + UPDATED_REAL_NAME);
    }

    @Test
    @Transactional
    public void getAllReviewsByRealNameIsInShouldWork() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where realName in DEFAULT_REAL_NAME or UPDATED_REAL_NAME
        defaultReviewShouldBeFound("realName.in=" + DEFAULT_REAL_NAME + "," + UPDATED_REAL_NAME);

        // Get all the reviewList where realName equals to UPDATED_REAL_NAME
        defaultReviewShouldNotBeFound("realName.in=" + UPDATED_REAL_NAME);
    }

    @Test
    @Transactional
    public void getAllReviewsByRealNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where realName is not null
        defaultReviewShouldBeFound("realName.specified=true");

        // Get all the reviewList where realName is null
        defaultReviewShouldNotBeFound("realName.specified=false");
    }

    @Test
    @Transactional
    public void getAllReviewsByReviewAnalysisIsEqualToSomething() throws Exception {
        // Initialize the database
        ReviewAnalysis reviewAnalysis = ReviewAnalysisResourceIntTest.createEntity(em);
        em.persist(reviewAnalysis);
        em.flush();
        review.setReviewAnalysis(reviewAnalysis);
        reviewRepository.saveAndFlush(review);
        Long reviewAnalysisId = reviewAnalysis.getId();

        // Get all the reviewList where reviewAnalysis equals to reviewAnalysisId
        defaultReviewShouldBeFound("reviewAnalysisId.equals=" + reviewAnalysisId);

        // Get all the reviewList where reviewAnalysis equals to reviewAnalysisId + 1
        defaultReviewShouldNotBeFound("reviewAnalysisId.equals=" + (reviewAnalysisId + 1));
    }


    @Test
    @Transactional
    public void getAllReviewsByReviewerIsEqualToSomething() throws Exception {
        // Initialize the database
        Reviewer reviewer = ReviewerResourceIntTest.createEntity(em);
        em.persist(reviewer);
        em.flush();
        review.addReviewer(reviewer);
        reviewRepository.saveAndFlush(review);
        Long reviewerId = reviewer.getId();

        // Get all the reviewList where reviewer equals to reviewerId
        defaultReviewShouldBeFound("reviewerId.equals=" + reviewerId);

        // Get all the reviewList where reviewer equals to reviewerId + 1
        defaultReviewShouldNotBeFound("reviewerId.equals=" + (reviewerId + 1));
    }


    @Test
    @Transactional
    public void getAllReviewsByProductIsEqualToSomething() throws Exception {
        // Initialize the database
        Product product = ProductResourceIntTest.createEntity(em);
        em.persist(product);
        em.flush();
        review.addProduct(product);
        reviewRepository.saveAndFlush(review);
        Long productId = product.getId();

        // Get all the reviewList where product equals to productId
        defaultReviewShouldBeFound("productId.equals=" + productId);

        // Get all the reviewList where product equals to productId + 1
        defaultReviewShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultReviewShouldBeFound(String filter) throws Exception {
        restReviewMockMvc.perform(get("/api/reviews?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(review.getId().intValue())))
            .andExpect(jsonPath("$.[*].productID").value(hasItem(DEFAULT_PRODUCT_ID.toString())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
            .andExpect(jsonPath("$.[*].productURL").value(hasItem(DEFAULT_PRODUCT_URL.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].reviewContent").value(hasItem(DEFAULT_REVIEW_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].reviewDateTime").value(hasItem(sameInstant(DEFAULT_REVIEW_DATE_TIME))))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].fullRating").value(hasItem(DEFAULT_FULL_RATING)))
            .andExpect(jsonPath("$.[*].reviewStatus").value(hasItem(DEFAULT_REVIEW_STATUS.toString())))
            .andExpect(jsonPath("$.[*].helpfulVotes").value(hasItem(DEFAULT_HELPFUL_VOTES)))
            .andExpect(jsonPath("$.[*].totalVotes").value(hasItem(DEFAULT_TOTAL_VOTES)))
            .andExpect(jsonPath("$.[*].verifiedPurchase").value(hasItem(DEFAULT_VERIFIED_PURCHASE.booleanValue())))
            .andExpect(jsonPath("$.[*].realName").value(hasItem(DEFAULT_REAL_NAME.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultReviewShouldNotBeFound(String filter) throws Exception {
        restReviewMockMvc.perform(get("/api/reviews?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingReview() throws Exception {
        // Get the review
        restReviewMockMvc.perform(get("/api/reviews/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReview() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        int databaseSizeBeforeUpdate = reviewRepository.findAll().size();

        // Update the review
        Review updatedReview = reviewRepository.findById(review.getId()).get();
        // Disconnect from session so that the updates on updatedReview are not directly saved in db
        em.detach(updatedReview);
        updatedReview
            .productID(UPDATED_PRODUCT_ID)
            .uid(UPDATED_UID)
            .productURL(UPDATED_PRODUCT_URL)
            .title(UPDATED_TITLE)
            .reviewContent(UPDATED_REVIEW_CONTENT)
            .language(UPDATED_LANGUAGE)
            .reviewDateTime(UPDATED_REVIEW_DATE_TIME)
            .rating(UPDATED_RATING)
            .fullRating(UPDATED_FULL_RATING)
            .reviewStatus(UPDATED_REVIEW_STATUS)
            .helpfulVotes(UPDATED_HELPFUL_VOTES)
            .totalVotes(UPDATED_TOTAL_VOTES)
            .verifiedPurchase(UPDATED_VERIFIED_PURCHASE)
            .realName(UPDATED_REAL_NAME);
        ReviewDTO reviewDTO = reviewMapper.toDto(updatedReview);

        restReviewMockMvc.perform(put("/api/reviews")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reviewDTO)))
            .andExpect(status().isOk());

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeUpdate);
        Review testReview = reviewList.get(reviewList.size() - 1);
        assertThat(testReview.getProductID()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testReview.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testReview.getProductURL()).isEqualTo(UPDATED_PRODUCT_URL);
        assertThat(testReview.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testReview.getReviewContent()).isEqualTo(UPDATED_REVIEW_CONTENT);
        assertThat(testReview.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testReview.getReviewDateTime()).isEqualTo(UPDATED_REVIEW_DATE_TIME);
        assertThat(testReview.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testReview.getFullRating()).isEqualTo(UPDATED_FULL_RATING);
        assertThat(testReview.getReviewStatus()).isEqualTo(UPDATED_REVIEW_STATUS);
        assertThat(testReview.getHelpfulVotes()).isEqualTo(UPDATED_HELPFUL_VOTES);
        assertThat(testReview.getTotalVotes()).isEqualTo(UPDATED_TOTAL_VOTES);
        assertThat(testReview.isVerifiedPurchase()).isEqualTo(UPDATED_VERIFIED_PURCHASE);
        assertThat(testReview.getRealName()).isEqualTo(UPDATED_REAL_NAME);

        // Validate the Review in Elasticsearch
        verify(mockReviewSearchRepository, times(1)).save(testReview);
    }

    @Test
    @Transactional
    public void updateNonExistingReview() throws Exception {
        int databaseSizeBeforeUpdate = reviewRepository.findAll().size();

        // Create the Review
        ReviewDTO reviewDTO = reviewMapper.toDto(review);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restReviewMockMvc.perform(put("/api/reviews")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reviewDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Review in Elasticsearch
        verify(mockReviewSearchRepository, times(0)).save(review);
    }

    @Test
    @Transactional
    public void deleteReview() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        int databaseSizeBeforeDelete = reviewRepository.findAll().size();

        // Get the review
        restReviewMockMvc.perform(delete("/api/reviews/{id}", review.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Review in Elasticsearch
        verify(mockReviewSearchRepository, times(1)).deleteById(review.getId());
    }

    @Test
    @Transactional
    public void searchReview() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);
        when(mockReviewSearchRepository.search(queryStringQuery("id:" + review.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(review), PageRequest.of(0, 1), 1));
        // Search the review
        restReviewMockMvc.perform(get("/api/_search/reviews?query=id:" + review.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(review.getId().intValue())))
            .andExpect(jsonPath("$.[*].productID").value(hasItem(DEFAULT_PRODUCT_ID.toString())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
            .andExpect(jsonPath("$.[*].productURL").value(hasItem(DEFAULT_PRODUCT_URL.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].reviewContent").value(hasItem(DEFAULT_REVIEW_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].reviewDateTime").value(hasItem(sameInstant(DEFAULT_REVIEW_DATE_TIME))))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].fullRating").value(hasItem(DEFAULT_FULL_RATING)))
            .andExpect(jsonPath("$.[*].reviewStatus").value(hasItem(DEFAULT_REVIEW_STATUS.toString())))
            .andExpect(jsonPath("$.[*].helpfulVotes").value(hasItem(DEFAULT_HELPFUL_VOTES)))
            .andExpect(jsonPath("$.[*].totalVotes").value(hasItem(DEFAULT_TOTAL_VOTES)))
            .andExpect(jsonPath("$.[*].verifiedPurchase").value(hasItem(DEFAULT_VERIFIED_PURCHASE.booleanValue())))
            .andExpect(jsonPath("$.[*].realName").value(hasItem(DEFAULT_REAL_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Review.class);
        Review review1 = new Review();
        review1.setId(1L);
        Review review2 = new Review();
        review2.setId(review1.getId());
        assertThat(review1).isEqualTo(review2);
        review2.setId(2L);
        assertThat(review1).isNotEqualTo(review2);
        review1.setId(null);
        assertThat(review1).isNotEqualTo(review2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReviewDTO.class);
        ReviewDTO reviewDTO1 = new ReviewDTO();
        reviewDTO1.setId(1L);
        ReviewDTO reviewDTO2 = new ReviewDTO();
        assertThat(reviewDTO1).isNotEqualTo(reviewDTO2);
        reviewDTO2.setId(reviewDTO1.getId());
        assertThat(reviewDTO1).isEqualTo(reviewDTO2);
        reviewDTO2.setId(2L);
        assertThat(reviewDTO1).isNotEqualTo(reviewDTO2);
        reviewDTO1.setId(null);
        assertThat(reviewDTO1).isNotEqualTo(reviewDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(reviewMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(reviewMapper.fromId(null)).isNull();
    }
}
