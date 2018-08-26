import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IReviewAnalysisRatingInsight } from 'app/shared/model/review-analysis-rating-insight.model';

@Component({
    selector: 'jhi-review-analysis-rating-insight-detail',
    templateUrl: './review-analysis-rating-insight-detail.component.html'
})
export class ReviewAnalysisRatingInsightDetailComponent implements OnInit {
    reviewAnalysis: IReviewAnalysisRatingInsight;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ reviewAnalysis }) => {
            this.reviewAnalysis = reviewAnalysis;
        });
    }

    previousState() {
        window.history.back();
    }
}
