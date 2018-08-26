package com.insightsystems.ratinginsight.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.insightsystems.ratinginsight.domain.enumeration.Language;

import com.insightsystems.ratinginsight.domain.enumeration.ReviewStatus;

/**
 * Review entity.
 * @author Abhay.
 */
@ApiModel(description = "Review entity. @author Abhay.")
@Entity
@Table(name = "review")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "review")
public class Review implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "product_id")
    private String productID;

    @Column(name = "jhi_uid")
    private String uid;

    @Column(name = "product_url")
    private String productURL;

    @Column(name = "title")
    private String title;

    @Column(name = "review_content")
    private String reviewContent;

    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private Language language;

    @Column(name = "review_date_time")
    private ZonedDateTime reviewDateTime;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "full_rating")
    private Integer fullRating;

    @Enumerated(EnumType.STRING)
    @Column(name = "review_status")
    private ReviewStatus reviewStatus;

    @Column(name = "helpful_votes")
    private Integer helpfulVotes;

    @Column(name = "total_votes")
    private Integer totalVotes;

    @Column(name = "verified_purchase")
    private Boolean verifiedPurchase;

    @Column(name = "real_name")
    private String realName;

    @OneToOne
    @JoinColumn(unique = true)
    private ReviewAnalysis reviewAnalysis;

    @ManyToMany(mappedBy = "reviews")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Reviewer> reviewers = new HashSet<>();

    @ManyToMany(mappedBy = "reviews")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Product> products = new HashSet<>();

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

    public Review productID(String productID) {
        this.productID = productID;
        return this;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getUid() {
        return uid;
    }

    public Review uid(String uid) {
        this.uid = uid;
        return this;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProductURL() {
        return productURL;
    }

    public Review productURL(String productURL) {
        this.productURL = productURL;
        return this;
    }

    public void setProductURL(String productURL) {
        this.productURL = productURL;
    }

    public String getTitle() {
        return title;
    }

    public Review title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public Review reviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
        return this;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public Language getLanguage() {
        return language;
    }

    public Review language(Language language) {
        this.language = language;
        return this;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public ZonedDateTime getReviewDateTime() {
        return reviewDateTime;
    }

    public Review reviewDateTime(ZonedDateTime reviewDateTime) {
        this.reviewDateTime = reviewDateTime;
        return this;
    }

    public void setReviewDateTime(ZonedDateTime reviewDateTime) {
        this.reviewDateTime = reviewDateTime;
    }

    public Integer getRating() {
        return rating;
    }

    public Review rating(Integer rating) {
        this.rating = rating;
        return this;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getFullRating() {
        return fullRating;
    }

    public Review fullRating(Integer fullRating) {
        this.fullRating = fullRating;
        return this;
    }

    public void setFullRating(Integer fullRating) {
        this.fullRating = fullRating;
    }

    public ReviewStatus getReviewStatus() {
        return reviewStatus;
    }

    public Review reviewStatus(ReviewStatus reviewStatus) {
        this.reviewStatus = reviewStatus;
        return this;
    }

    public void setReviewStatus(ReviewStatus reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public Integer getHelpfulVotes() {
        return helpfulVotes;
    }

    public Review helpfulVotes(Integer helpfulVotes) {
        this.helpfulVotes = helpfulVotes;
        return this;
    }

    public void setHelpfulVotes(Integer helpfulVotes) {
        this.helpfulVotes = helpfulVotes;
    }

    public Integer getTotalVotes() {
        return totalVotes;
    }

    public Review totalVotes(Integer totalVotes) {
        this.totalVotes = totalVotes;
        return this;
    }

    public void setTotalVotes(Integer totalVotes) {
        this.totalVotes = totalVotes;
    }

    public Boolean isVerifiedPurchase() {
        return verifiedPurchase;
    }

    public Review verifiedPurchase(Boolean verifiedPurchase) {
        this.verifiedPurchase = verifiedPurchase;
        return this;
    }

    public void setVerifiedPurchase(Boolean verifiedPurchase) {
        this.verifiedPurchase = verifiedPurchase;
    }

    public String getRealName() {
        return realName;
    }

    public Review realName(String realName) {
        this.realName = realName;
        return this;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public ReviewAnalysis getReviewAnalysis() {
        return reviewAnalysis;
    }

    public Review reviewAnalysis(ReviewAnalysis reviewAnalysis) {
        this.reviewAnalysis = reviewAnalysis;
        return this;
    }

    public void setReviewAnalysis(ReviewAnalysis reviewAnalysis) {
        this.reviewAnalysis = reviewAnalysis;
    }

    public Set<Reviewer> getReviewers() {
        return reviewers;
    }

    public Review reviewers(Set<Reviewer> reviewers) {
        this.reviewers = reviewers;
        return this;
    }

    public Review addReviewer(Reviewer reviewer) {
        this.reviewers.add(reviewer);
        reviewer.getReviews().add(this);
        return this;
    }

    public Review removeReviewer(Reviewer reviewer) {
        this.reviewers.remove(reviewer);
        reviewer.getReviews().remove(this);
        return this;
    }

    public void setReviewers(Set<Reviewer> reviewers) {
        this.reviewers = reviewers;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public Review products(Set<Product> products) {
        this.products = products;
        return this;
    }

    public Review addProduct(Product product) {
        this.products.add(product);
        product.getReviews().add(this);
        return this;
    }

    public Review removeProduct(Product product) {
        this.products.remove(product);
        product.getReviews().remove(this);
        return this;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
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
        Review review = (Review) o;
        if (review.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), review.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Review{" +
            "id=" + getId() +
            ", productID='" + getProductID() + "'" +
            ", uid='" + getUid() + "'" +
            ", productURL='" + getProductURL() + "'" +
            ", title='" + getTitle() + "'" +
            ", reviewContent='" + getReviewContent() + "'" +
            ", language='" + getLanguage() + "'" +
            ", reviewDateTime='" + getReviewDateTime() + "'" +
            ", rating=" + getRating() +
            ", fullRating=" + getFullRating() +
            ", reviewStatus='" + getReviewStatus() + "'" +
            ", helpfulVotes=" + getHelpfulVotes() +
            ", totalVotes=" + getTotalVotes() +
            ", verifiedPurchase='" + isVerifiedPurchase() + "'" +
            ", realName='" + getRealName() + "'" +
            "}";
    }
}
