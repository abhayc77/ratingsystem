package com.insightsystems.ratinginsight.web.rest;

import com.insightsystems.ratinginsight.RatingSystemApp;

import com.insightsystems.ratinginsight.domain.Reviewer;
import com.insightsystems.ratinginsight.domain.ReviewerProfile;
import com.insightsystems.ratinginsight.domain.Review;
import com.insightsystems.ratinginsight.repository.ReviewerRepository;
import com.insightsystems.ratinginsight.repository.search.ReviewerSearchRepository;
import com.insightsystems.ratinginsight.service.ReviewerService;
import com.insightsystems.ratinginsight.service.dto.ReviewerDTO;
import com.insightsystems.ratinginsight.service.mapper.ReviewerMapper;
import com.insightsystems.ratinginsight.web.rest.errors.ExceptionTranslator;
import com.insightsystems.ratinginsight.service.dto.ReviewerCriteria;
import com.insightsystems.ratinginsight.service.ReviewerQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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
import java.util.ArrayList;
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
 * Test class for the ReviewerResource REST controller.
 *
 * @see ReviewerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RatingSystemApp.class)
public class ReviewerResourceIntTest {

    private static final String DEFAULT_REVIEWER_ID = "AAAAAAAAAA";
    private static final String UPDATED_REVIEWER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_UID = "AAAAAAAAAA";
    private static final String UPDATED_UID = "BBBBBBBBBB";

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_STREET_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_STREET_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_POSTAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POSTAL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE_PROVINCE = "AAAAAAAAAA";
    private static final String UPDATED_STATE_PROVINCE = "BBBBBBBBBB";

    @Autowired
    private ReviewerRepository reviewerRepository;
    @Mock
    private ReviewerRepository reviewerRepositoryMock;

    @Autowired
    private ReviewerMapper reviewerMapper;
    
    @Mock
    private ReviewerService reviewerServiceMock;

    @Autowired
    private ReviewerService reviewerService;

    /**
     * This repository is mocked in the com.insightsystems.ratinginsight.repository.search test package.
     *
     * @see com.insightsystems.ratinginsight.repository.search.ReviewerSearchRepositoryMockConfiguration
     */
    @Autowired
    private ReviewerSearchRepository mockReviewerSearchRepository;

    @Autowired
    private ReviewerQueryService reviewerQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restReviewerMockMvc;

    private Reviewer reviewer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ReviewerResource reviewerResource = new ReviewerResource(reviewerService, reviewerQueryService);
        this.restReviewerMockMvc = MockMvcBuilders.standaloneSetup(reviewerResource)
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
    public static Reviewer createEntity(EntityManager em) {
        Reviewer reviewer = new Reviewer()
            .reviewerID(DEFAULT_REVIEWER_ID)
            .uid(DEFAULT_UID)
            .username(DEFAULT_USERNAME)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .streetAddress(DEFAULT_STREET_ADDRESS)
            .postalCode(DEFAULT_POSTAL_CODE)
            .city(DEFAULT_CITY)
            .stateProvince(DEFAULT_STATE_PROVINCE);
        return reviewer;
    }

    @Before
    public void initTest() {
        reviewer = createEntity(em);
    }

