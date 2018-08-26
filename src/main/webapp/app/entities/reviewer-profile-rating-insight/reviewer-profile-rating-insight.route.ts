import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ReviewerProfileRatingInsight } from 'app/shared/model/reviewer-profile-rating-insight.model';
import { ReviewerProfileRatingInsightService } from './reviewer-profile-rating-insight.service';
import { ReviewerProfileRatingInsightComponent } from './reviewer-profile-rating-insight.component';
import { ReviewerProfileRatingInsightDetailComponent } from './reviewer-profile-rating-insight-detail.component';
import { ReviewerProfileRatingInsightUpdateComponent } from './reviewer-profile-rating-insight-update.component';
import { ReviewerProfileRatingInsightDeletePopupComponent } from './reviewer-profile-rating-insight-delete-dialog.component';
import { IReviewerProfileRatingInsight } from 'app/shared/model/reviewer-profile-rating-insight.model';

@Injectable({ providedIn: 'root' })
export class ReviewerProfileRatingInsightResolve implements Resolve<IReviewerProfileRatingInsight> {
    constructor(private service: ReviewerProfileRatingInsightService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((reviewerProfile: HttpResponse<ReviewerProfileRatingInsight>) => reviewerProfile.body));
        }
        return of(new ReviewerProfileRatingInsight());
    }
}

export const reviewerProfileRoute: Routes = [
    {
        path: 'reviewer-profile-rating-insight',
        component: ReviewerProfileRatingInsightComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ratingSystemApp.reviewerProfile.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'reviewer-profile-rating-insight/:id/view',
        component: ReviewerProfileRatingInsightDetailComponent,
        resolve: {
            reviewerProfile: ReviewerProfileRatingInsightResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ratingSystemApp.reviewerProfile.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'reviewer-profile-rating-insight/new',
        component: ReviewerProfileRatingInsightUpdateComponent,
        resolve: {
            reviewerProfile: ReviewerProfileRatingInsightResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ratingSystemApp.reviewerProfile.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'reviewer-profile-rating-insight/:id/edit',
        component: ReviewerProfileRatingInsightUpdateComponent,
        resolve: {
            reviewerProfile: ReviewerProfileRatingInsightResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ratingSystemApp.reviewerProfile.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const reviewerProfilePopupRoute: Routes = [
    {
        path: 'reviewer-profile-rating-insight/:id/delete',
        component: ReviewerProfileRatingInsightDeletePopupComponent,
        resolve: {
            reviewerProfile: ReviewerProfileRatingInsightResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ratingSystemApp.reviewerProfile.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
