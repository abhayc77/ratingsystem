/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { RatingSystemTestModule } from '../../../test.module';
import { ReviewAnalysisRatingInsightUpdateComponent } from 'app/entities/review-analysis-rating-insight/review-analysis-rating-insight-update.component';
import { ReviewAnalysisRatingInsightService } from 'app/entities/review-analysis-rating-insight/review-analysis-rating-insight.service';
import { ReviewAnalysisRatingInsight } from 'app/shared/model/review-analysis-rating-insight.model';

describe('Component Tests', () => {
    describe('ReviewAnalysisRatingInsight Management Update Component', () => {
        let comp: ReviewAnalysisRatingInsightUpdateComponent;
        let fixture: ComponentFixture<ReviewAnalysisRatingInsightUpdateComponent>;
        let service: ReviewAnalysisRatingInsightService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [RatingSystemTestModule],
                declarations: [ReviewAnalysisRatingInsightUpdateComponent]
            })
                .overrideTemplate(ReviewAnalysisRatingInsightUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ReviewAnalysisRatingInsightUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ReviewAnalysisRatingInsightService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new ReviewAnalysisRatingInsight(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.reviewAnalysis = entity;
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
                    const entity = new ReviewAnalysisRatingInsight();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.reviewAnalysis = entity;
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
