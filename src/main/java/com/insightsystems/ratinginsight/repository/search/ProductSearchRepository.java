package com.insightsystems.ratinginsight.repository.search;

import com.insightsystems.ratinginsight.domain.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Product entity.
 */
public interface ProductSearchRepository extends ElasticsearchRepository<Product, Long> {
}
