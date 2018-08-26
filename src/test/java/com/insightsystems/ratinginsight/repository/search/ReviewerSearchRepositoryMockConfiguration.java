package com.insightsystems.ratinginsight.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of ReviewerSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ReviewerSearchRepositoryMockConfiguration {

    @MockBean
    private ReviewerSearchRepository mockReviewerSearchRepository;

}
