import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IReviewRatingInsight } from 'app/shared/model/review-rating-insight.model';

type EntityResponseType = HttpResponse<IReviewRatingInsight>;
type EntityArrayResponseType = HttpResponse<IReviewRatingInsight[]>;

@Injectable({ providedIn: 'root' })
export class ReviewRatingInsightService {
    private resourceUrl = SERVER_API_URL + 'api/reviews';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/reviews';

    constructor(private http: HttpClient) {}

    create(review: IReviewRatingInsight): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(review);
        return this.http
            .post<IReviewRatingInsight>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(review: IReviewRatingInsight): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(review);
        return this.http
            .put<IReviewRatingInsight>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IReviewRatingInsight>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IReviewRatingInsight[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IReviewRatingInsight[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    private convertDateFromClient(review: IReviewRatingInsight): IReviewRatingInsight {
        const copy: IReviewRatingInsight = Object.assign({}, review, {
            reviewDateTime: review.reviewDateTime != null && review.reviewDateTime.isValid() ? review.reviewDateTime.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.reviewDateTime = res.body.reviewDateTime != null ? moment(res.body.reviewDateTime) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((review: IReviewRatingInsight) => {
            review.reviewDateTime = review.reviewDateTime != null ? moment(review.reviewDateTime) : null;
        });
        return res;
    }
}
