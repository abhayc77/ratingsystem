/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { RatingSystemTestModule } from '../../../test.module';
import { ProductRatingInsightUpdateComponent } from 'app/entities/product-rating-insight/product-rating-insight-update.component';
import { ProductRatingInsightService } from 'app/entities/product-rating-insight/product-rating-insight.service';
import { ProductRatingInsight } from 'app/shared/model/product-rating-insight.model';

describe('Component Tests', () => {
    describe('ProductRatingInsight Management Update Component', () => {
        let comp: ProductRatingInsightUpdateComponent;
        let fixture: ComponentFixture<ProductRatingInsightUpdateComponent>;
        let service: ProductRatingInsightService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [RatingSystemTestModule],
                declarations: [ProductRatingInsightUpdateComponent]
            })
                .overrideTemplate(ProductRatingInsightUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ProductRatingInsightUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProductRatingInsightService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new ProductRatingInsight(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.product = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new ProductRatingInsight();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.product = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
