package com.insightsystems.ratinginsight.service.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.Objects;
import com.insightsystems.ratinginsight.domain.enumeration.ReviewSentiment;
import com.insightsystems.ratinginsight.domain.enumeration.ReviewInsight;

/**
 * A DTO for the ReviewAnalysis entity.
 */
public class ReviewAnalysisDTO implements Serializable {

    private Long id;

    private String uid;

    private ReviewSentiment sentiment;

    private Float sentimentValue;

    private ZonedDateTime reviewAnalysisDateTime;

    private ReviewInsight insight;

    private String reviewInsightData;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public ReviewSentiment getSentiment() {
        return sentiment;
    }

    public void setSentiment(ReviewSentiment sentiment) {
        this.sentiment = sentiment;
    }

    public Float getSentimentValue() {
        return sentimentValue;
    }

    public void setSentimentValue(Float sentimentValue) {
        this.sentimentValue = sentimentValue;
    }

    public ZonedDateTime getReviewAnalysisDateTime() {
        return reviewAnalysisDateTime;
    }

    public void setReviewAnalysisDateTime(ZonedDateTime reviewAnalysisDateTime) {
        this.reviewAnalysisDateTime = reviewAnalysisDateTime;
    }

    public ReviewInsight getInsight() {
        return insight;
    }

    public void setInsight(ReviewInsight insight) {
        this.insight = insight;
    }

    public String getReviewInsightData() {
        return reviewInsightData;
    }

    public void setReviewInsightData(String reviewInsightData) {
        this.reviewInsightData = reviewInsightData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ReviewAnalysisDTO reviewAnalysisDTO = (ReviewAnalysisDTO) o;
        if (reviewAnalysisDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reviewAnalysisDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReviewAnalysisDTO{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", sentiment='" + getSentiment() + "'" +
            ", sentimentValue=" + getSentimentValue() +
            ", reviewAnalysisDateTime='" + getReviewAnalysisDateTime() + "'" +
            ", insight='" + getInsight() + "'" +
            ", reviewInsightData='" + getReviewInsightData() + "'" +
            "}";
    }
}
