import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ReviewRatingInsight } from 'app/shared/model/review-rating-insight.model';
import { ReviewRatingInsightService } from './review-rating-insight.service';
import { ReviewRatingInsightComponent } from './review-rating-insight.component';
import { ReviewRatingInsightDetailComponent } from './review-rating-insight-detail.component';
import { ReviewRatingInsightUpdateComponent } from './review-rating-insight-update.component';
import { ReviewRatingInsightDeletePopupComponent } from './review-rating-insight-delete-dialog.component';
import { IReviewRatingInsight } from 'app/shared/model/review-rating-insight.model';

@Injectable({ providedIn: 'root' })
export class ReviewRatingInsightResolve implements Resolve<IReviewRatingInsight> {
    constructor(private service: ReviewRatingInsightService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((review: HttpResponse<ReviewRatingInsight>) => review.body));
        }
        return of(new ReviewRatingInsight());
    }
}

export const reviewRoute: Routes = [
    {
        path: 'review-rating-insight',
        component: ReviewRatingInsightComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ratingSystemApp.review.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'review-rating-insight/:id/view',
        component: ReviewRatingInsightDetailComponent,
        resolve: {
            review: ReviewRatingInsightResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ratingSystemApp.review.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'review-rating-insight/new',
        component: ReviewRatingInsightUpdateComponent,
        resolve: {
            review: ReviewRatingInsightResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ratingSystemApp.review.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'review-rating-insight/:id/edit',
        component: ReviewRatingInsightUpdateComponent,
        resolve: {
            review: ReviewRatingInsightResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ratingSystemApp.review.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const reviewPopupRoute: Routes = [
    {
        path: 'review-rating-insight/:id/delete',
        component: ReviewRatingInsightDeletePopupComponent,
        resolve: {
            review: ReviewRatingInsightResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ratingSystemApp.review.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
