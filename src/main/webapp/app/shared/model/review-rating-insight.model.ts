import { Moment } from 'moment';
import { IReviewerRatingInsight } from 'app/shared/model//reviewer-rating-insight.model';
import { IProductRatingInsight } from 'app/shared/model//product-rating-insight.model';

export const enum Language {
    FRENCH = 'FRENCH',
    ENGLISH = 'ENGLISH',
    SPANISH = 'SPANISH'
}

export const enum ReviewStatus {
    UNKNOWN = 'UNKNOWN',
    NEW_REVIEW = 'NEW_REVIEW',
    PROCESSED_REVIEW = 'PROCESSED_REVIEW'
}

export interface IReviewRatingInsight {
    id?: number;
    productID?: string;
    uid?: string;
    productURL?: string;
    title?: string;
    reviewContent?: string;
    language?: Language;
    reviewDateTime?: Moment;
    rating?: number;
    fullRating?: number;
    reviewStatus?: ReviewStatus;
    helpfulVotes?: number;
    totalVotes?: number;
    verifiedPurchase?: boolean;
    realName?: string;
    reviewAnalysisId?: number;
    reviewers?: IReviewerRatingInsight[];
    products?: IProductRatingInsight[];
}

export class ReviewRatingInsight implements IReviewRatingInsight {
    constructor(
        public id?: number,
        public productID?: string,
        public uid?: string,
        public productURL?: string,
        public title?: string,
        public reviewContent?: string,
        public language?: Language,
        public reviewDateTime?: Moment,
        public rating?: number,
        public fullRating?: number,
        public reviewStatus?: ReviewStatus,
        public helpfulVotes?: number,
        public totalVotes?: number,
        public verifiedPurchase?: boolean,
        public realName?: string,
        public reviewAnalysisId?: number,
        public reviewers?: IReviewerRatingInsight[],
        public products?: IProductRatingInsight[]
    ) {
        this.verifiedPurchase = this.verifiedPurchase || false;
    }
}
