import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ProductRatingInsight } from 'app/shared/model/product-rating-insight.model';
import { ProductRatingInsightService } from './product-rating-insight.service';
import { ProductRatingInsightComponent } from './product-rating-insight.component';
import { ProductRatingInsightDetailComponent } from './product-rating-insight-detail.component';
import { ProductRatingInsightUpdateComponent } from './product-rating-insight-update.component';
import { ProductRatingInsightDeletePopupComponent } from './product-rating-insight-delete-dialog.component';
import { IProductRatingInsight } from 'app/shared/model/product-rating-insight.model';

@Injectable({ providedIn: 'root' })
export class ProductRatingInsightResolve implements Resolve<IProductRatingInsight> {
    constructor(private service: ProductRatingInsightService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((product: HttpResponse<ProductRatingInsight>) => product.body));
        }
        return of(new ProductRatingInsight());
    }
}

export const productRoute: Routes = [
    {
        path: 'product-rating-insight',
        component: ProductRatingInsightComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ratingSystemApp.product.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'product-rating-insight/:id/view',
        component: ProductRatingInsightDetailComponent,
        resolve: {
            product: ProductRatingInsightResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ratingSystemApp.product.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'product-rating-insight/new',
        component: ProductRatingInsightUpdateComponent,
        resolve: {
            product: ProductRatingInsightResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ratingSystemApp.product.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'product-rating-insight/:id/edit',
        component: ProductRatingInsightUpdateComponent,
        resolve: {
            product: ProductRatingInsightResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ratingSystemApp.product.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const productPopupRoute: Routes = [
    {
        path: 'product-rating-insight/:id/delete',
        component: ProductRatingInsightDeletePopupComponent,
        resolve: {
            product: ProductRatingInsightResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ratingSystemApp.product.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
