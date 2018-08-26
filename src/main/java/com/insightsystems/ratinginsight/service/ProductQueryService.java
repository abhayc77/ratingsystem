package com.insightsystems.ratinginsight.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.insightsystems.ratinginsight.domain.Product;
import com.insightsystems.ratinginsight.domain.*; // for static metamodels
import com.insightsystems.ratinginsight.repository.ProductRepository;
import com.insightsystems.ratinginsight.repository.search.ProductSearchRepository;
import com.insightsystems.ratinginsight.service.dto.ProductCriteria;

import com.insightsystems.ratinginsight.service.dto.ProductDTO;
import com.insightsystems.ratinginsight.service.mapper.ProductMapper;

/**
 * Service for executing complex queries for Product entities in the database.
 * The main input is a {@link ProductCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductDTO} or a {@link Page} of {@link ProductDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductQueryService extends QueryService<Product> {

    private final Logger log = LoggerFactory.getLogger(ProductQueryService.class);

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    private final ProductSearchRepository productSearchRepository;

    public ProductQueryService(ProductRepository productRepository, ProductMapper productMapper, ProductSearchRepository productSearchRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.productSearchRepository = productSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ProductDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductDTO> findByCriteria(ProductCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Product> specification = createSpecification(criteria);
        return productMapper.toDto(productRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProductDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductDTO> findByCriteria(ProductCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Product> specification = createSpecification(criteria);
        return productRepository.findAll(specification, page)
            .map(productMapper::toDto);
    }

    /**
     * Function to convert ProductCriteria to a {@link Specification}
     */
    private Specification<Product> createSpecification(ProductCriteria criteria) {
        Specification<Product> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Product_.id));
            }
            if (criteria.getProductID() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProductID(), Product_.productID));
            }
            if (criteria.getUid() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUid(), Product_.uid));
            }
            if (criteria.getProductName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProductName(), Product_.productName));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), Product_.price));
            }
            if (criteria.getShortDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getShortDescription(), Product_.shortDescription));
            }
            if (criteria.getLongDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLongDescription(), Product_.longDescription));
            }
            if (criteria.getProductImageURL() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProductImageURL(), Product_.productImageURL));
            }
            if (criteria.getProductURL() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProductURL(), Product_.productURL));
            }
            if (criteria.getSupplierID() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSupplierID(), Product_.supplierID));
            }
            if (criteria.getAverageRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAverageRating(), Product_.averageRating));
            }
            if (criteria.getRatingCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRatingCount(), Product_.ratingCount));
            }
            if (criteria.getProductStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getProductStatus(), Product_.productStatus));
            }
            if (criteria.getReviewId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getReviewId(), Product_.reviews, Review_.id));
            }
        }
        return specification;
    }

}
