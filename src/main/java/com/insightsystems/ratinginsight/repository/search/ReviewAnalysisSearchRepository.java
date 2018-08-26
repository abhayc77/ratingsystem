package com.insightsystems.ratinginsight.repository.search;

import com.insightsystems.ratinginsight.domain.ReviewAnalysis;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ReviewAnalysis entity.
 */
public interface ReviewAnalysisSearchRepository extends ElasticsearchRepository<ReviewAnalysis, Long> {
}
