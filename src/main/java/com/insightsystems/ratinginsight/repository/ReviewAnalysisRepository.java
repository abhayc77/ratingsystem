package com.insightsystems.ratinginsight.repository;

import com.insightsystems.ratinginsight.domain.ReviewAnalysis;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ReviewAnalysis entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReviewAnalysisRepository extends JpaRepository<ReviewAnalysis, Long>, JpaSpecificationExecutor<ReviewAnalysis> {

}
