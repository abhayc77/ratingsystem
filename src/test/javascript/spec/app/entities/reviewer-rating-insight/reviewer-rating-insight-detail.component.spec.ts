/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RatingSystemTestModule } from '../../../test.module';
import { ReviewerRatingInsightDetailComponent } from 'app/entities/reviewer-rating-insight/reviewer-rating-insight-detail.component';
import { ReviewerRatingInsight } from 'app/shared/model/reviewer-rating-insight.model';

describe('Component Tests', () => {
    describe('ReviewerRatingInsight Management Detail Component', () => {
        let comp: ReviewerRatingInsightDetailComponent;
        let fixture: ComponentFixture<ReviewerRatingInsightDetailComponent>;
        const route = ({ data: of({ reviewer: new ReviewerRatingInsight(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [RatingSystemTestModule],
                declarations: [ReviewerRatingInsightDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ReviewerRatingInsightDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ReviewerRatingInsightDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.reviewer).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
