import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IReviewerRatingInsight } from 'app/shared/model/reviewer-rating-insight.model';

type EntityResponseType = HttpResponse<IReviewerRatingInsight>;
type EntityArrayResponseType = HttpResponse<IReviewerRatingInsight[]>;

@Injectable({ providedIn: 'root' })
export class ReviewerRatingInsightService {
    private resourceUrl = SERVER_API_URL + 'api/reviewers';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/reviewers';

    constructor(private http: HttpClient) {}

    create(reviewer: IReviewerRatingInsight): Observable<EntityResponseType> {
        return this.http.post<IReviewerRatingInsight>(this.resourceUrl, reviewer, { observe: 'response' });
    }

    update(reviewer: IReviewerRatingInsight): Observable<EntityResponseType> {
        return this.http.put<IReviewerRatingInsight>(this.resourceUrl, reviewer, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IReviewerRatingInsight>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IReviewerRatingInsight[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IReviewerRatingInsight[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
