import { IReviewRatingInsight } from 'app/shared/model//review-rating-insight.model';

export const enum ProductStatus {
    UNKNOWN = 'UNKNOWN',
    ACTIVE = 'ACTIVE',
    DISCONTINUED = 'DISCONTINUED'
}

export interface IProductRatingInsight {
    id?: number;
    productID?: string;
    uid?: string;
    productName?: string;
    price?: number;
    shortDescription?: string;
    longDescription?: string;
    productImageURL?: string;
    productURL?: string;
    supplierID?: string;
    averageRating?: number;
    ratingCount?: number;
    productStatus?: ProductStatus;
    reviews?: IReviewRatingInsight[];
}

export class ProductRatingInsight implements IProductRatingInsight {
    constructor(
        public id?: number,
        public productID?: string,
        public uid?: string,
        public productName?: string,
        public price?: number,
        public shortDescription?: string,
        public longDescription?: string,
        public productImageURL?: string,
        public productURL?: string,
        public supplierID?: string,
        public averageRating?: number,
        public ratingCount?: number,
        public productStatus?: ProductStatus,
        public reviews?: IReviewRatingInsight[]
    ) {}
}
