import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IReviewerRatingInsight } from 'app/shared/model/reviewer-rating-insight.model';
import { ReviewerRatingInsightService } from './reviewer-rating-insight.service';
import { IReviewerProfileRatingInsight } from 'app/shared/model/reviewer-profile-rating-insight.model';
import { ReviewerProfileRatingInsightService } from 'app/entities/reviewer-profile-rating-insight';
import { IReviewRatingInsight } from 'app/shared/model/review-rating-insight.model';
import { ReviewRatingInsightService } from 'app/entities/review-rating-insight';

@Component({
    selector: 'jhi-reviewer-rating-insight-update',
    templateUrl: './reviewer-rating-insight-update.component.html'
})
export class ReviewerRatingInsightUpdateComponent implements OnInit {
    private _reviewer: IReviewerRatingInsight;
    isSaving: boolean;

    profiles: IReviewerProfileRatingInsight[];

    reviews: IReviewRatingInsight[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private reviewerService: ReviewerRatingInsightService,
        private reviewerProfileService: ReviewerProfileRatingInsightService,
        private reviewService: ReviewRatingInsightService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ reviewer }) => {
            this.reviewer = reviewer;
        });
        this.reviewerProfileService.query({ filter: 'reviewer(firstname)-is-null' }).subscribe(
            (res: HttpResponse<IReviewerProfileRatingInsight[]>) => {
                if (!this.reviewer.profileId) {
                    this.profiles = res.body;
                } else {
                    this.reviewerProfileService.find(this.reviewer.profileId).subscribe(
                        (subRes: HttpResponse<IReviewerProfileRatingInsight>) => {
                            this.profiles = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.reviewService.query().subscribe(
            (res: HttpResponse<IReviewRatingInsight[]>) => {
                this.reviews = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.reviewer.id !== undefined) {
            this.subscribeToSaveResponse(this.reviewerService.update(this.reviewer));
        } else {
            this.subscribeToSaveResponse(this.reviewerService.create(this.reviewer));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IReviewerRatingInsight>>) {
        result.subscribe(
            (res: HttpResponse<IReviewerRatingInsight>) => this.onSaveSuccess(),
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

    trackReviewerProfileById(index: number, item: IReviewerProfileRatingInsight) {
        return item.id;
    }

    trackReviewById(index: number, item: IReviewRatingInsight) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
    get reviewer() {
        return this._reviewer;
    }

    set reviewer(reviewer: IReviewerRatingInsight) {
        this._reviewer = reviewer;
    }
}
