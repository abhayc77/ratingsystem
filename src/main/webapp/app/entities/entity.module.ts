import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { RatingSystemProductRatingInsightModule } from './product-rating-insight/product-rating-insight.module';
import { RatingSystemReviewRatingInsightModule } from './review-rating-insight/review-rating-insight.module';
import { RatingSystemReviewAnalysisRatingInsightModule } from './review-analysis-rating-insight/review-analysis-rating-insight.module';
import { RatingSystemReviewerRatingInsightModule } from './reviewer-rating-insight/reviewer-rating-insight.module';
import { RatingSystemReviewerProfileRatingInsightModule } from './reviewer-profile-rating-insight/reviewer-profile-rating-insight.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        RatingSystemProductRatingInsightModule,
        RatingSystemReviewRatingInsightModule,
        RatingSystemReviewAnalysisRatingInsightModule,
        RatingSystemReviewerRatingInsightModule,
        RatingSystemReviewerProfileRatingInsightModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RatingSystemEntityModule {}
