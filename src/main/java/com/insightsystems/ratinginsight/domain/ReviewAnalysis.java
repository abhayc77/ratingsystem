package com.insightsystems.ratinginsight.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import com.insightsystems.ratinginsight.domain.enumeration.ReviewSentiment;

import com.insightsystems.ratinginsight.domain.enumeration.ReviewInsight;

/**
 * A ReviewAnalysis.
 */
@Entity
@Table(name = "review_analysis")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "reviewanalysis")
public class ReviewAnalysis implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "jhi_uid")
    private String uid;

    @Enumerated(EnumType.STRING)
    @Column(name = "sentiment")
    private ReviewSentiment sentiment;

    @Column(name = "sentiment_value")
    private Float sentimentValue;

    @Column(name = "review_analysis_date_time")
    private ZonedDateTime reviewAnalysisDateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "insight")
    private ReviewInsight insight;

    @Column(name = "review_insight_data")
    private String reviewInsightData;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public ReviewAnalysis uid(String uid) {
        this.uid = uid;
        return this;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public ReviewSentiment getSentiment() {
        return sentiment;
    }

    public ReviewAnalysis sentiment(ReviewSentiment sentiment) {
        this.sentiment = sentiment;
        return this;
    }

    public void setSentiment(ReviewSentiment sentiment) {
        this.sentiment = sentiment;
    }

    public Float getSentimentValue() {
        return sentimentValue;
    }

    public ReviewAnalysis sentimentValue(Float sentimentValue) {
        this.sentimentValue = sentimentValue;
        return this;
    }

    public void setSentimentValue(Float sentimentValue) {
        this.sentimentValue = sentimentValue;
    }

    public ZonedDateTime getReviewAnalysisDateTime() {
        return reviewAnalysisDateTime;
    }

    public ReviewAnalysis reviewAnalysisDateTime(ZonedDateTime reviewAnalysisDateTime) {
        this.reviewAnalysisDateTime = reviewAnalysisDateTime;
        return this;
    }

    public void setReviewAnalysisDateTime(ZonedDateTime reviewAnalysisDateTime) {
        this.reviewAnalysisDateTime = reviewAnalysisDateTime;
    }

    public ReviewInsight getInsight() {
        return insight;
    }

    public ReviewAnalysis insight(ReviewInsight insight) {
        this.insight = insight;
        return this;
    }

    public void setInsight(ReviewInsight insight) {
        this.insight = insight;
    }

    public String getReviewInsightData() {
        return reviewInsightData;
    }

    public ReviewAnalysis reviewInsightData(String reviewInsightData) {
        this.reviewInsightData = reviewInsightData;
        return this;
    }

    public void setReviewInsightData(String reviewInsightData) {
        this.reviewInsightData = reviewInsightData;
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
        ReviewAnalysis reviewAnalysis = (ReviewAnalysis) o;
        if (reviewAnalysis.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reviewAnalysis.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReviewAnalysis{" +
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
