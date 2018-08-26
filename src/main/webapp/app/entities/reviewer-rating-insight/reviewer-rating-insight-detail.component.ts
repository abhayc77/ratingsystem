import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IReviewerRatingInsight } from 'app/shared/model/reviewer-rating-insight.model';

@Component({
    selector: 'jhi-reviewer-rating-insight-detail',
    templateUrl: './reviewer-rating-insight-detail.component.html'
})
export class ReviewerRatingInsightDetailComponent implements OnInit {
    reviewer: IReviewerRatingInsight;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ reviewer }) => {
            this.reviewer = reviewer;
        });
    }

    previousState() {
        window.history.back();
    }
}
