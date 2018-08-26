import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProductRatingInsight } from 'app/shared/model/product-rating-insight.model';

@Component({
    selector: 'jhi-product-rating-insight-detail',
    templateUrl: './product-rating-insight-detail.component.html'
})
export class ProductRatingInsightDetailComponent implements OnInit {
    product: IProductRatingInsight;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ product }) => {
            this.product = product;
        });
    }

    previousState() {
        window.history.back();
    }
}
