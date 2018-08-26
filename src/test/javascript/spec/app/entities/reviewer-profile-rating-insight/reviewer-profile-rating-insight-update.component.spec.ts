/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { RatingSystemTestModule } from '../../../test.module';
import { ReviewerProfileRatingInsightUpdateComponent } from 'app/entities/reviewer-profile-rating-insight/reviewer-profile-rating-insight-update.component';
import { ReviewerProfileRatingInsightService } from 'app/entities/reviewer-profile-rating-insight/reviewer-profile-rating-insight.service';
import { ReviewerProfileRatingInsight } from 'app/shared/model/reviewer-profile-rating-insight.model';

describe('Component Tests', () => {
    describe('ReviewerProfileRatingInsight Management Update Component', () => {
        let comp: ReviewerProfileRatingInsightUpdateComponent;
        let fixture: ComponentFixture<ReviewerProfileRatingInsightUpdateComponent>;
        let service: ReviewerProfileRatingInsightService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [RatingSystemTestModule],
                declarations: [ReviewerProfileRatingInsightUpdateComponent]
            })
                .overrideTemplate(ReviewerProfileRatingInsightUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ReviewerProfileRatingInsightUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ReviewerProfileRatingInsightService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new ReviewerProfileRatingInsight(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.reviewerProfile = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new ReviewerProfileRatingInsight();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.reviewerProfile = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
