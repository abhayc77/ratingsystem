/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { RatingSystemTestModule } from '../../../test.module';
import { ReviewerRatingInsightUpdateComponent } from 'app/entities/reviewer-rating-insight/reviewer-rating-insight-update.component';
import { ReviewerRatingInsightService } from 'app/entities/reviewer-rating-insight/reviewer-rating-insight.service';
import { ReviewerRatingInsight } from 'app/shared/model/reviewer-rating-insight.model';

describe('Component Tests', () => {
    describe('ReviewerRatingInsight Management Update Component', () => {
        let comp: ReviewerRatingInsightUpdateComponent;
        let fixture: ComponentFixture<ReviewerRatingInsightUpdateComponent>;
        let service: ReviewerRatingInsightService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [RatingSystemTestModule],
                declarations: [ReviewerRatingInsightUpdateComponent]
            })
                .overrideTemplate(ReviewerRatingInsightUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ReviewerRatingInsightUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ReviewerRatingInsightService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new ReviewerRatingInsight(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.reviewer = entity;
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
                    const entity = new ReviewerRatingInsight();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.reviewer = entity;
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
