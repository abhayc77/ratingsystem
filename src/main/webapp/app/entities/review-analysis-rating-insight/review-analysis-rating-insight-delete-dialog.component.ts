import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IReviewAnalysisRatingInsight } from 'app/shared/model/review-analysis-rating-insight.model';
import { ReviewAnalysisRatingInsightService } from './review-analysis-rating-insight.service';

@Component({
    selector: 'jhi-review-analysis-rating-insight-delete-dialog',
    templateUrl: './review-analysis-rating-insight-delete-dialog.component.html'
})
export class ReviewAnalysisRatingInsightDeleteDialogComponent {
    reviewAnalysis: IReviewAnalysisRatingInsight;

    constructor(
        private reviewAnalysisService: ReviewAnalysisRatingInsightService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.reviewAnalysisService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'reviewAnalysisListModification',
                content: 'Deleted an reviewAnalysis'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-review-analysis-rating-insight-delete-popup',
    template: ''
})
export class ReviewAnalysisRatingInsightDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ reviewAnalysis }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ReviewAnalysisRatingInsightDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.reviewAnalysis = reviewAnalysis;
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
