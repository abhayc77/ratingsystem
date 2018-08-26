import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IProductRatingInsight } from 'app/shared/model/product-rating-insight.model';
import { ProductRatingInsightService } from './product-rating-insight.service';

@Component({
    selector: 'jhi-product-rating-insight-delete-dialog',
    templateUrl: './product-rating-insight-delete-dialog.component.html'
})
export class ProductRatingInsightDeleteDialogComponent {
    product: IProductRatingInsight;

    constructor(
        private productService: ProductRatingInsightService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.productService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'productListModification',
                content: 'Deleted an product'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-product-rating-insight-delete-popup',
    template: ''
})
export class ProductRatingInsightDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ product }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ProductRatingInsightDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.product = product;
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
