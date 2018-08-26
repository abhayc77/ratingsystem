export interface IReviewerProfileRatingInsight {
    id?: number;
    totalReviews?: number;
    reviewerRanking?: number;
    totalHelpfulVotes?: number;
    recentRating?: string;
    reviewerId?: number;
}

export class ReviewerProfileRatingInsight implements IReviewerProfileRatingInsight {
    constructor(
        public id?: number,
        public totalReviews?: number,
        public reviewerRanking?: number,
        public totalHelpfulVotes?: number,
        public recentRating?: string,
        public reviewerId?: number
    ) {}
}
