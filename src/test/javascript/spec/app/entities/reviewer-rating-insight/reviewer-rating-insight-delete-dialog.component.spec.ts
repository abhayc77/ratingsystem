/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { RatingSystemTestModule } from '../../../test.module';
import { ReviewerRatingInsightDeleteDialogComponent } from 'app/entities/reviewer-rating-insight/reviewer-rating-insight-delete-dialog.component';
import { ReviewerRatingInsightService } from 'app/entities/reviewer-rating-insight/reviewer-rating-insight.service';

describe('Component Tests', () => {
    describe('ReviewerRatingInsight Management Delete Component', () => {
        let comp: ReviewerRatingInsightDeleteDialogComponent;
        let fixture: ComponentFixture<ReviewerRatingInsightDeleteDialogComponent>;
        let service: ReviewerRatingInsightService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [RatingSystemTestModule],
                declarations: [ReviewerRatingInsightDeleteDialogComponent]
            })
                .overrideTemplate(ReviewerRatingInsightDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ReviewerRatingInsightDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ReviewerRatingInsightService);
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
