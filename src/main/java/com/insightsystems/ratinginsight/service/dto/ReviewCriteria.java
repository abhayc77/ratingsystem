package com.insightsystems.ratinginsight.service.dto;

import java.io.Serializable;
import com.insightsystems.ratinginsight.domain.enumeration.Language;
import com.insightsystems.ratinginsight.domain.enumeration.ReviewStatus;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;



import io.github.jhipster.service.filter.ZonedDateTimeFilter;


/**
 * Criteria class for the Review entity. This class is used in ReviewResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /reviews?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ReviewCriteria implements Serializable {
    /**
     * Class for filtering Language
     */
    public static class LanguageFilter extends Filter<Language> {
    }

    /**
     * Class for filtering ReviewStatus
     */
    public static class ReviewStatusFilter extends Filter<ReviewStatus> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter productID;

    private StringFilter uid;

    private StringFilter productURL;

    private StringFilter title;

    private StringFilter reviewContent;

    private LanguageFilter language;

    private ZonedDateTimeFilter reviewDateTime;

    private IntegerFilter rating;

    private IntegerFilter fullRating;

    private ReviewStatusFilter reviewStatus;

    private IntegerFilter helpfulVotes;

    private IntegerFilter totalVotes;

    private BooleanFilter verifiedPurchase;

    private StringFilter realName;

    private LongFilter reviewAnalysisId;

    private LongFilter reviewerId;

    private LongFilter productId;

    public ReviewCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getProductID() {
        return productID;
    }

    public void setProductID(StringFilter productID) {
        this.productID = productID;
    }

    public StringFilter getUid() {
        return uid;
    }

    public void setUid(StringFilter uid) {
        this.uid = uid;
    }

    public StringFilter getProductURL() {
        return productURL;
    }

    public void setProductURL(StringFilter productURL) {
        this.productURL = productURL;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(StringFilter reviewContent) {
        this.reviewContent = reviewContent;
    }

    public LanguageFilter getLanguage() {
        return language;
    }

    public void setLanguage(LanguageFilter language) {
        this.language = language;
    }

    public ZonedDateTimeFilter getReviewDateTime() {
        return reviewDateTime;
    }

    public void setReviewDateTime(ZonedDateTimeFilter reviewDateTime) {
        this.reviewDateTime = reviewDateTime;
    }

    public IntegerFilter getRating() {
        return rating;
    }

    public void setRating(IntegerFilter rating) {
        this.rating = rating;
    }

    public IntegerFilter getFullRating() {
        return fullRating;
    }

    public void setFullRating(IntegerFilter fullRating) {
        this.fullRating = fullRating;
    }

    public ReviewStatusFilter getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(ReviewStatusFilter reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public IntegerFilter getHelpfulVotes() {
        return helpfulVotes;
    }

    public void setHelpfulVotes(IntegerFilter helpfulVotes) {
        this.helpfulVotes = helpfulVotes;
    }

    public IntegerFilter getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(IntegerFilter totalVotes) {
        this.totalVotes = totalVotes;
    }

    public BooleanFilter getVerifiedPurchase() {
        return verifiedPurchase;
    }

    public void setVerifiedPurchase(BooleanFilter verifiedPurchase) {
        this.verifiedPurchase = verifiedPurchase;
    }

    public StringFilter getRealName() {
        return realName;
    }

    public void setRealName(StringFilter realName) {
        this.realName = realName;
    }

    public LongFilter getReviewAnalysisId() {
        return reviewAnalysisId;
    }

    public void setReviewAnalysisId(LongFilter reviewAnalysisId) {
        this.reviewAnalysisId = reviewAnalysisId;
    }

    public LongFilter getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(LongFilter reviewerId) {
        this.reviewerId = reviewerId;
    }

    public LongFilter getProductId() {
        return productId;
    }

    public void setProductId(LongFilter productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "ReviewCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (productID != null ? "productID=" + productID + ", " : "") +
                (uid != null ? "uid=" + uid + ", " : "") +
                (productURL != null ? "productURL=" + productURL + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (reviewContent != null ? "reviewContent=" + reviewContent + ", " : "") +
                (language != null ? "language=" + language + ", " : "") +
                (reviewDateTime != null ? "reviewDateTime=" + reviewDateTime + ", " : "") +
                (rating != null ? "rating=" + rating + ", " : "") +
                (fullRating != null ? "fullRating=" + fullRating + ", " : "") +
                (reviewStatus != null ? "reviewStatus=" + reviewStatus + ", " : "") +
                (helpfulVotes != null ? "helpfulVotes=" + helpfulVotes + ", " : "") +
                (totalVotes != null ? "totalVotes=" + totalVotes + ", " : "") +
                (verifiedPurchase != null ? "verifiedPurchase=" + verifiedPurchase + ", " : "") +
                (realName != null ? "realName=" + realName + ", " : "") +
                (reviewAnalysisId != null ? "reviewAnalysisId=" + reviewAnalysisId + ", " : "") +
                (reviewerId != null ? "reviewerId=" + reviewerId + ", " : "") +
                (productId != null ? "productId=" + productId + ", " : "") +
            "}";
    }

}
