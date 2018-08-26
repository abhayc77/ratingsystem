import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IReviewerProfileRatingInsight } from 'app/shared/model/reviewer-profile-rating-insight.model';
import { ReviewerProfileRatingInsightService } from './reviewer-profile-rating-insight.service';
import { IReviewerRatingInsight } from 'app/shared/model/reviewer-rating-insight.model';
import { ReviewerRatingInsightService } from 'app/entities/reviewer-rating-insight';

@Component({
    selector: 'jhi-reviewer-profile-rating-insight-update',
    templateUrl: './reviewer-profile-rating-insight-update.component.html'
})
export class ReviewerProfileRatingInsightUpdateComponent implements OnInit {
    private _reviewerProfile: IReviewerProfileRatingInsight;
    isSaving: boolean;

    reviewers: IReviewerRatingInsight[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private reviewerProfileService: ReviewerProfileRatingInsightService,
        private reviewerService: ReviewerRatingInsightService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ reviewerProfile }) => {
            this.reviewerProfile = reviewerProfile;
        });
        this.reviewerService.query().subscribe(
            (res: HttpResponse<IReviewerRatingInsight[]>) => {
                this.reviewers = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.reviewerProfile.id !== undefined) {
            this.subscribeToSaveResponse(this.reviewerProfileService.update(this.reviewerProfile));
        } else {
            this.subscribeToSaveResponse(this.reviewerProfileService.create(this.reviewerProfile));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IReviewerProfileRatingInsight>>) {
        result.subscribe(
            (res: HttpResponse<IReviewerProfileRatingInsight>) => this.onSaveSuccess(),
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackReviewerById(index: number, item: IReviewerRatingInsight) {
        return item.id;
    }
    get reviewerProfile() {
        return this._reviewerProfile;
    }

    set reviewerProfile(reviewerProfile: IReviewerProfileRatingInsight) {
        this._reviewerProfile = reviewerProfile;
    }
}
