/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { RatingSystemTestModule } from '../../../test.module';
import { ReviewAnalysisRatingInsightDeleteDialogComponent } from 'app/entities/review-analysis-rating-insight/review-analysis-rating-insight-delete-dialog.component';
import { ReviewAnalysisRatingInsightService } from 'app/entities/review-analysis-rating-insight/review-analysis-rating-insight.service';

describe('Component Tests', () => {
    describe('ReviewAnalysisRatingInsight Management Delete Component', () => {
        let comp: ReviewAnalysisRatingInsightDeleteDialogComponent;
        let fixture: ComponentFixture<ReviewAnalysisRatingInsightDeleteDialogComponent>;
        let service: ReviewAnalysisRatingInsightService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [RatingSystemTestModule],
                declarations: [ReviewAnalysisRatingInsightDeleteDialogComponent]
            })
                .overrideTemplate(ReviewAnalysisRatingInsightDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ReviewAnalysisRatingInsightDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ReviewAnalysisRatingInsightService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
