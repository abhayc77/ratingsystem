/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { RatingSystemTestModule } from '../../../test.module';
import { ReviewerProfileRatingInsightDeleteDialogComponent } from 'app/entities/reviewer-profile-rating-insight/reviewer-profile-rating-insight-delete-dialog.component';
import { ReviewerProfileRatingInsightService } from 'app/entities/reviewer-profile-rating-insight/reviewer-profile-rating-insight.service';

describe('Component Tests', () => {
    describe('ReviewerProfileRatingInsight Management Delete Component', () => {
        let comp: ReviewerProfileRatingInsightDeleteDialogComponent;
        let fixture: ComponentFixture<ReviewerProfileRatingInsightDeleteDialogComponent>;
        let service: ReviewerProfileRatingInsightService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [RatingSystemTestModule],
                declarations: [ReviewerProfileRatingInsightDeleteDialogComponent]
            })
                .overrideTemplate(ReviewerProfileRatingInsightDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ReviewerProfileRatingInsightDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ReviewerProfileRatingInsightService);
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
