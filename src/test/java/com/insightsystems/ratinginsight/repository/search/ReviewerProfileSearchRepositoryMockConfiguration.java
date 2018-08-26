package com.insightsystems.ratinginsight.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of ReviewerProfileSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ReviewerProfileSearchRepositoryMockConfiguration {

    @MockBean
    private ReviewerProfileSearchRepository mockReviewerProfileSearchRepository;

}