    @Test
    @Transactional
    public void createReviewer() throws Exception {
        int databaseSizeBeforeCreate = reviewerRepository.findAll().size();

        // Create the Reviewer
        ReviewerDTO reviewerDTO = reviewerMapper.toDto(reviewer);
        restReviewerMockMvc.perform(post("/api/reviewers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reviewerDTO)))
            .andExpect(status().isCreated());

        // Validate the Reviewer in the database
        List<Reviewer> reviewerList = reviewerRepository.findAll();
        assertThat(reviewerList).hasSize(databaseSizeBeforeCreate + 1);
        Reviewer testReviewer = reviewerList.get(reviewerList.size() - 1);
        assertThat(testReviewer.getReviewerID()).isEqualTo(DEFAULT_REVIEWER_ID);
        assertThat(testReviewer.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testReviewer.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testReviewer.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testReviewer.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testReviewer.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testReviewer.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testReviewer.getStreetAddress()).isEqualTo(DEFAULT_STREET_ADDRESS);
        assertThat(testReviewer.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
        assertThat(testReviewer.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testReviewer.getStateProvince()).isEqualTo(DEFAULT_STATE_PROVINCE);

        // Validate the Reviewer in Elasticsearch
        verify(mockReviewerSearchRepository, times(1)).save(testReviewer);
    }

    @Test
    @Transactional
    public void createReviewerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reviewerRepository.findAll().size();

        // Create the Reviewer with an existing ID
        reviewer.setId(1L);
        ReviewerDTO reviewerDTO = reviewerMapper.toDto(reviewer);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReviewerMockMvc.perform(post("/api/reviewers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reviewerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Reviewer in the database
        List<Reviewer> reviewerList = reviewerRepository.findAll();
        assertThat(reviewerList).hasSize(databaseSizeBeforeCreate);

        // Validate the Reviewer in Elasticsearch
        verify(mockReviewerSearchRepository, times(0)).save(reviewer);
    }

    @Test
    @Transactional
    public void getAllReviewers() throws Exception {
        // Initialize the database
        reviewerRepository.saveAndFlush(reviewer);

        // Get all the reviewerList
        restReviewerMockMvc.perform(get("/api/reviewers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reviewer.getId().intValue())))
            .andExpect(jsonPath("$.[*].reviewerID").value(hasItem(DEFAULT_REVIEWER_ID.toString())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].stateProvince").value(hasItem(DEFAULT_STATE_PROVINCE.toString())));
    }
    
    public void getAllReviewersWithEagerRelationshipsIsEnabled() throws Exception {
        ReviewerResource reviewerResource = new ReviewerResource(reviewerServiceMock, reviewerQueryService);
        when(reviewerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restReviewerMockMvc = MockMvcBuilders.standaloneSetup(reviewerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restReviewerMockMvc.perform(get("/api/reviewers?eagerload=true"))
        .andExpect(status().isOk());

        verify(reviewerServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllReviewersWithEagerRelationshipsIsNotEnabled() throws Exception {
        ReviewerResource reviewerResource = new ReviewerResource(reviewerServiceMock, reviewerQueryService);
            when(reviewerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restReviewerMockMvc = MockMvcBuilders.standaloneSetup(reviewerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restReviewerMockMvc.perform(get("/api/reviewers?eagerload=true"))
        .andExpect(status().isOk());

            verify(reviewerServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getReviewer() throws Exception {
        // Initialize the database
        reviewerRepository.saveAndFlush(reviewer);

        // Get the reviewer
        restReviewerMockMvc.perform(get("/api/reviewers/{id}", reviewer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reviewer.getId().intValue()))
            .andExpect(jsonPath("$.reviewerID").value(DEFAULT_REVIEWER_ID.toString()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID.toString()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME.toString()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.toString()))
            .andExpect(jsonPath("$.streetAddress").value(DEFAULT_STREET_ADDRESS.toString()))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.stateProvince").value(DEFAULT_STATE_PROVINCE.toString()));
    }

    @Test
    @Transactional
    public void getAllReviewersByReviewerIDIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewerRepository.saveAndFlush(reviewer);

        // Get all the reviewerList where reviewerID equals to DEFAULT_REVIEWER_ID
        defaultReviewerShouldBeFound("reviewerID.equals=" + DEFAULT_REVIEWER_ID);

        // Get all the reviewerList where reviewerID equals to UPDATED_REVIEWER_ID
        defaultReviewerShouldNotBeFound("reviewerID.equals=" + UPDATED_REVIEWER_ID);
    }

    @Test
    @Transactional
    public void getAllReviewersByReviewerIDIsInShouldWork() throws Exception {
        // Initialize the database
        reviewerRepository.saveAndFlush(reviewer);

        // Get all the reviewerList where reviewerID in DEFAULT_REVIEWER_ID or UPDATED_REVIEWER_ID
        defaultReviewerShouldBeFound("reviewerID.in=" + DEFAULT_REVIEWER_ID + "," + UPDATED_REVIEWER_ID);

        // Get all the reviewerList where reviewerID equals to UPDATED_REVIEWER_ID
        defaultReviewerShouldNotBeFound("reviewerID.in=" + UPDATED_REVIEWER_ID);
    }

    @Test
    @Transactional
    public void getAllReviewersByReviewerIDIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewerRepository.saveAndFlush(reviewer);

        // Get all the reviewerList where reviewerID is not null
        defaultReviewerShouldBeFound("reviewerID.specified=true");

        // Get all the reviewerList where reviewerID is null
        defaultReviewerShouldNotBeFound("reviewerID.specified=false");
    }

    @Test
    @Transactional
    public void getAllReviewersByUidIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewerRepository.saveAndFlush(reviewer);

        // Get all the reviewerList where uid equals to DEFAULT_UID
        defaultReviewerShouldBeFound("uid.equals=" + DEFAULT_UID);

        // Get all the reviewerList where uid equals to UPDATED_UID
        defaultReviewerShouldNotBeFound("uid.equals=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllReviewersByUidIsInShouldWork() throws Exception {
        // Initialize the database
        reviewerRepository.saveAndFlush(reviewer);

        // Get all the reviewerList where uid in DEFAULT_UID or UPDATED_UID
        defaultReviewerShouldBeFound("uid.in=" + DEFAULT_UID + "," + UPDATED_UID);

        // Get all the reviewerList where uid equals to UPDATED_UID
        defaultReviewerShouldNotBeFound("uid.in=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllReviewersByUidIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewerRepository.saveAndFlush(reviewer);

        // Get all the reviewerList where uid is not null
        defaultReviewerShouldBeFound("uid.specified=true");

        // Get all the reviewerList where uid is null
        defaultReviewerShouldNotBeFound("uid.specified=false");
    }

    @Test
    @Transactional
    public void getAllReviewersByUsernameIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewerRepository.saveAndFlush(reviewer);

        // Get all the reviewerList where username equals to DEFAULT_USERNAME
        defaultReviewerShouldBeFound("username.equals=" + DEFAULT_USERNAME);

        // Get all the reviewerList where username equals to UPDATED_USERNAME
        defaultReviewerShouldNotBeFound("username.equals=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    public void getAllReviewersByUsernameIsInShouldWork() throws Exception {
        // Initialize the database
        reviewerRepository.saveAndFlush(reviewer);

        // Get all the reviewerList where username in DEFAULT_USERNAME or UPDATED_USERNAME
        defaultReviewerShouldBeFound("username.in=" + DEFAULT_USERNAME + "," + UPDATED_USERNAME);

        // Get all the reviewerList where username equals to UPDATED_USERNAME
        defaultReviewerShouldNotBeFound("username.in=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    public void getAllReviewersByUsernameIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewerRepository.saveAndFlush(reviewer);

        // Get all the reviewerList where username is not null
        defaultReviewerShouldBeFound("username.specified=true");

        // Get all the reviewerList where username is null
        defaultReviewerShouldNotBeFound("username.specified=false");
    }

    @Test
    @Transactional
    public void getAllReviewersByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewerRepository.saveAndFlush(reviewer);

        // Get all the reviewerList where firstName equals to DEFAULT_FIRST_NAME
        defaultReviewerShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the reviewerList where firstName equals to UPDATED_FIRST_NAME
        defaultReviewerShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllReviewersByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        reviewerRepository.saveAndFlush(reviewer);

        // Get all the reviewerList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultReviewerShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the reviewerList where firstName equals to UPDATED_FIRST_NAME
        defaultReviewerShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllReviewersByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewerRepository.saveAndFlush(reviewer);

        // Get all the reviewerList where firstName is not null
        defaultReviewerShouldBeFound("firstName.specified=true");

        // Get all the reviewerList where firstName is null
        defaultReviewerShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    public void getAllReviewersByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewerRepository.saveAndFlush(reviewer);

        // Get all the reviewerList where lastName equals to DEFAULT_LAST_NAME
        defaultReviewerShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the reviewerList where lastName equals to UPDATED_LAST_NAME
        defaultReviewerShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllReviewersByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        reviewerRepository.saveAndFlush(reviewer);

        // Get all the reviewerList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultReviewerShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the reviewerList where lastName equals to UPDATED_LAST_NAME
        defaultReviewerShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllReviewersByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewerRepository.saveAndFlush(reviewer);

        // Get all the reviewerList where lastName is not null
        defaultReviewerShouldBeFound("lastName.specified=true");

        // Get all the reviewerList where lastName is null
        defaultReviewerShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    public void getAllReviewersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewerRepository.saveAndFlush(reviewer);

        // Get all the reviewerList where email equals to DEFAULT_EMAIL
        defaultReviewerShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the reviewerList where email equals to UPDATED_EMAIL
        defaultReviewerShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllReviewersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        reviewerRepository.saveAndFlush(reviewer);

        // Get all the reviewerList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultReviewerShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the reviewerList where email equals to UPDATED_EMAIL
        defaultReviewerShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllReviewersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewerRepository.saveAndFlush(reviewer);

        // Get all the reviewerList where email is not null
        defaultReviewerShouldBeFound("email.specified=true");

        // Get all the reviewerList where email is null
        defaultReviewerShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    public void getAllReviewersByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewerRepository.saveAndFlush(reviewer);

        // Get all the reviewerList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultReviewerShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the reviewerList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultReviewerShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllReviewersByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        reviewerRepository.saveAndFlush(reviewer);

        // Get all the reviewerList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultReviewerShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the reviewerList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultReviewerShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllReviewersByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewerRepository.saveAndFlush(reviewer);

        // Get all the reviewerList where phoneNumber is not null
        defaultReviewerShouldBeFound("phoneNumber.specified=true");

        // Get all the reviewerList where phoneNumber is null
        defaultReviewerShouldNotBeFound("phoneNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllReviewersByStreetAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewerRepository.saveAndFlush(reviewer);

        // Get all the reviewerList where streetAddress equals to DEFAULT_STREET_ADDRESS
        defaultReviewerShouldBeFound("streetAddress.equals=" + DEFAULT_STREET_ADDRESS);

        // Get all the reviewerList where streetAddress equals to UPDATED_STREET_ADDRESS
        defaultReviewerShouldNotBeFound("streetAddress.equals=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllReviewersByStreetAddressIsInShouldWork() throws Exception {
        // Initialize the database
        reviewerRepository.saveAndFlush(reviewer);

        // Get all the reviewerList where streetAddress in DEFAULT_STREET_ADDRESS or UPDATED_STREET_ADDRESS
        defaultReviewerShouldBeFound("streetAddress.in=" + DEFAULT_STREET_ADDRESS + "," + UPDATED_STREET_ADDRESS);

        // Get all the reviewerList where streetAddress equals to UPDATED_STREET_ADDRESS
        defaultReviewerShouldNotBeFound("streetAddress.in=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllReviewersByStreetAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewerRepository.saveAndFlush(reviewer);

        // Get all the reviewerList where streetAddress is not null
        defaultReviewerShouldBeFound("streetAddress.specified=true");

        // Get all the reviewerList where streetAddress is null
        defaultReviewerShouldNotBeFound("streetAddress.specified=false");
    }

    @Test
    @Transactional
    public void getAllReviewersByPostalCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewerRepository.saveAndFlush(reviewer);

        // Get all the reviewerList where postalCode equals to DEFAULT_POSTAL_CODE
        defaultReviewerShouldBeFound("postalCode.equals=" + DEFAULT_POSTAL_CODE);

        // Get all the reviewerList where postalCode equals to UPDATED_POSTAL_CODE
        defaultReviewerShouldNotBeFound("postalCode.equals=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    public void getAllReviewersByPostalCodeIsInShouldWork() throws Exception {
        // Initialize the database
        reviewerRepository.saveAndFlush(reviewer);

        // Get all the reviewerList where postalCode in DEFAULT_POSTAL_CODE or UPDATED_POSTAL_CODE
        defaultReviewerShouldBeFound("postalCode.in=" + DEFAULT_POSTAL_CODE + "," + UPDATED_POSTAL_CODE);

        // Get all the reviewerList where postalCode equals to UPDATED_POSTAL_CODE
        defaultReviewerShouldNotBeFound("postalCode.in=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    public void getAllReviewersByPostalCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewerRepository.saveAndFlush(reviewer);

        // Get all the reviewerList where postalCode is not null
        defaultReviewerShouldBeFound("postalCode.specified=true");

        // Get all the reviewerList where postalCode is null
        defaultReviewerShouldNotBeFound("postalCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllReviewersByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewerRepository.saveAndFlush(reviewer);

        // Get all the reviewerList where city equals to DEFAULT_CITY
        defaultReviewerShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the reviewerList where city equals to UPDATED_CITY
        defaultReviewerShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllReviewersByCityIsInShouldWork() throws Exception {
        // Initialize the database
        reviewerRepository.saveAndFlush(reviewer);

        // Get all the reviewerList where city in DEFAULT_CITY or UPDATED_CITY
        defaultReviewerShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the reviewerList where city equals to UPDATED_CITY
        defaultReviewerShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllReviewersByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewerRepository.saveAndFlush(reviewer);

        // Get all the reviewerList where city is not null
        defaultReviewerShouldBeFound("city.specified=true");

        // Get all the reviewerList where city is null
        defaultReviewerShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    public void getAllReviewersByStateProvinceIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewerRepository.saveAndFlush(reviewer);

        // Get all the reviewerList where stateProvince equals to DEFAULT_STATE_PROVINCE
        defaultReviewerShouldBeFound("stateProvince.equals=" + DEFAULT_STATE_PROVINCE);

        // Get all the reviewerList where stateProvince equals to UPDATED_STATE_PROVINCE
        defaultReviewerShouldNotBeFound("stateProvince.equals=" + UPDATED_STATE_PROVINCE);
    }

    @Test
    @Transactional
    public void getAllReviewersByStateProvinceIsInShouldWork() throws Exception {
        // Initialize the database
        reviewerRepository.saveAndFlush(reviewer);

        // Get all the reviewerList where stateProvince in DEFAULT_STATE_PROVINCE or UPDATED_STATE_PROVINCE
        defaultReviewerShouldBeFound("stateProvince.in=" + DEFAULT_STATE_PROVINCE + "," + UPDATED_STATE_PROVINCE);

        // Get all the reviewerList where stateProvince equals to UPDATED_STATE_PROVINCE
        defaultReviewerShouldNotBeFound("stateProvince.in=" + UPDATED_STATE_PROVINCE);
    }

    @Test
    @Transactional
    public void getAllReviewersByStateProvinceIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewerRepository.saveAndFlush(reviewer);

        // Get all the reviewerList where stateProvince is not null
        defaultReviewerShouldBeFound("stateProvince.specified=true");

        // Get all the reviewerList where stateProvince is null
        defaultReviewerShouldNotBeFound("stateProvince.specified=false");
    }

    @Test
    @Transactional
    public void getAllReviewersByProfileIsEqualToSomething() throws Exception {
        // Initialize the database
        ReviewerProfile profile = ReviewerProfileResourceIntTest.createEntity(em);
        em.persist(profile);
        em.flush();
        reviewer.setProfile(profile);
        reviewerRepository.saveAndFlush(reviewer);
        Long profileId = profile.getId();

        // Get all the reviewerList where profile equals to profileId
        defaultReviewerShouldBeFound("profileId.equals=" + profileId);

        // Get all the reviewerList where profile equals to profileId + 1
        defaultReviewerShouldNotBeFound("profileId.equals=" + (profileId + 1));
    }


    @Test
    @Transactional
    public void getAllReviewersByReviewIsEqualToSomething() throws Exception {
        // Initialize the database
        Review review = ReviewResourceIntTest.createEntity(em);
        em.persist(review);
        em.flush();
        reviewer.addReview(review);
        reviewerRepository.saveAndFlush(reviewer);
        Long reviewId = review.getId();

        // Get all the reviewerList where review equals to reviewId
        defaultReviewerShouldBeFound("reviewId.equals=" + reviewId);

        // Get all the reviewerList where review equals to reviewId + 1
        defaultReviewerShouldNotBeFound("reviewId.equals=" + (reviewId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultReviewerShouldBeFound(String filter) throws Exception {
        restReviewerMockMvc.perform(get("/api/reviewers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reviewer.getId().intValue())))
            .andExpect(jsonPath("$.[*].reviewerID").value(hasItem(DEFAULT_REVIEWER_ID.toString())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].stateProvince").value(hasItem(DEFAULT_STATE_PROVINCE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultReviewerShouldNotBeFound(String filter) throws Exception {
        restReviewerMockMvc.perform(get("/api/reviewers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingReviewer() throws Exception {
        // Get the reviewer
        restReviewerMockMvc.perform(get("/api/reviewers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReviewer() throws Exception {
        // Initialize the database
        reviewerRepository.saveAndFlush(reviewer);

        int databaseSizeBeforeUpdate = reviewerRepository.findAll().size();

        // Update the reviewer
        Reviewer updatedReviewer = reviewerRepository.findById(reviewer.getId()).get();
        // Disconnect from session so that the updates on updatedReviewer are not directly saved in db
        em.detach(updatedReviewer);
        updatedReviewer
            .reviewerID(UPDATED_REVIEWER_ID)
            .uid(UPDATED_UID)
            .username(UPDATED_USERNAME)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .streetAddress(UPDATED_STREET_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE)
            .city(UPDATED_CITY)
            .stateProvince(UPDATED_STATE_PROVINCE);
        ReviewerDTO reviewerDTO = reviewerMapper.toDto(updatedReviewer);

        restReviewerMockMvc.perform(put("/api/reviewers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reviewerDTO)))
            .andExpect(status().isOk());

        // Validate the Reviewer in the database
        List<Reviewer> reviewerList = reviewerRepository.findAll();
        assertThat(reviewerList).hasSize(databaseSizeBeforeUpdate);
        Reviewer testReviewer = reviewerList.get(reviewerList.size() - 1);
        assertThat(testReviewer.getReviewerID()).isEqualTo(UPDATED_REVIEWER_ID);
        assertThat(testReviewer.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testReviewer.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testReviewer.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testReviewer.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testReviewer.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testReviewer.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testReviewer.getStreetAddress()).isEqualTo(UPDATED_STREET_ADDRESS);
        assertThat(testReviewer.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testReviewer.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testReviewer.getStateProvince()).isEqualTo(UPDATED_STATE_PROVINCE);

        // Validate the Reviewer in Elasticsearch
        verify(mockReviewerSearchRepository, times(1)).save(testReviewer);
    }

    @Test
    @Transactional
    public void updateNonExistingReviewer() throws Exception {
        int databaseSizeBeforeUpdate = reviewerRepository.findAll().size();

        // Create the Reviewer
        ReviewerDTO reviewerDTO = reviewerMapper.toDto(reviewer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restReviewerMockMvc.perform(put("/api/reviewers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reviewerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Reviewer in the database
        List<Reviewer> reviewerList = reviewerRepository.findAll();
        assertThat(reviewerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Reviewer in Elasticsearch
        verify(mockReviewerSearchRepository, times(0)).save(reviewer);
    }

    @Test
    @Transactional
    public void deleteReviewer() throws Exception {
        // Initialize the database
        reviewerRepository.saveAndFlush(reviewer);

        int databaseSizeBeforeDelete = reviewerRepository.findAll().size();

        // Get the reviewer
        restReviewerMockMvc.perform(delete("/api/reviewers/{id}", reviewer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Reviewer> reviewerList = reviewerRepository.findAll();
        assertThat(reviewerList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Reviewer in Elasticsearch
        verify(mockReviewerSearchRepository, times(1)).deleteById(reviewer.getId());
    }

    @Test
    @Transactional
    public void searchReviewer() throws Exception {
        // Initialize the database
        reviewerRepository.saveAndFlush(reviewer);
        when(mockReviewerSearchRepository.search(queryStringQuery("id:" + reviewer.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(reviewer), PageRequest.of(0, 1), 1));
        // Search the reviewer
        restReviewerMockMvc.perform(get("/api/_search/reviewers?query=id:" + reviewer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reviewer.getId().intValue())))
            .andExpect(jsonPath("$.[*].reviewerID").value(hasItem(DEFAULT_REVIEWER_ID.toString())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].stateProvince").value(hasItem(DEFAULT_STATE_PROVINCE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reviewer.class);
        Reviewer reviewer1 = new Reviewer();
        reviewer1.setId(1L);
        Reviewer reviewer2 = new Reviewer();
        reviewer2.setId(reviewer1.getId());
        assertThat(reviewer1).isEqualTo(reviewer2);
        reviewer2.setId(2L);
        assertThat(reviewer1).isNotEqualTo(reviewer2);
        reviewer1.setId(null);
        assertThat(reviewer1).isNotEqualTo(reviewer2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReviewerDTO.class);
        ReviewerDTO reviewerDTO1 = new ReviewerDTO();
        reviewerDTO1.setId(1L);
        ReviewerDTO reviewerDTO2 = new ReviewerDTO();
        assertThat(reviewerDTO1).isNotEqualTo(reviewerDTO2);
        reviewerDTO2.setId(reviewerDTO1.getId());
        assertThat(reviewerDTO1).isEqualTo(reviewerDTO2);
        reviewerDTO2.setId(2L);
        assertThat(reviewerDTO1).isNotEqualTo(reviewerDTO2);
        reviewerDTO1.setId(null);
        assertThat(reviewerDTO1).isNotEqualTo(reviewerDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(reviewerMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(reviewerMapper.fromId(null)).isNull();
    }
}
