import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IReviewRatingInsight } from 'app/shared/model/review-rating-insight.model';
import { ReviewRatingInsightService } from './review-rating-insight.service';
import { IReviewAnalysisRatingInsight } from 'app/shared/model/review-analysis-rating-insight.model';
import { ReviewAnalysisRatingInsightService } from 'app/entities/review-analysis-rating-insight';
import { IReviewerRatingInsight } from 'app/shared/model/reviewer-rating-insight.model';
import { ReviewerRatingInsightService } from 'app/entities/reviewer-rating-insight';
import { IProductRatingInsight } from 'app/shared/model/product-rating-insight.model';
import { ProductRatingInsightService } from 'app/entities/product-rating-insight';

@Component({
    selector: 'jhi-review-rating-insight-update',
    templateUrl: './review-rating-insight-update.component.html'
})
export class ReviewRatingInsightUpdateComponent implements OnInit {
    private _review: IReviewRatingInsight;
    isSaving: boolean;

    reviewanalyses: IReviewAnalysisRatingInsight[];

    reviewers: IReviewerRatingInsight[];

    products: IProductRatingInsight[];
    reviewDateTime: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private reviewService: ReviewRatingInsightService,
        private reviewAnalysisService: ReviewAnalysisRatingInsightService,
        private reviewerService: ReviewerRatingInsightService,
        private productService: ProductRatingInsightService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ review }) => {
            this.review = review;
        });
        this.reviewAnalysisService.query({ filter: 'review-is-null' }).subscribe(
            (res: HttpResponse<IReviewAnalysisRatingInsight[]>) => {
                if (!this.review.reviewAnalysisId) {
                    this.reviewanalyses = res.body;
                } else {
                    this.reviewAnalysisService.find(this.review.reviewAnalysisId).subscribe(
                        (subRes: HttpResponse<IReviewAnalysisRatingInsight>) => {
                            this.reviewanalyses = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.reviewerService.query().subscribe(
            (res: HttpResponse<IReviewerRatingInsight[]>) => {
                this.reviewers = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.productService.query().subscribe(
            (res: HttpResponse<IProductRatingInsight[]>) => {
                this.products = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.review.reviewDateTime = moment(this.reviewDateTime, DATE_TIME_FORMAT);
        if (this.review.id !== undefined) {
            this.subscribeToSaveResponse(this.reviewService.update(this.review));
        } else {
            this.subscribeToSaveResponse(this.reviewService.create(this.review));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IReviewRatingInsight>>) {
        result.subscribe((res: HttpResponse<IReviewRatingInsight>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackReviewAnalysisById(index: number, item: IReviewAnalysisRatingInsight) {
        return item.id;
    }

    trackReviewerById(index: number, item: IReviewerRatingInsight) {
        return item.id;
    }

    trackProductById(index: number, item: IProductRatingInsight) {
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
    get review() {
        return this._review;
    }

    set review(review: IReviewRatingInsight) {
        this._review = review;
        this.reviewDateTime = moment(review.reviewDateTime).format(DATE_TIME_FORMAT);
    }
}
