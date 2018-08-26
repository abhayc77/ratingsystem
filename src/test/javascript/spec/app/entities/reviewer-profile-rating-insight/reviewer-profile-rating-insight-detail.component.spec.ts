/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RatingSystemTestModule } from '../../../test.module';
import { ReviewerProfileRatingInsightDetailComponent } from 'app/entities/reviewer-profile-rating-insight/reviewer-profile-rating-insight-detail.component';
import { ReviewerProfileRatingInsight } from 'app/shared/model/reviewer-profile-rating-insight.model';

describe('Component Tests', () => {
    describe('ReviewerProfileRatingInsight Management Detail Component', () => {
        let comp: ReviewerProfileRatingInsightDetailComponent;
        let fixture: ComponentFixture<ReviewerProfileRatingInsightDetailComponent>;
        const route = ({ data: of({ reviewerProfile: new ReviewerProfileRatingInsight(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [RatingSystemTestModule],
                declarations: [ReviewerProfileRatingInsightDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ReviewerProfileRatingInsightDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ReviewerProfileRatingInsightDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.reviewerProfile).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
