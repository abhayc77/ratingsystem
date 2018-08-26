import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ReviewAnalysisRatingInsight } from 'app/shared/model/review-analysis-rating-insight.model';
import { ReviewAnalysisRatingInsightService } from './review-analysis-rating-insight.service';
import { ReviewAnalysisRatingInsightComponent } from './review-analysis-rating-insight.component';
import { ReviewAnalysisRatingInsightDetailComponent } from './review-analysis-rating-insight-detail.component';
import { ReviewAnalysisRatingInsightUpdateComponent } from './review-analysis-rating-insight-update.component';
import { ReviewAnalysisRatingInsightDeletePopupComponent } from './review-analysis-rating-insight-delete-dialog.component';
import { IReviewAnalysisRatingInsight } from 'app/shared/model/review-analysis-rating-insight.model';

@Injectable({ providedIn: 'root' })
export class ReviewAnalysisRatingInsightResolve implements Resolve<IReviewAnalysisRatingInsight> {
    constructor(private service: ReviewAnalysisRatingInsightService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((reviewAnalysis: HttpResponse<ReviewAnalysisRatingInsight>) => reviewAnalysis.body));
        }
        return of(new ReviewAnalysisRatingInsight());
    }
}

export const reviewAnalysisRoute: Routes = [
    {
        path: 'review-analysis-rating-insight',
        component: ReviewAnalysisRatingInsightComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ratingSystemApp.reviewAnalysis.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'review-analysis-rating-insight/:id/view',
        component: ReviewAnalysisRatingInsightDetailComponent,
        resolve: {
            reviewAnalysis: ReviewAnalysisRatingInsightResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ratingSystemApp.reviewAnalysis.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'review-analysis-rating-insight/new',
        component: ReviewAnalysisRatingInsightUpdateComponent,
        resolve: {
            reviewAnalysis: ReviewAnalysisRatingInsightResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ratingSystemApp.reviewAnalysis.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'review-analysis-rating-insight/:id/edit',
        component: ReviewAnalysisRatingInsightUpdateComponent,
        resolve: {
            reviewAnalysis: ReviewAnalysisRatingInsightResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ratingSystemApp.reviewAnalysis.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const reviewAnalysisPopupRoute: Routes = [
    {
        path: 'review-analysis-rating-insight/:id/delete',
        component: ReviewAnalysisRatingInsightDeletePopupComponent,
        resolve: {
            reviewAnalysis: ReviewAnalysisRatingInsightResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ratingSystemApp.reviewAnalysis.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
