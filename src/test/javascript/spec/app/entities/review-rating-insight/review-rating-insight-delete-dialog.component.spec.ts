/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { RatingSystemTestModule } from '../../../test.module';
import { ReviewRatingInsightDeleteDialogComponent } from 'app/entities/review-rating-insight/review-rating-insight-delete-dialog.component';
import { ReviewRatingInsightService } from 'app/entities/review-rating-insight/review-rating-insight.service';

describe('Component Tests', () => {
    describe('ReviewRatingInsight Management Delete Component', () => {
        let comp: ReviewRatingInsightDeleteDialogComponent;
        let fixture: ComponentFixture<ReviewRatingInsightDeleteDialogComponent>;
        let service: ReviewRatingInsightService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [RatingSystemTestModule],
                declarations: [ReviewRatingInsightDeleteDialogComponent]
            })
                .overrideTemplate(ReviewRatingInsightDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ReviewRatingInsightDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ReviewRatingInsightService);
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
