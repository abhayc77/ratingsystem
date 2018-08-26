package com.insightsystems.ratinginsight.service.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.Objects;
import com.insightsystems.ratinginsight.domain.enumeration.Language;
import com.insightsystems.ratinginsight.domain.enumeration.ReviewStatus;

/**
 * A DTO for the Review entity.
 */
public class ReviewDTO implements Serializable {

    private Long id;

    private String productID;

    private String uid;

    private String productURL;

    private String title;

    private String reviewContent;

    private Language language;

    private ZonedDateTime reviewDateTime;

    private Integer rating;

    private Integer fullRating;

    private ReviewStatus reviewStatus;

    private Integer helpfulVotes;

    private Integer totalVotes;

    private Boolean verifiedPurchase;

    private String realName;

    private Long reviewAnalysisId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProductURL() {
        return productURL;
    }

    public void setProductURL(String productURL) {
        this.productURL = productURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public ZonedDateTime getReviewDateTime() {
        return reviewDateTime;
    }

    public void setReviewDateTime(ZonedDateTime reviewDateTime) {
        this.reviewDateTime = reviewDateTime;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getFullRating() {
        return fullRating;
    }

    public void setFullRating(Integer fullRating) {
        this.fullRating = fullRating;
    }

    public ReviewStatus getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(ReviewStatus reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public Integer getHelpfulVotes() {
        return helpfulVotes;
    }

    public void setHelpfulVotes(Integer helpfulVotes) {
        this.helpfulVotes = helpfulVotes;
    }

    public Integer getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(Integer totalVotes) {
        this.totalVotes = totalVotes;
    }

    public Boolean isVerifiedPurchase() {
        return verifiedPurchase;
    }

    public void setVerifiedPurchase(Boolean verifiedPurchase) {
        this.verifiedPurchase = verifiedPurchase;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Long getReviewAnalysisId() {
        return reviewAnalysisId;
    }

    public void setReviewAnalysisId(Long reviewAnalysisId) {
        this.reviewAnalysisId = reviewAnalysisId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ReviewDTO reviewDTO = (ReviewDTO) o;
        if (reviewDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reviewDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReviewDTO{" +
            "id=" + getId() +
            ", productID='" + getProductID() + "'" +
            ", uid='" + getUid() + "'" +
            ", productURL='" + getProductURL() + "'" +
            ", title='" + getTitle() + "'" +
            ", reviewContent='" + getReviewContent() + "'" +
            ", language='" + getLanguage() + "'" +
            ", reviewDateTime='" + getReviewDateTime() + "'" +
            ", rating=" + getRating() +
            ", fullRating=" + getFullRating() +
            ", reviewStatus='" + getReviewStatus() + "'" +
            ", helpfulVotes=" + getHelpfulVotes() +
            ", totalVotes=" + getTotalVotes() +
            ", verifiedPurchase='" + isVerifiedPurchase() + "'" +
            ", realName='" + getRealName() + "'" +
            ", reviewAnalysis=" + getReviewAnalysisId() +
            "}";
    }
}
