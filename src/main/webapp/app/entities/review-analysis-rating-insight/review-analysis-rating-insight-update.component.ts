import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IReviewAnalysisRatingInsight } from 'app/shared/model/review-analysis-rating-insight.model';
import { ReviewAnalysisRatingInsightService } from './review-analysis-rating-insight.service';

@Component({
    selector: 'jhi-review-analysis-rating-insight-update',
    templateUrl: './review-analysis-rating-insight-update.component.html'
})
export class ReviewAnalysisRatingInsightUpdateComponent implements OnInit {
    private _reviewAnalysis: IReviewAnalysisRatingInsight;
    isSaving: boolean;
    reviewAnalysisDateTime: string;

    constructor(private reviewAnalysisService: ReviewAnalysisRatingInsightService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ reviewAnalysis }) => {
            this.reviewAnalysis = reviewAnalysis;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.reviewAnalysis.reviewAnalysisDateTime = moment(this.reviewAnalysisDateTime, DATE_TIME_FORMAT);
        if (this.reviewAnalysis.id !== undefined) {
            this.subscribeToSaveResponse(this.reviewAnalysisService.update(this.reviewAnalysis));
        } else {
            this.subscribeToSaveResponse(this.reviewAnalysisService.create(this.reviewAnalysis));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IReviewAnalysisRatingInsight>>) {
        result.subscribe(
            (res: HttpResponse<IReviewAnalysisRatingInsight>) => this.onSaveSuccess(),
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
    get reviewAnalysis() {
        return this._reviewAnalysis;
    }

    set reviewAnalysis(reviewAnalysis: IReviewAnalysisRatingInsight) {
        this._reviewAnalysis = reviewAnalysis;
        this.reviewAnalysisDateTime = moment(reviewAnalysis.reviewAnalysisDateTime).format(DATE_TIME_FORMAT);
    }
}
