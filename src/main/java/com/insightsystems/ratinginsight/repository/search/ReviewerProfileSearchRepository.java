package com.insightsystems.ratinginsight.repository.search;

import com.insightsystems.ratinginsight.domain.ReviewerProfile;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ReviewerProfile entity.
 */
public interface ReviewerProfileSearchRepository extends ElasticsearchRepository<ReviewerProfile, Long> {
}
