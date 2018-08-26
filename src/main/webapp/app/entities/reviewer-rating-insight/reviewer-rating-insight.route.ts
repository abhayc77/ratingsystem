import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ReviewerRatingInsight } from 'app/shared/model/reviewer-rating-insight.model';
import { ReviewerRatingInsightService } from './reviewer-rating-insight.service';
import { ReviewerRatingInsightComponent } from './reviewer-rating-insight.component';
import { ReviewerRatingInsightDetailComponent } from './reviewer-rating-insight-detail.component';
import { ReviewerRatingInsightUpdateComponent } from './reviewer-rating-insight-update.component';
import { ReviewerRatingInsightDeletePopupComponent } from './reviewer-rating-insight-delete-dialog.component';
import { IReviewerRatingInsight } from 'app/shared/model/reviewer-rating-insight.model';

@Injectable({ providedIn: 'root' })
export class ReviewerRatingInsightResolve implements Resolve<IReviewerRatingInsight> {
    constructor(private service: ReviewerRatingInsightService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((reviewer: HttpResponse<ReviewerRatingInsight>) => reviewer.body));
        }
        return of(new ReviewerRatingInsight());
    }
}

export const reviewerRoute: Routes = [
    {
        path: 'reviewer-rating-insight',
        component: ReviewerRatingInsightComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ratingSystemApp.reviewer.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'reviewer-rating-insight/:id/view',
        component: ReviewerRatingInsightDetailComponent,
        resolve: {
            reviewer: ReviewerRatingInsightResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ratingSystemApp.reviewer.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'reviewer-rating-insight/new',
        component: ReviewerRatingInsightUpdateComponent,
        resolve: {
            reviewer: ReviewerRatingInsightResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ratingSystemApp.reviewer.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'reviewer-rating-insight/:id/edit',
        component: ReviewerRatingInsightUpdateComponent,
        resolve: {
            reviewer: ReviewerRatingInsightResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ratingSystemApp.reviewer.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const reviewerPopupRoute: Routes = [
    {
        path: 'reviewer-rating-insight/:id/delete',
        component: ReviewerRatingInsightDeletePopupComponent,
        resolve: {
            reviewer: ReviewerRatingInsightResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ratingSystemApp.reviewer.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
