package com.insightsystems.ratinginsight.repository.search;

import com.insightsystems.ratinginsight.domain.Reviewer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Reviewer entity.
 */
public interface ReviewerSearchRepository extends ElasticsearchRepository<Reviewer, Long> {
}
