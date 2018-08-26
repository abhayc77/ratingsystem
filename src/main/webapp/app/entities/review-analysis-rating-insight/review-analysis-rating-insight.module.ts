import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RatingSystemSharedModule } from 'app/shared';
import {
    ReviewAnalysisRatingInsightComponent,
    ReviewAnalysisRatingInsightDetailComponent,
    ReviewAnalysisRatingInsightUpdateComponent,
    ReviewAnalysisRatingInsightDeletePopupComponent,
    ReviewAnalysisRatingInsightDeleteDialogComponent,
    reviewAnalysisRoute,
    reviewAnalysisPopupRoute
} from './';

const ENTITY_STATES = [...reviewAnalysisRoute, ...reviewAnalysisPopupRoute];

@NgModule({
    imports: [RatingSystemSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ReviewAnalysisRatingInsightComponent,
        ReviewAnalysisRatingInsightDetailComponent,
        ReviewAnalysisRatingInsightUpdateComponent,
        ReviewAnalysisRatingInsightDeleteDialogComponent,
        ReviewAnalysisRatingInsightDeletePopupComponent
    ],
    entryComponents: [
        ReviewAnalysisRatingInsightComponent,
        ReviewAnalysisRatingInsightUpdateComponent,
        ReviewAnalysisRatingInsightDeleteDialogComponent,
        ReviewAnalysisRatingInsightDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RatingSystemReviewAnalysisRatingInsightModule {}
