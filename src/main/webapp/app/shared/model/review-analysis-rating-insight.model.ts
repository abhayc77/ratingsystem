import { Moment } from 'moment';

export const enum ReviewSentiment {
    POSITIVE = 'POSITIVE',
    NEUTRAL = 'NEUTRAL',
    NEGATIVE = 'NEGATIVE'
}

export const enum ReviewInsight {
    UNKKOWN = 'UNKKOWN',
    GENUINE = 'GENUINE',
    FAKE = 'FAKE'
}

export interface IReviewAnalysisRatingInsight {
    id?: number;
    uid?: string;
    sentiment?: ReviewSentiment;
    sentimentValue?: number;
    reviewAnalysisDateTime?: Moment;
    insight?: ReviewInsight;
    reviewInsightData?: string;
}

export class ReviewAnalysisRatingInsight implements IReviewAnalysisRatingInsight {
    constructor(
        public id?: number,
        public uid?: string,
        public sentiment?: ReviewSentiment,
        public sentimentValue?: number,
        public reviewAnalysisDateTime?: Moment,
        public insight?: ReviewInsight,
        public reviewInsightData?: string
    ) {}
}
