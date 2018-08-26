import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IReviewerProfileRatingInsight } from 'app/shared/model/reviewer-profile-rating-insight.model';

type EntityResponseType = HttpResponse<IReviewerProfileRatingInsight>;
type EntityArrayResponseType = HttpResponse<IReviewerProfileRatingInsight[]>;

@Injectable({ providedIn: 'root' })
export class ReviewerProfileRatingInsightService {
    private resourceUrl = SERVER_API_URL + 'api/reviewer-profiles';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/reviewer-profiles';

    constructor(private http: HttpClient) {}

    create(reviewerProfile: IReviewerProfileRatingInsight): Observable<EntityResponseType> {
        return this.http.post<IReviewerProfileRatingInsight>(this.resourceUrl, reviewerProfile, { observe: 'response' });
    }

    update(reviewerProfile: IReviewerProfileRatingInsight): Observable<EntityResponseType> {
        return this.http.put<IReviewerProfileRatingInsight>(this.resourceUrl, reviewerProfile, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IReviewerProfileRatingInsight>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IReviewerProfileRatingInsight[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IReviewerProfileRatingInsight[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
