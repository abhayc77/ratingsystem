import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IProductRatingInsight } from 'app/shared/model/product-rating-insight.model';
import { ProductRatingInsightService } from './product-rating-insight.service';
import { IReviewRatingInsight } from 'app/shared/model/review-rating-insight.model';
import { ReviewRatingInsightService } from 'app/entities/review-rating-insight';

@Component({
    selector: 'jhi-product-rating-insight-update',
    templateUrl: './product-rating-insight-update.component.html'
})
export class ProductRatingInsightUpdateComponent implements OnInit {
    private _product: IProductRatingInsight;
    isSaving: boolean;

    reviews: IReviewRatingInsight[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private productService: ProductRatingInsightService,
        private reviewService: ReviewRatingInsightService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ product }) => {
            this.product = product;
        });
        this.reviewService.query().subscribe(
            (res: HttpResponse<IReviewRatingInsight[]>) => {
                this.reviews = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.product.id !== undefined) {
            this.subscribeToSaveResponse(this.productService.update(this.product));
        } else {
            this.subscribeToSaveResponse(this.productService.create(this.product));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IProductRatingInsight>>) {
        result.subscribe(
            (res: HttpResponse<IProductRatingInsight>) => this.onSaveSuccess(),
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackReviewById(index: number, item: IReviewRatingInsight) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
    get product() {
        return this._product;
    }

    set product(product: IProductRatingInsight) {
        this._product = product;
    }
}
