import { IReviewRatingInsight } from 'app/shared/model//review-rating-insight.model';

export interface IReviewerRatingInsight {
    id?: number;
    reviewerID?: string;
    uid?: string;
    username?: string;
    firstName?: string;
    lastName?: string;
    email?: string;
    phoneNumber?: string;
    streetAddress?: string;
    postalCode?: string;
    city?: string;
    stateProvince?: string;
    profileId?: number;
    reviews?: IReviewRatingInsight[];
}

export class ReviewerRatingInsight implements IReviewerRatingInsight {
    constructor(
        public id?: number,
        public reviewerID?: string,
        public uid?: string,
        public username?: string,
        public firstName?: string,
        public lastName?: string,
        public email?: string,
        public phoneNumber?: string,
        public streetAddress?: string,
        public postalCode?: string,
        public city?: string,
        public stateProvince?: string,
        public profileId?: number,
        public reviews?: IReviewRatingInsight[]
    ) {}
}
