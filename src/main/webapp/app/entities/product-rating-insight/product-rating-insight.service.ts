import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IProductRatingInsight } from 'app/shared/model/product-rating-insight.model';

type EntityResponseType = HttpResponse<IProductRatingInsight>;
type EntityArrayResponseType = HttpResponse<IProductRatingInsight[]>;

@Injectable({ providedIn: 'root' })
export class ProductRatingInsightService {
    private resourceUrl = SERVER_API_URL + 'api/products';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/products';

    constructor(private http: HttpClient) {}

    create(product: IProductRatingInsight): Observable<EntityResponseType> {
        return this.http.post<IProductRatingInsight>(this.resourceUrl, product, { observe: 'response' });
    }

    update(product: IProductRatingInsight): Observable<EntityResponseType> {
        return this.http.put<IProductRatingInsight>(this.resourceUrl, product, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IProductRatingInsight>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IProductRatingInsight[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IProductRatingInsight[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
