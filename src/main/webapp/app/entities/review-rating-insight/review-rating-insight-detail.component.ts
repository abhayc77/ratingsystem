import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IReviewRatingInsight } from 'app/shared/model/review-rating-insight.model';

@Component({
    selector: 'jhi-review-rating-insight-detail',
    templateUrl: './review-rating-insight-detail.component.html'
})
export class ReviewRatingInsightDetailComponent implements OnInit {
    review: IReviewRatingInsight;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ review }) => {
            this.review = review;
        });
    }

    previousState() {
        window.history.back();
    }
}
