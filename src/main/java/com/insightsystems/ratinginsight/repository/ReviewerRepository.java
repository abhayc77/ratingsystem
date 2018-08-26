package com.insightsystems.ratinginsight.repository;

import com.insightsystems.ratinginsight.domain.Reviewer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Reviewer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReviewerRepository extends JpaRepository<Reviewer, Long>, JpaSpecificationExecutor<Reviewer> {

    @Query(value = "select distinct reviewer from Reviewer reviewer left join fetch reviewer.reviews",
        countQuery = "select count(distinct reviewer) from Reviewer reviewer")
    Page<Reviewer> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct reviewer from Reviewer reviewer left join fetch reviewer.reviews")
    List<Reviewer> findAllWithEagerRelationships();

    @Query("select reviewer from Reviewer reviewer left join fetch reviewer.reviews where reviewer.id =:id")
    Optional<Reviewer> findOneWithEagerRelationships(@Param("id") Long id);

}
