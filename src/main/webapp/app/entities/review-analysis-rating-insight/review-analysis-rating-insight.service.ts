import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IReviewAnalysisRatingInsight } from 'app/shared/model/review-analysis-rating-insight.model';

type EntityResponseType = HttpResponse<IReviewAnalysisRatingInsight>;
type EntityArrayResponseType = HttpResponse<IReviewAnalysisRatingInsight[]>;

@Injectable({ providedIn: 'root' })
export class ReviewAnalysisRatingInsightService {
    private resourceUrl = SERVER_API_URL + 'api/review-analyses';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/review-analyses';

    constructor(private http: HttpClient) {}

    create(reviewAnalysis: IReviewAnalysisRatingInsight): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(reviewAnalysis);
        return this.http
            .post<IReviewAnalysisRatingInsight>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(reviewAnalysis: IReviewAnalysisRatingInsight): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(reviewAnalysis);
        return this.http
            .put<IReviewAnalysisRatingInsight>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IReviewAnalysisRatingInsight>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IReviewAnalysisRatingInsight[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IReviewAnalysisRatingInsight[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    private convertDateFromClient(reviewAnalysis: IReviewAnalysisRatingInsight): IReviewAnalysisRatingInsight {
        const copy: IReviewAnalysisRatingInsight = Object.assign({}, reviewAnalysis, {
            reviewAnalysisDateTime:
                reviewAnalysis.reviewAnalysisDateTime != null && reviewAnalysis.reviewAnalysisDateTime.isValid()
                    ? reviewAnalysis.reviewAnalysisDateTime.toJSON()
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.reviewAnalysisDateTime = res.body.reviewAnalysisDateTime != null ? moment(res.body.reviewAnalysisDateTime) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((reviewAnalysis: IReviewAnalysisRatingInsight) => {
            reviewAnalysis.reviewAnalysisDateTime =
                reviewAnalysis.reviewAnalysisDateTime != null ? moment(reviewAnalysis.reviewAnalysisDateTime) : null;
        });
        return res;
    }
}
