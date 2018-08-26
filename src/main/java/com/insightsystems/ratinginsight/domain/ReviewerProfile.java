package com.insightsystems.ratinginsight.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ReviewerProfile.
 */
@Entity
@Table(name = "reviewer_profile")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "reviewerprofile")
public class ReviewerProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "total_reviews")
    private Long totalReviews;

    @Column(name = "reviewer_ranking")
    private Float reviewerRanking;

    @Column(name = "total_helpful_votes")
    private Long totalHelpfulVotes;

    @Column(name = "recent_rating")
    private String recentRating;

    @OneToOne(mappedBy = "profile")
    @JsonIgnore
    private Reviewer reviewer;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTotalReviews() {
        return totalReviews;
    }

    public ReviewerProfile totalReviews(Long totalReviews) {
        this.totalReviews = totalReviews;
        return this;
    }

    public void setTotalReviews(Long totalReviews) {
        this.totalReviews = totalReviews;
    }

    public Float getReviewerRanking() {
        return reviewerRanking;
    }

    public ReviewerProfile reviewerRanking(Float reviewerRanking) {
        this.reviewerRanking = reviewerRanking;
        return this;
    }

    public void setReviewerRanking(Float reviewerRanking) {
        this.reviewerRanking = reviewerRanking;
    }

    public Long getTotalHelpfulVotes() {
        return totalHelpfulVotes;
    }

    public ReviewerProfile totalHelpfulVotes(Long totalHelpfulVotes) {
        this.totalHelpfulVotes = totalHelpfulVotes;
        return this;
    }

    public void setTotalHelpfulVotes(Long totalHelpfulVotes) {
        this.totalHelpfulVotes = totalHelpfulVotes;
    }

    public String getRecentRating() {
        return recentRating;
    }

    public ReviewerProfile recentRating(String recentRating) {
        this.recentRating = recentRating;
        return this;
    }

    public void setRecentRating(String recentRating) {
        this.recentRating = recentRating;
    }

    public Reviewer getReviewer() {
        return reviewer;
    }

    public ReviewerProfile reviewer(Reviewer reviewer) {
        this.reviewer = reviewer;
        return this;
    }

    public void setReviewer(Reviewer reviewer) {
        this.reviewer = reviewer;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReviewerProfile reviewerProfile = (ReviewerProfile) o;
        if (reviewerProfile.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reviewerProfile.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReviewerProfile{" +
            "id=" + getId() +
            ", totalReviews=" + getTotalReviews() +
            ", reviewerRanking=" + getReviewerRanking() +
            ", totalHelpfulVotes=" + getTotalHelpfulVotes() +
            ", recentRating='" + getRecentRating() + "'" +
            "}";
    }
}
