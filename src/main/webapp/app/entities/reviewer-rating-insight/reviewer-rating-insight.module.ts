import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RatingSystemSharedModule } from 'app/shared';
import {
    ReviewerRatingInsightComponent,
    ReviewerRatingInsightDetailComponent,
    ReviewerRatingInsightUpdateComponent,
    ReviewerRatingInsightDeletePopupComponent,
    ReviewerRatingInsightDeleteDialogComponent,
    reviewerRoute,
    reviewerPopupRoute
} from './';

const ENTITY_STATES = [...reviewerRoute, ...reviewerPopupRoute];

@NgModule({
    imports: [RatingSystemSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ReviewerRatingInsightComponent,
        ReviewerRatingInsightDetailComponent,
        ReviewerRatingInsightUpdateComponent,
        ReviewerRatingInsightDeleteDialogComponent,
        ReviewerRatingInsightDeletePopupComponent
    ],
    entryComponents: [
        ReviewerRatingInsightComponent,
        ReviewerRatingInsightUpdateComponent,
        ReviewerRatingInsightDeleteDialogComponent,
        ReviewerRatingInsightDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RatingSystemReviewerRatingInsightModule {}
