package com.insightsystems.ratinginsight.service.dto;

import java.io.Serializable;
import com.insightsystems.ratinginsight.domain.enumeration.ProductStatus;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Product entity. This class is used in ProductResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /products?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProductCriteria implements Serializable {
    /**
     * Class for filtering ProductStatus
     */
    public static class ProductStatusFilter extends Filter<ProductStatus> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter productID;

    private StringFilter uid;

    private StringFilter productName;

    private DoubleFilter price;

    private StringFilter shortDescription;

    private StringFilter longDescription;

    private StringFilter productImageURL;

    private StringFilter productURL;

    private StringFilter supplierID;

    private FloatFilter averageRating;

    private IntegerFilter ratingCount;

    private ProductStatusFilter productStatus;

    private LongFilter reviewId;

    public ProductCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getProductID() {
        return productID;
    }

    public void setProductID(StringFilter productID) {
        this.productID = productID;
    }

    public StringFilter getUid() {
        return uid;
    }

    public void setUid(StringFilter uid) {
        this.uid = uid;
    }

    public StringFilter getProductName() {
        return productName;
    }

    public void setProductName(StringFilter productName) {
        this.productName = productName;
    }

    public DoubleFilter getPrice() {
        return price;
    }

    public void setPrice(DoubleFilter price) {
        this.price = price;
    }

    public StringFilter getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(StringFilter shortDescription) {
        this.shortDescription = shortDescription;
    }

    public StringFilter getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(StringFilter longDescription) {
        this.longDescription = longDescription;
    }

    public StringFilter getProductImageURL() {
        return productImageURL;
    }

    public void setProductImageURL(StringFilter productImageURL) {
        this.productImageURL = productImageURL;
    }

    public StringFilter getProductURL() {
        return productURL;
    }

    public void setProductURL(StringFilter productURL) {
        this.productURL = productURL;
    }

    public StringFilter getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(StringFilter supplierID) {
        this.supplierID = supplierID;
    }

    public FloatFilter getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(FloatFilter averageRating) {
        this.averageRating = averageRating;
    }

    public IntegerFilter getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(IntegerFilter ratingCount) {
        this.ratingCount = ratingCount;
    }

    public ProductStatusFilter getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(ProductStatusFilter productStatus) {
        this.productStatus = productStatus;
    }

    public LongFilter getReviewId() {
        return reviewId;
    }

    public void setReviewId(LongFilter reviewId) {
        this.reviewId = reviewId;
    }

    @Override
    public String toString() {
        return "ProductCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (productID != null ? "productID=" + productID + ", " : "") +
                (uid != null ? "uid=" + uid + ", " : "") +
                (productName != null ? "productName=" + productName + ", " : "") +
                (price != null ? "price=" + price + ", " : "") +
                (shortDescription != null ? "shortDescription=" + shortDescription + ", " : "") +
                (longDescription != null ? "longDescription=" + longDescription + ", " : "") +
                (productImageURL != null ? "productImageURL=" + productImageURL + ", " : "") +
                (productURL != null ? "productURL=" + productURL + ", " : "") +
                (supplierID != null ? "supplierID=" + supplierID + ", " : "") +
                (averageRating != null ? "averageRating=" + averageRating + ", " : "") +
                (ratingCount != null ? "ratingCount=" + ratingCount + ", " : "") +
                (productStatus != null ? "productStatus=" + productStatus + ", " : "") +
                (reviewId != null ? "reviewId=" + reviewId + ", " : "") +
            "}";
    }

}
