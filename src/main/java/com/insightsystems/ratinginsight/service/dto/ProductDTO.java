package com.insightsystems.ratinginsight.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.insightsystems.ratinginsight.domain.enumeration.ProductStatus;

/**
 * A DTO for the Product entity.
 */
public class ProductDTO implements Serializable {

    private Long id;

    private String productID;

    private String uid;

    private String productName;

    private Double price;

    private String shortDescription;

    private String longDescription;

    private String productImageURL;

    private String productURL;

    private String supplierID;

    private Float averageRating;

    private Integer ratingCount;

    private ProductStatus productStatus;

    private Set<ReviewDTO> reviews = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getProductImageURL() {
        return productImageURL;
    }

    public void setProductImageURL(String productImageURL) {
        this.productImageURL = productImageURL;
    }

    public String getProductURL() {
        return productURL;
    }

    public void setProductURL(String productURL) {
        this.productURL = productURL;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    public Float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Float averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Integer ratingCount) {
        this.ratingCount = ratingCount;
    }

    public ProductStatus getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
    }

    public Set<ReviewDTO> getReviews() {
        return reviews;
    }

    public void setReviews(Set<ReviewDTO> reviews) {
        this.reviews = reviews;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProductDTO productDTO = (ProductDTO) o;
        if (productDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), productDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
            "id=" + getId() +
            ", productID='" + getProductID() + "'" +
            ", uid='" + getUid() + "'" +
            ", productName='" + getProductName() + "'" +
            ", price=" + getPrice() +
            ", shortDescription='" + getShortDescription() + "'" +
            ", longDescription='" + getLongDescription() + "'" +
            ", productImageURL='" + getProductImageURL() + "'" +
            ", productURL='" + getProductURL() + "'" +
            ", supplierID='" + getSupplierID() + "'" +
            ", averageRating=" + getAverageRating() +
            ", ratingCount=" + getRatingCount() +
            ", productStatus='" + getProductStatus() + "'" +
            "}";
    }
}
