import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RatingSystemSharedModule } from 'app/shared';
import {
    ProductRatingInsightComponent,
    ProductRatingInsightDetailComponent,
    ProductRatingInsightUpdateComponent,
    ProductRatingInsightDeletePopupComponent,
    ProductRatingInsightDeleteDialogComponent,
    productRoute,
    productPopupRoute
} from './';

const ENTITY_STATES = [...productRoute, ...productPopupRoute];

@NgModule({
    imports: [RatingSystemSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ProductRatingInsightComponent,
        ProductRatingInsightDetailComponent,
        ProductRatingInsightUpdateComponent,
        ProductRatingInsightDeleteDialogComponent,
        ProductRatingInsightDeletePopupComponent
    ],
    entryComponents: [
        ProductRatingInsightComponent,
        ProductRatingInsightUpdateComponent,
        ProductRatingInsightDeleteDialogComponent,
        ProductRatingInsightDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RatingSystemProductRatingInsightModule {}
