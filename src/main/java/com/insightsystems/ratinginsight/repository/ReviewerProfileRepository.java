package com.insightsystems.ratinginsight.repository;

import com.insightsystems.ratinginsight.domain.ReviewerProfile;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ReviewerProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReviewerProfileRepository extends JpaRepository<ReviewerProfile, Long>, JpaSpecificationExecutor<ReviewerProfile> {

}
