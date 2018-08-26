import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IReviewerProfileRatingInsight } from 'app/shared/model/reviewer-profile-rating-insight.model';
import { ReviewerProfileRatingInsightService } from './reviewer-profile-rating-insight.service';

@Component({
    selector: 'jhi-reviewer-profile-rating-insight-delete-dialog',
    templateUrl: './reviewer-profile-rating-insight-delete-dialog.component.html'
})
export class ReviewerProfileRatingInsightDeleteDialogComponent {
    reviewerProfile: IReviewerProfileRatingInsight;

    constructor(
        private reviewerProfileService: ReviewerProfileRatingInsightService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.reviewerProfileService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'reviewerProfileListModification',
                content: 'Deleted an reviewerProfile'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-reviewer-profile-rating-insight-delete-popup',
    template: ''
})
export class ReviewerProfileRatingInsightDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ reviewerProfile }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ReviewerProfileRatingInsightDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.reviewerProfile = reviewerProfile;
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
