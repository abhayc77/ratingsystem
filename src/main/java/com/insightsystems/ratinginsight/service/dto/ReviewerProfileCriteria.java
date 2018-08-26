package com.insightsystems.ratinginsight.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the ReviewerProfile entity. This class is used in ReviewerProfileResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /reviewer-profiles?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ReviewerProfileCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private LongFilter totalReviews;

    private FloatFilter reviewerRanking;

    private LongFilter totalHelpfulVotes;

    private StringFilter recentRating;

    private LongFilter reviewerId;

    public ReviewerProfileCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(LongFilter totalReviews) {
        this.totalReviews = totalReviews;
    }

    public FloatFilter getReviewerRanking() {
        return reviewerRanking;
    }

    public void setReviewerRanking(FloatFilter reviewerRanking) {
        this.reviewerRanking = reviewerRanking;
    }

    public LongFilter getTotalHelpfulVotes() {
        return totalHelpfulVotes;
    }

    public void setTotalHelpfulVotes(LongFilter totalHelpfulVotes) {
        this.totalHelpfulVotes = totalHelpfulVotes;
    }

    public StringFilter getRecentRating() {
        return recentRating;
    }

    public void setRecentRating(StringFilter recentRating) {
        this.recentRating = recentRating;
    }

    public LongFilter getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(LongFilter reviewerId) {
        this.reviewerId = reviewerId;
    }

    @Override
    public String toString() {
        return "ReviewerProfileCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (totalReviews != null ? "totalReviews=" + totalReviews + ", " : "") +
                (reviewerRanking != null ? "reviewerRanking=" + reviewerRanking + ", " : "") +
                (totalHelpfulVotes != null ? "totalHelpfulVotes=" + totalHelpfulVotes + ", " : "") +
                (recentRating != null ? "recentRating=" + recentRating + ", " : "") +
                (reviewerId != null ? "reviewerId=" + reviewerId + ", " : "") +
            "}";
    }

}
