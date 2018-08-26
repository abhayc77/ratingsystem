/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RatingSystemTestModule } from '../../../test.module';
import { ProductRatingInsightDetailComponent } from 'app/entities/product-rating-insight/product-rating-insight-detail.component';
import { ProductRatingInsight } from 'app/shared/model/product-rating-insight.model';

describe('Component Tests', () => {
    describe('ProductRatingInsight Management Detail Component', () => {
        let comp: ProductRatingInsightDetailComponent;
        let fixture: ComponentFixture<ProductRatingInsightDetailComponent>;
        const route = ({ data: of({ product: new ProductRatingInsight(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [RatingSystemTestModule],
                declarations: [ProductRatingInsightDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ProductRatingInsightDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ProductRatingInsightDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.product).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
