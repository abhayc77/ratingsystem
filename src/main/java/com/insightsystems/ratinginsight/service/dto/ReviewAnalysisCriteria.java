package com.insightsystems.ratinginsight.service.dto;

import java.io.Serializable;
import com.insightsystems.ratinginsight.domain.enumeration.ReviewSentiment;
import com.insightsystems.ratinginsight.domain.enumeration.ReviewInsight;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;



import io.github.jhipster.service.filter.ZonedDateTimeFilter;


/**
 * Criteria class for the ReviewAnalysis entity. This class is used in ReviewAnalysisResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /review-analyses?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ReviewAnalysisCriteria implements Serializable {
    /**
     * Class for filtering ReviewSentiment
     */
    public static class ReviewSentimentFilter extends Filter<ReviewSentiment> {
    }

    /**
     * Class for filtering ReviewInsight
     */
    public static class ReviewInsightFilter extends Filter<ReviewInsight> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter uid;

    private ReviewSentimentFilter sentiment;

    private FloatFilter sentimentValue;

    private ZonedDateTimeFilter reviewAnalysisDateTime;

    private ReviewInsightFilter insight;

    private StringFilter reviewInsightData;

    public ReviewAnalysisCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getUid() {
        return uid;
    }

    public void setUid(StringFilter uid) {
        this.uid = uid;
    }

    public ReviewSentimentFilter getSentiment() {
        return sentiment;
    }

    public void setSentiment(ReviewSentimentFilter sentiment) {
        this.sentiment = sentiment;
    }

    public FloatFilter getSentimentValue() {
        return sentimentValue;
    }

    public void setSentimentValue(FloatFilter sentimentValue) {
        this.sentimentValue = sentimentValue;
    }

    public ZonedDateTimeFilter getReviewAnalysisDateTime() {
        return reviewAnalysisDateTime;
    }

    public void setReviewAnalysisDateTime(ZonedDateTimeFilter reviewAnalysisDateTime) {
        this.reviewAnalysisDateTime = reviewAnalysisDateTime;
    }

    public ReviewInsightFilter getInsight() {
        return insight;
    }

    public void setInsight(ReviewInsightFilter insight) {
        this.insight = insight;
    }

    public StringFilter getReviewInsightData() {
        return reviewInsightData;
    }

    public void setReviewInsightData(StringFilter reviewInsightData) {
        this.reviewInsightData = reviewInsightData;
    }

    @Override
    public String toString() {
        return "ReviewAnalysisCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (uid != null ? "uid=" + uid + ", " : "") +
                (sentiment != null ? "sentiment=" + sentiment + ", " : "") +
                (sentimentValue != null ? "sentimentValue=" + sentimentValue + ", " : "") +
                (reviewAnalysisDateTime != null ? "reviewAnalysisDateTime=" + reviewAnalysisDateTime + ", " : "") +
                (insight != null ? "insight=" + insight + ", " : "") +
                (reviewInsightData != null ? "reviewInsightData=" + reviewInsightData + ", " : "") +
            "}";
    }

}
