import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IReviewerProfileRatingInsight } from 'app/shared/model/reviewer-profile-rating-insight.model';

@Component({
    selector: 'jhi-reviewer-profile-rating-insight-detail',
    templateUrl: './reviewer-profile-rating-insight-detail.component.html'
})
export class ReviewerProfileRatingInsightDetailComponent implements OnInit {
    reviewerProfile: IReviewerProfileRatingInsight;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ reviewerProfile }) => {
            this.reviewerProfile = reviewerProfile;
        });
    }

    previousState() {
        window.history.back();
    }
}
