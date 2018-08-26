/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RatingSystemTestModule } from '../../../test.module';
import { ReviewRatingInsightDetailComponent } from 'app/entities/review-rating-insight/review-rating-insight-detail.component';
import { ReviewRatingInsight } from 'app/shared/model/review-rating-insight.model';

describe('Component Tests', () => {
    describe('ReviewRatingInsight Management Detail Component', () => {
        let comp: ReviewRatingInsightDetailComponent;
        let fixture: ComponentFixture<ReviewRatingInsightDetailComponent>;
        const route = ({ data: of({ review: new ReviewRatingInsight(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [RatingSystemTestModule],
                declarations: [ReviewRatingInsightDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ReviewRatingInsightDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ReviewRatingInsightDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.review).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
