package com.insightsystems.ratinginsight.repository.search;

import com.insightsystems.ratinginsight.domain.Review;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Review entity.
 */
public interface ReviewSearchRepository extends ElasticsearchRepository<Review, Long> {
}
