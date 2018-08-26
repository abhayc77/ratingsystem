/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { RatingSystemTestModule } from '../../../test.module';
import { ReviewRatingInsightUpdateComponent } from 'app/entities/review-rating-insight/review-rating-insight-update.component';
import { ReviewRatingInsightService } from 'app/entities/review-rating-insight/review-rating-insight.service';
import { ReviewRatingInsight } from 'app/shared/model/review-rating-insight.model';

describe('Component Tests', () => {
    describe('ReviewRatingInsight Management Update Component', () => {
        let comp: ReviewRatingInsightUpdateComponent;
        let fixture: ComponentFixture<ReviewRatingInsightUpdateComponent>;
        let service: ReviewRatingInsightService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [RatingSystemTestModule],
                declarations: [ReviewRatingInsightUpdateComponent]
            })
                .overrideTemplate(ReviewRatingInsightUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ReviewRatingInsightUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ReviewRatingInsightService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new ReviewRatingInsight(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.review = entity;
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
                    const entity = new ReviewRatingInsight();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.review = entity;
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
