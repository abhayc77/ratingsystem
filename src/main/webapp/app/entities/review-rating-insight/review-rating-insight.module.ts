import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RatingSystemSharedModule } from 'app/shared';
import {
    ReviewRatingInsightComponent,
    ReviewRatingInsightDetailComponent,
    ReviewRatingInsightUpdateComponent,
    ReviewRatingInsightDeletePopupComponent,
    ReviewRatingInsightDeleteDialogComponent,
    reviewRoute,
    reviewPopupRoute
} from './';

const ENTITY_STATES = [...reviewRoute, ...reviewPopupRoute];

@NgModule({
    imports: [RatingSystemSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ReviewRatingInsightComponent,
        ReviewRatingInsightDetailComponent,
        ReviewRatingInsightUpdateComponent,
        ReviewRatingInsightDeleteDialogComponent,
        ReviewRatingInsightDeletePopupComponent
    ],
    entryComponents: [
        ReviewRatingInsightComponent,
        ReviewRatingInsightUpdateComponent,
        ReviewRatingInsightDeleteDialogComponent,
        ReviewRatingInsightDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RatingSystemReviewRatingInsightModule {}
