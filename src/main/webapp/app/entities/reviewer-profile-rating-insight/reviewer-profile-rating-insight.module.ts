import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RatingSystemSharedModule } from 'app/shared';
import {
    ReviewerProfileRatingInsightComponent,
    ReviewerProfileRatingInsightDetailComponent,
    ReviewerProfileRatingInsightUpdateComponent,
    ReviewerProfileRatingInsightDeletePopupComponent,
    ReviewerProfileRatingInsightDeleteDialogComponent,
    reviewerProfileRoute,
    reviewerProfilePopupRoute
} from './';

const ENTITY_STATES = [...reviewerProfileRoute, ...reviewerProfilePopupRoute];

@NgModule({
    imports: [RatingSystemSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ReviewerProfileRatingInsightComponent,
        ReviewerProfileRatingInsightDetailComponent,
        ReviewerProfileRatingInsightUpdateComponent,
        ReviewerProfileRatingInsightDeleteDialogComponent,
        ReviewerProfileRatingInsightDeletePopupComponent
    ],
    entryComponents: [
        ReviewerProfileRatingInsightComponent,
        ReviewerProfileRatingInsightUpdateComponent,
        ReviewerProfileRatingInsightDeleteDialogComponent,
        ReviewerProfileRatingInsightDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RatingSystemReviewerProfileRatingInsightModule {}
