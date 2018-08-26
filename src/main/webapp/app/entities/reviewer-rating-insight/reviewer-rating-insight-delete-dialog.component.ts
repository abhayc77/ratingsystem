import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IReviewerRatingInsight } from 'app/shared/model/reviewer-rating-insight.model';
import { ReviewerRatingInsightService } from './reviewer-rating-insight.service';

@Component({
    selector: 'jhi-reviewer-rating-insight-delete-dialog',
    templateUrl: './reviewer-rating-insight-delete-dialog.component.html'
})
export class ReviewerRatingInsightDeleteDialogComponent {
    reviewer: IReviewerRatingInsight;

    constructor(
        private reviewerService: ReviewerRatingInsightService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.reviewerService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'reviewerListModification',
                content: 'Deleted an reviewer'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-reviewer-rating-insight-delete-popup',
    template: ''
})
export class ReviewerRatingInsightDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ reviewer }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ReviewerRatingInsightDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.reviewer = reviewer;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
