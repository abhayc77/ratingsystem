package com.insightsystems.ratinginsight.web.rest;

import com.insightsystems.ratinginsight.RatingSystemApp;

import com.insightsystems.ratinginsight.domain.Product;
import com.insightsystems.ratinginsight.domain.Review;
import com.insightsystems.ratinginsight.repository.ProductRepository;
import com.insightsystems.ratinginsight.repository.search.ProductSearchRepository;
import com.insightsystems.ratinginsight.service.ProductService;
import com.insightsystems.ratinginsight.service.dto.ProductDTO;
import com.insightsystems.ratinginsight.service.mapper.ProductMapper;
import com.insightsystems.ratinginsight.web.rest.errors.ExceptionTranslator;
import com.insightsystems.ratinginsight.service.dto.ProductCriteria;
import com.insightsystems.ratinginsight.service.ProductQueryService;

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

import com.insightsystems.ratinginsight.domain.enumeration.ProductStatus;
/**
 * Test class for the ProductResource REST controller.
 *
 * @see ProductResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RatingSystemApp.class)
public class ProductResourceIntTest {

    private static final String DEFAULT_PRODUCT_ID = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_UID = "AAAAAAAAAA";
    private static final String UPDATED_UID = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final String DEFAULT_SHORT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_LONG_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_LONG_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_URL = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_URL = "BBBBBBBBBB";

    private static final String DEFAULT_SUPPLIER_ID = "AAAAAAAAAA";
    private static final String UPDATED_SUPPLIER_ID = "BBBBBBBBBB";

    private static final Float DEFAULT_AVERAGE_RATING = 1F;
    private static final Float UPDATED_AVERAGE_RATING = 2F;

    private static final Integer DEFAULT_RATING_COUNT = 1;
    private static final Integer UPDATED_RATING_COUNT = 2;

    private static final ProductStatus DEFAULT_PRODUCT_STATUS = ProductStatus.UNKNOWN;
    private static final ProductStatus UPDATED_PRODUCT_STATUS = ProductStatus.ACTIVE;

    @Autowired
    private ProductRepository productRepository;
    @Mock
    private ProductRepository productRepositoryMock;

    @Autowired
    private ProductMapper productMapper;
    
    @Mock
    private ProductService productServiceMock;

    @Autowired
    private ProductService productService;

    /**
     * This repository is mocked in the com.insightsystems.ratinginsight.repository.search test package.
     *
     * @see com.insightsystems.ratinginsight.repository.search.ProductSearchRepositoryMockConfiguration
     */
    @Autowired
    private ProductSearchRepository mockProductSearchRepository;

    @Autowired
    private ProductQueryService productQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProductMockMvc;

    private Product product;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProductResource productResource = new ProductResource(productService, productQueryService);
        this.restProductMockMvc = MockMvcBuilders.standaloneSetup(productResource)
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
    public static Product createEntity(EntityManager em) {
        Product product = new Product()
            .productID(DEFAULT_PRODUCT_ID)
            .uid(DEFAULT_UID)
            .productName(DEFAULT_PRODUCT_NAME)
            .price(DEFAULT_PRICE)
            .shortDescription(DEFAULT_SHORT_DESCRIPTION)
            .longDescription(DEFAULT_LONG_DESCRIPTION)
            .productImageURL(DEFAULT_PRODUCT_IMAGE_URL)
            .productURL(DEFAULT_PRODUCT_URL)
            .supplierID(DEFAULT_SUPPLIER_ID)
            .averageRating(DEFAULT_AVERAGE_RATING)
            .ratingCount(DEFAULT_RATING_COUNT)
            .productStatus(DEFAULT_PRODUCT_STATUS);
        return product;
    }

    @Before
    public void initTest() {
        product = createEntity(em);
    }

    @Test
    @Transactional
    public void createProduct() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);
        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isCreated());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate + 1);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getProductID()).isEqualTo(DEFAULT_PRODUCT_ID);
        assertThat(testProduct.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testProduct.getProductName()).isEqualTo(DEFAULT_PRODUCT_NAME);
        assertThat(testProduct.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testProduct.getShortDescription()).isEqualTo(DEFAULT_SHORT_DESCRIPTION);
        assertThat(testProduct.getLongDescription()).isEqualTo(DEFAULT_LONG_DESCRIPTION);
        assertThat(testProduct.getProductImageURL()).isEqualTo(DEFAULT_PRODUCT_IMAGE_URL);
        assertThat(testProduct.getProductURL()).isEqualTo(DEFAULT_PRODUCT_URL);
        assertThat(testProduct.getSupplierID()).isEqualTo(DEFAULT_SUPPLIER_ID);
        assertThat(testProduct.getAverageRating()).isEqualTo(DEFAULT_AVERAGE_RATING);
        assertThat(testProduct.getRatingCount()).isEqualTo(DEFAULT_RATING_COUNT);
        assertThat(testProduct.getProductStatus()).isEqualTo(DEFAULT_PRODUCT_STATUS);

        // Validate the Product in Elasticsearch
        verify(mockProductSearchRepository, times(1)).save(testProduct);
    }

    @Test
    @Transactional
    public void createProductWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();

        // Create the Product with an existing ID
        product.setId(1L);
        ProductDTO productDTO = productMapper.toDto(product);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate);

        // Validate the Product in Elasticsearch
        verify(mockProductSearchRepository, times(0)).save(product);
    }

    @Test
    @Transactional
    public void getAllProducts() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList
        restProductMockMvc.perform(get("/api/products?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].productID").value(hasItem(DEFAULT_PRODUCT_ID.toString())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].shortDescription").value(hasItem(DEFAULT_SHORT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].longDescription").value(hasItem(DEFAULT_LONG_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].productImageURL").value(hasItem(DEFAULT_PRODUCT_IMAGE_URL.toString())))
            .andExpect(jsonPath("$.[*].productURL").value(hasItem(DEFAULT_PRODUCT_URL.toString())))
            .andExpect(jsonPath("$.[*].supplierID").value(hasItem(DEFAULT_SUPPLIER_ID.toString())))
            .andExpect(jsonPath("$.[*].averageRating").value(hasItem(DEFAULT_AVERAGE_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].ratingCount").value(hasItem(DEFAULT_RATING_COUNT)))
            .andExpect(jsonPath("$.[*].productStatus").value(hasItem(DEFAULT_PRODUCT_STATUS.toString())));
    }
    
    public void getAllProductsWithEagerRelationshipsIsEnabled() throws Exception {
        ProductResource productResource = new ProductResource(productServiceMock, productQueryService);
        when(productServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restProductMockMvc = MockMvcBuilders.standaloneSetup(productResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restProductMockMvc.perform(get("/api/products?eagerload=true"))
        .andExpect(status().isOk());

        verify(productServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllProductsWithEagerRelationshipsIsNotEnabled() throws Exception {
        ProductResource productResource = new ProductResource(productServiceMock, productQueryService);
            when(productServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restProductMockMvc = MockMvcBuilders.standaloneSetup(productResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restProductMockMvc.perform(get("/api/products?eagerload=true"))
        .andExpect(status().isOk());

            verify(productServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get the product
        restProductMockMvc.perform(get("/api/products/{id}", product.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(product.getId().intValue()))
            .andExpect(jsonPath("$.productID").value(DEFAULT_PRODUCT_ID.toString()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID.toString()))
            .andExpect(jsonPath("$.productName").value(DEFAULT_PRODUCT_NAME.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.shortDescription").value(DEFAULT_SHORT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.longDescription").value(DEFAULT_LONG_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.productImageURL").value(DEFAULT_PRODUCT_IMAGE_URL.toString()))
            .andExpect(jsonPath("$.productURL").value(DEFAULT_PRODUCT_URL.toString()))
            .andExpect(jsonPath("$.supplierID").value(DEFAULT_SUPPLIER_ID.toString()))
            .andExpect(jsonPath("$.averageRating").value(DEFAULT_AVERAGE_RATING.doubleValue()))
            .andExpect(jsonPath("$.ratingCount").value(DEFAULT_RATING_COUNT))
            .andExpect(jsonPath("$.productStatus").value(DEFAULT_PRODUCT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getAllProductsByProductIDIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productID equals to DEFAULT_PRODUCT_ID
        defaultProductShouldBeFound("productID.equals=" + DEFAULT_PRODUCT_ID);

        // Get all the productList where productID equals to UPDATED_PRODUCT_ID
        defaultProductShouldNotBeFound("productID.equals=" + UPDATED_PRODUCT_ID);
    }

    @Test
    @Transactional
    public void getAllProductsByProductIDIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productID in DEFAULT_PRODUCT_ID or UPDATED_PRODUCT_ID
        defaultProductShouldBeFound("productID.in=" + DEFAULT_PRODUCT_ID + "," + UPDATED_PRODUCT_ID);

        // Get all the productList where productID equals to UPDATED_PRODUCT_ID
        defaultProductShouldNotBeFound("productID.in=" + UPDATED_PRODUCT_ID);
    }

    @Test
    @Transactional
    public void getAllProductsByProductIDIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productID is not null
        defaultProductShouldBeFound("productID.specified=true");

        // Get all the productList where productID is null
        defaultProductShouldNotBeFound("productID.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByUidIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where uid equals to DEFAULT_UID
        defaultProductShouldBeFound("uid.equals=" + DEFAULT_UID);

        // Get all the productList where uid equals to UPDATED_UID
        defaultProductShouldNotBeFound("uid.equals=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllProductsByUidIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where uid in DEFAULT_UID or UPDATED_UID
        defaultProductShouldBeFound("uid.in=" + DEFAULT_UID + "," + UPDATED_UID);

        // Get all the productList where uid equals to UPDATED_UID
        defaultProductShouldNotBeFound("uid.in=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllProductsByUidIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where uid is not null
        defaultProductShouldBeFound("uid.specified=true");

        // Get all the productList where uid is null
        defaultProductShouldNotBeFound("uid.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByProductNameIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productName equals to DEFAULT_PRODUCT_NAME
        defaultProductShouldBeFound("productName.equals=" + DEFAULT_PRODUCT_NAME);

        // Get all the productList where productName equals to UPDATED_PRODUCT_NAME
        defaultProductShouldNotBeFound("productName.equals=" + UPDATED_PRODUCT_NAME);
    }

    @Test
    @Transactional
    public void getAllProductsByProductNameIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productName in DEFAULT_PRODUCT_NAME or UPDATED_PRODUCT_NAME
        defaultProductShouldBeFound("productName.in=" + DEFAULT_PRODUCT_NAME + "," + UPDATED_PRODUCT_NAME);

        // Get all the productList where productName equals to UPDATED_PRODUCT_NAME
        defaultProductShouldNotBeFound("productName.in=" + UPDATED_PRODUCT_NAME);
    }

    @Test
    @Transactional
    public void getAllProductsByProductNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productName is not null
        defaultProductShouldBeFound("productName.specified=true");

        // Get all the productList where productName is null
        defaultProductShouldNotBeFound("productName.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price equals to DEFAULT_PRICE
        defaultProductShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the productList where price equals to UPDATED_PRICE
        defaultProductShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultProductShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the productList where price equals to UPDATED_PRICE
        defaultProductShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price is not null
        defaultProductShouldBeFound("price.specified=true");

        // Get all the productList where price is null
        defaultProductShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByShortDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where shortDescription equals to DEFAULT_SHORT_DESCRIPTION
        defaultProductShouldBeFound("shortDescription.equals=" + DEFAULT_SHORT_DESCRIPTION);

        // Get all the productList where shortDescription equals to UPDATED_SHORT_DESCRIPTION
        defaultProductShouldNotBeFound("shortDescription.equals=" + UPDATED_SHORT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProductsByShortDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where shortDescription in DEFAULT_SHORT_DESCRIPTION or UPDATED_SHORT_DESCRIPTION
        defaultProductShouldBeFound("shortDescription.in=" + DEFAULT_SHORT_DESCRIPTION + "," + UPDATED_SHORT_DESCRIPTION);

        // Get all the productList where shortDescription equals to UPDATED_SHORT_DESCRIPTION
        defaultProductShouldNotBeFound("shortDescription.in=" + UPDATED_SHORT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProductsByShortDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where shortDescription is not null
        defaultProductShouldBeFound("shortDescription.specified=true");

        // Get all the productList where shortDescription is null
        defaultProductShouldNotBeFound("shortDescription.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByLongDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where longDescription equals to DEFAULT_LONG_DESCRIPTION
        defaultProductShouldBeFound("longDescription.equals=" + DEFAULT_LONG_DESCRIPTION);

        // Get all the productList where longDescription equals to UPDATED_LONG_DESCRIPTION
        defaultProductShouldNotBeFound("longDescription.equals=" + UPDATED_LONG_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProductsByLongDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where longDescription in DEFAULT_LONG_DESCRIPTION or UPDATED_LONG_DESCRIPTION
        defaultProductShouldBeFound("longDescription.in=" + DEFAULT_LONG_DESCRIPTION + "," + UPDATED_LONG_DESCRIPTION);

        // Get all the productList where longDescription equals to UPDATED_LONG_DESCRIPTION
        defaultProductShouldNotBeFound("longDescription.in=" + UPDATED_LONG_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProductsByLongDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where longDescription is not null
        defaultProductShouldBeFound("longDescription.specified=true");

        // Get all the productList where longDescription is null
        defaultProductShouldNotBeFound("longDescription.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByProductImageURLIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productImageURL equals to DEFAULT_PRODUCT_IMAGE_URL
        defaultProductShouldBeFound("productImageURL.equals=" + DEFAULT_PRODUCT_IMAGE_URL);

        // Get all the productList where productImageURL equals to UPDATED_PRODUCT_IMAGE_URL
        defaultProductShouldNotBeFound("productImageURL.equals=" + UPDATED_PRODUCT_IMAGE_URL);
    }

    @Test
    @Transactional
    public void getAllProductsByProductImageURLIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productImageURL in DEFAULT_PRODUCT_IMAGE_URL or UPDATED_PRODUCT_IMAGE_URL
        defaultProductShouldBeFound("productImageURL.in=" + DEFAULT_PRODUCT_IMAGE_URL + "," + UPDATED_PRODUCT_IMAGE_URL);

        // Get all the productList where productImageURL equals to UPDATED_PRODUCT_IMAGE_URL
        defaultProductShouldNotBeFound("productImageURL.in=" + UPDATED_PRODUCT_IMAGE_URL);
    }

    @Test
    @Transactional
    public void getAllProductsByProductImageURLIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productImageURL is not null
        defaultProductShouldBeFound("productImageURL.specified=true");

        // Get all the productList where productImageURL is null
        defaultProductShouldNotBeFound("productImageURL.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByProductURLIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productURL equals to DEFAULT_PRODUCT_URL
        defaultProductShouldBeFound("productURL.equals=" + DEFAULT_PRODUCT_URL);

        // Get all the productList where productURL equals to UPDATED_PRODUCT_URL
        defaultProductShouldNotBeFound("productURL.equals=" + UPDATED_PRODUCT_URL);
    }

    @Test
    @Transactional
    public void getAllProductsByProductURLIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productURL in DEFAULT_PRODUCT_URL or UPDATED_PRODUCT_URL
        defaultProductShouldBeFound("productURL.in=" + DEFAULT_PRODUCT_URL + "," + UPDATED_PRODUCT_URL);

        // Get all the productList where productURL equals to UPDATED_PRODUCT_URL
        defaultProductShouldNotBeFound("productURL.in=" + UPDATED_PRODUCT_URL);
    }

    @Test
    @Transactional
    public void getAllProductsByProductURLIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productURL is not null
        defaultProductShouldBeFound("productURL.specified=true");

        // Get all the productList where productURL is null
        defaultProductShouldNotBeFound("productURL.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsBySupplierIDIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where supplierID equals to DEFAULT_SUPPLIER_ID
        defaultProductShouldBeFound("supplierID.equals=" + DEFAULT_SUPPLIER_ID);

        // Get all the productList where supplierID equals to UPDATED_SUPPLIER_ID
        defaultProductShouldNotBeFound("supplierID.equals=" + UPDATED_SUPPLIER_ID);
    }

    @Test
    @Transactional
    public void getAllProductsBySupplierIDIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where supplierID in DEFAULT_SUPPLIER_ID or UPDATED_SUPPLIER_ID
        defaultProductShouldBeFound("supplierID.in=" + DEFAULT_SUPPLIER_ID + "," + UPDATED_SUPPLIER_ID);

        // Get all the productList where supplierID equals to UPDATED_SUPPLIER_ID
        defaultProductShouldNotBeFound("supplierID.in=" + UPDATED_SUPPLIER_ID);
    }

    @Test
    @Transactional
    public void getAllProductsBySupplierIDIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where supplierID is not null
        defaultProductShouldBeFound("supplierID.specified=true");

        // Get all the productList where supplierID is null
        defaultProductShouldNotBeFound("supplierID.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByAverageRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where averageRating equals to DEFAULT_AVERAGE_RATING
        defaultProductShouldBeFound("averageRating.equals=" + DEFAULT_AVERAGE_RATING);

        // Get all the productList where averageRating equals to UPDATED_AVERAGE_RATING
        defaultProductShouldNotBeFound("averageRating.equals=" + UPDATED_AVERAGE_RATING);
    }

    @Test
    @Transactional
    public void getAllProductsByAverageRatingIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where averageRating in DEFAULT_AVERAGE_RATING or UPDATED_AVERAGE_RATING
        defaultProductShouldBeFound("averageRating.in=" + DEFAULT_AVERAGE_RATING + "," + UPDATED_AVERAGE_RATING);

        // Get all the productList where averageRating equals to UPDATED_AVERAGE_RATING
        defaultProductShouldNotBeFound("averageRating.in=" + UPDATED_AVERAGE_RATING);
    }

    @Test
    @Transactional
    public void getAllProductsByAverageRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where averageRating is not null
        defaultProductShouldBeFound("averageRating.specified=true");

        // Get all the productList where averageRating is null
        defaultProductShouldNotBeFound("averageRating.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByRatingCountIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where ratingCount equals to DEFAULT_RATING_COUNT
        defaultProductShouldBeFound("ratingCount.equals=" + DEFAULT_RATING_COUNT);

        // Get all the productList where ratingCount equals to UPDATED_RATING_COUNT
        defaultProductShouldNotBeFound("ratingCount.equals=" + UPDATED_RATING_COUNT);
    }

    @Test
    @Transactional
    public void getAllProductsByRatingCountIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where ratingCount in DEFAULT_RATING_COUNT or UPDATED_RATING_COUNT
        defaultProductShouldBeFound("ratingCount.in=" + DEFAULT_RATING_COUNT + "," + UPDATED_RATING_COUNT);

        // Get all the productList where ratingCount equals to UPDATED_RATING_COUNT
        defaultProductShouldNotBeFound("ratingCount.in=" + UPDATED_RATING_COUNT);
    }

    @Test
    @Transactional
    public void getAllProductsByRatingCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where ratingCount is not null
        defaultProductShouldBeFound("ratingCount.specified=true");

        // Get all the productList where ratingCount is null
        defaultProductShouldNotBeFound("ratingCount.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByRatingCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where ratingCount greater than or equals to DEFAULT_RATING_COUNT
        defaultProductShouldBeFound("ratingCount.greaterOrEqualThan=" + DEFAULT_RATING_COUNT);

        // Get all the productList where ratingCount greater than or equals to UPDATED_RATING_COUNT
        defaultProductShouldNotBeFound("ratingCount.greaterOrEqualThan=" + UPDATED_RATING_COUNT);
    }

    @Test
    @Transactional
    public void getAllProductsByRatingCountIsLessThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where ratingCount less than or equals to DEFAULT_RATING_COUNT
        defaultProductShouldNotBeFound("ratingCount.lessThan=" + DEFAULT_RATING_COUNT);

        // Get all the productList where ratingCount less than or equals to UPDATED_RATING_COUNT
        defaultProductShouldBeFound("ratingCount.lessThan=" + UPDATED_RATING_COUNT);
    }


    @Test
    @Transactional
    public void getAllProductsByProductStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productStatus equals to DEFAULT_PRODUCT_STATUS
        defaultProductShouldBeFound("productStatus.equals=" + DEFAULT_PRODUCT_STATUS);

        // Get all the productList where productStatus equals to UPDATED_PRODUCT_STATUS
        defaultProductShouldNotBeFound("productStatus.equals=" + UPDATED_PRODUCT_STATUS);
    }

    @Test
    @Transactional
    public void getAllProductsByProductStatusIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productStatus in DEFAULT_PRODUCT_STATUS or UPDATED_PRODUCT_STATUS
        defaultProductShouldBeFound("productStatus.in=" + DEFAULT_PRODUCT_STATUS + "," + UPDATED_PRODUCT_STATUS);

        // Get all the productList where productStatus equals to UPDATED_PRODUCT_STATUS
        defaultProductShouldNotBeFound("productStatus.in=" + UPDATED_PRODUCT_STATUS);
    }

    @Test
    @Transactional
    public void getAllProductsByProductStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productStatus is not null
        defaultProductShouldBeFound("productStatus.specified=true");

        // Get all the productList where productStatus is null
        defaultProductShouldNotBeFound("productStatus.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByReviewIsEqualToSomething() throws Exception {
        // Initialize the database
        Review review = ReviewResourceIntTest.createEntity(em);
        em.persist(review);
        em.flush();
        product.addReview(review);
        productRepository.saveAndFlush(product);
        Long reviewId = review.getId();

        // Get all the productList where review equals to reviewId
        defaultProductShouldBeFound("reviewId.equals=" + reviewId);

        // Get all the productList where review equals to reviewId + 1
        defaultProductShouldNotBeFound("reviewId.equals=" + (reviewId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultProductShouldBeFound(String filter) throws Exception {
        restProductMockMvc.perform(get("/api/products?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].productID").value(hasItem(DEFAULT_PRODUCT_ID.toString())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].shortDescription").value(hasItem(DEFAULT_SHORT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].longDescription").value(hasItem(DEFAULT_LONG_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].productImageURL").value(hasItem(DEFAULT_PRODUCT_IMAGE_URL.toString())))
            .andExpect(jsonPath("$.[*].productURL").value(hasItem(DEFAULT_PRODUCT_URL.toString())))
            .andExpect(jsonPath("$.[*].supplierID").value(hasItem(DEFAULT_SUPPLIER_ID.toString())))
            .andExpect(jsonPath("$.[*].averageRating").value(hasItem(DEFAULT_AVERAGE_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].ratingCount").value(hasItem(DEFAULT_RATING_COUNT)))
            .andExpect(jsonPath("$.[*].productStatus").value(hasItem(DEFAULT_PRODUCT_STATUS.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultProductShouldNotBeFound(String filter) throws Exception {
        restProductMockMvc.perform(get("/api/products?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingProduct() throws Exception {
        // Get the product
        restProductMockMvc.perform(get("/api/products/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product
        Product updatedProduct = productRepository.findById(product.getId()).get();
        // Disconnect from session so that the updates on updatedProduct are not directly saved in db
        em.detach(updatedProduct);
        updatedProduct
            .productID(UPDATED_PRODUCT_ID)
            .uid(UPDATED_UID)
            .productName(UPDATED_PRODUCT_NAME)
            .price(UPDATED_PRICE)
            .shortDescription(UPDATED_SHORT_DESCRIPTION)
            .longDescription(UPDATED_LONG_DESCRIPTION)
            .productImageURL(UPDATED_PRODUCT_IMAGE_URL)
            .productURL(UPDATED_PRODUCT_URL)
            .supplierID(UPDATED_SUPPLIER_ID)
            .averageRating(UPDATED_AVERAGE_RATING)
            .ratingCount(UPDATED_RATING_COUNT)
            .productStatus(UPDATED_PRODUCT_STATUS);
        ProductDTO productDTO = productMapper.toDto(updatedProduct);

        restProductMockMvc.perform(put("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getProductID()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testProduct.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testProduct.getProductName()).isEqualTo(UPDATED_PRODUCT_NAME);
        assertThat(testProduct.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testProduct.getShortDescription()).isEqualTo(UPDATED_SHORT_DESCRIPTION);
        assertThat(testProduct.getLongDescription()).isEqualTo(UPDATED_LONG_DESCRIPTION);
        assertThat(testProduct.getProductImageURL()).isEqualTo(UPDATED_PRODUCT_IMAGE_URL);
        assertThat(testProduct.getProductURL()).isEqualTo(UPDATED_PRODUCT_URL);
        assertThat(testProduct.getSupplierID()).isEqualTo(UPDATED_SUPPLIER_ID);
        assertThat(testProduct.getAverageRating()).isEqualTo(UPDATED_AVERAGE_RATING);
        assertThat(testProduct.getRatingCount()).isEqualTo(UPDATED_RATING_COUNT);
        assertThat(testProduct.getProductStatus()).isEqualTo(UPDATED_PRODUCT_STATUS);

        // Validate the Product in Elasticsearch
        verify(mockProductSearchRepository, times(1)).save(testProduct);
    }

    @Test
    @Transactional
    public void updateNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restProductMockMvc.perform(put("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Product in Elasticsearch
        verify(mockProductSearchRepository, times(0)).save(product);
    }

    @Test
    @Transactional
    public void deleteProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeDelete = productRepository.findAll().size();

        // Get the product
        restProductMockMvc.perform(delete("/api/products/{id}", product.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Product in Elasticsearch
        verify(mockProductSearchRepository, times(1)).deleteById(product.getId());
    }

    @Test
    @Transactional
    public void searchProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);
        when(mockProductSearchRepository.search(queryStringQuery("id:" + product.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(product), PageRequest.of(0, 1), 1));
        // Search the product
        restProductMockMvc.perform(get("/api/_search/products?query=id:" + product.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].productID").value(hasItem(DEFAULT_PRODUCT_ID.toString())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].shortDescription").value(hasItem(DEFAULT_SHORT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].longDescription").value(hasItem(DEFAULT_LONG_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].productImageURL").value(hasItem(DEFAULT_PRODUCT_IMAGE_URL.toString())))
            .andExpect(jsonPath("$.[*].productURL").value(hasItem(DEFAULT_PRODUCT_URL.toString())))
            .andExpect(jsonPath("$.[*].supplierID").value(hasItem(DEFAULT_SUPPLIER_ID.toString())))
            .andExpect(jsonPath("$.[*].averageRating").value(hasItem(DEFAULT_AVERAGE_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].ratingCount").value(hasItem(DEFAULT_RATING_COUNT)))
            .andExpect(jsonPath("$.[*].productStatus").value(hasItem(DEFAULT_PRODUCT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Product.class);
        Product product1 = new Product();
        product1.setId(1L);
        Product product2 = new Product();
        product2.setId(product1.getId());
        assertThat(product1).isEqualTo(product2);
        product2.setId(2L);
        assertThat(product1).isNotEqualTo(product2);
        product1.setId(null);
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductDTO.class);
        ProductDTO productDTO1 = new ProductDTO();
        productDTO1.setId(1L);
        ProductDTO productDTO2 = new ProductDTO();
        assertThat(productDTO1).isNotEqualTo(productDTO2);
        productDTO2.setId(productDTO1.getId());
        assertThat(productDTO1).isEqualTo(productDTO2);
        productDTO2.setId(2L);
        assertThat(productDTO1).isNotEqualTo(productDTO2);
        productDTO1.setId(null);
        assertThat(productDTO1).isNotEqualTo(productDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(productMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(productMapper.fromId(null)).isNull();
    }
}
