/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RatingSystemTestModule } from '../../../test.module';
import { ReviewAnalysisRatingInsightDetailComponent } from 'app/entities/review-analysis-rating-insight/review-analysis-rating-insight-detail.component';
import { ReviewAnalysisRatingInsight } from 'app/shared/model/review-analysis-rating-insight.model';

describe('Component Tests', () => {
    describe('ReviewAnalysisRatingInsight Management Detail Component', () => {
        let comp: ReviewAnalysisRatingInsightDetailComponent;
        let fixture: ComponentFixture<ReviewAnalysisRatingInsightDetailComponent>;
        const route = ({ data: of({ reviewAnalysis: new ReviewAnalysisRatingInsight(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [RatingSystemTestModule],
                declarations: [ReviewAnalysisRatingInsightDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ReviewAnalysisRatingInsightDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ReviewAnalysisRatingInsightDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.reviewAnalysis).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
