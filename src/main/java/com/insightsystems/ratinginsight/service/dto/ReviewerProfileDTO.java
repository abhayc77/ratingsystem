package com.insightsystems.ratinginsight.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the ReviewerProfile entity.
 */
public class ReviewerProfileDTO implements Serializable {

    private Long id;

    private Long totalReviews;

    private Float reviewerRanking;

    private Long totalHelpfulVotes;

    private String recentRating;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(Long totalReviews) {
        this.totalReviews = totalReviews;
    }

    public Float getReviewerRanking() {
        return reviewerRanking;
    }

    public void setReviewerRanking(Float reviewerRanking) {
        this.reviewerRanking = reviewerRanking;
    }

    public Long getTotalHelpfulVotes() {
        return totalHelpfulVotes;
    }

    public void setTotalHelpfulVotes(Long totalHelpfulVotes) {
        this.totalHelpfulVotes = totalHelpfulVotes;
    }

    public String getRecentRating() {
        return recentRating;
    }

    public void setRecentRating(String recentRating) {
        this.recentRating = recentRating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ReviewerProfileDTO reviewerProfileDTO = (ReviewerProfileDTO) o;
        if (reviewerProfileDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reviewerProfileDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReviewerProfileDTO{" +
            "id=" + getId() +
            ", totalReviews=" + getTotalReviews() +
            ", reviewerRanking=" + getReviewerRanking() +
            ", totalHelpfulVotes=" + getTotalHelpfulVotes() +
            ", recentRating='" + getRecentRating() + "'" +
            "}";
    }
}
