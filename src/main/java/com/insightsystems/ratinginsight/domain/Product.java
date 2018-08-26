package com.insightsystems.ratinginsight.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.insightsystems.ratinginsight.domain.enumeration.ProductStatus;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "product_id")
    private String productID;

    @Column(name = "jhi_uid")
    private String uid;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "price")
    private Double price;

    @Column(name = "short_description")
    private String shortDescription;

    @Column(name = "long_description")
    private String longDescription;

    @Column(name = "product_image_url")
    private String productImageURL;

    @Column(name = "product_url")
    private String productURL;

    @Column(name = "supplier_id")
    private String supplierID;

    @Column(name = "average_rating")
    private Float averageRating;

    @Column(name = "rating_count")
    private Integer ratingCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_status")
    private ProductStatus productStatus;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "product_review",
               joinColumns = @JoinColumn(name = "products_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "reviews_id", referencedColumnName = "id"))
    private Set<Review> reviews = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductID() {
        return productID;
    }

    public Product productID(String productID) {
        this.productID = productID;
        return this;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getUid() {
        return uid;
    }

    public Product uid(String uid) {
        this.uid = uid;
        return this;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProductName() {
        return productName;
    }

    public Product productName(String productName) {
        this.productName = productName;
        return this;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getPrice() {
        return price;
    }

    public Product price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public Product shortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
        return this;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public Product longDescription(String longDescription) {
        this.longDescription = longDescription;
        return this;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getProductImageURL() {
        return productImageURL;
    }

    public Product productImageURL(String productImageURL) {
        this.productImageURL = productImageURL;
        return this;
    }

    public void setProductImageURL(String productImageURL) {
        this.productImageURL = productImageURL;
    }

    public String getProductURL() {
        return productURL;
    }

    public Product productURL(String productURL) {
        this.productURL = productURL;
        return this;
    }

    public void setProductURL(String productURL) {
        this.productURL = productURL;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public Product supplierID(String supplierID) {
        this.supplierID = supplierID;
        return this;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    public Float getAverageRating() {
        return averageRating;
    }

    public Product averageRating(Float averageRating) {
        this.averageRating = averageRating;
        return this;
    }

    public void setAverageRating(Float averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getRatingCount() {
        return ratingCount;
    }

    public Product ratingCount(Integer ratingCount) {
        this.ratingCount = ratingCount;
        return this;
    }

    public void setRatingCount(Integer ratingCount) {
        this.ratingCount = ratingCount;
    }

    public ProductStatus getProductStatus() {
        return productStatus;
    }

    public Product productStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
        return this;
    }

    public void setProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public Product reviews(Set<Review> reviews) {
        this.reviews = reviews;
        return this;
    }

    public Product addReview(Review review) {
        this.reviews.add(review);
        review.getProducts().add(this);
        return this;
    }

    public Product removeReview(Review review) {
        this.reviews.remove(review);
        review.getProducts().remove(this);
        return this;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Product product = (Product) o;
        if (product.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), product.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Product{" +
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
