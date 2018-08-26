package com.insightsystems.ratinginsight.domain;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * The Reviewer entity.
 */
@ApiModel(description = "The Reviewer entity.")
@Entity
@Table(name = "reviewer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "reviewer")
public class Reviewer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "reviewer_id")
    private String reviewerID;

    @Column(name = "jhi_uid")
    private String uid;

    @Column(name = "username")
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "street_address")
    private String streetAddress;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "city")
    private String city;

    @Column(name = "state_province")
    private String stateProvince;

    @OneToOne
    @JoinColumn(unique = true)
    private ReviewerProfile profile;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "reviewer_review",
               joinColumns = @JoinColumn(name = "reviewers_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "reviews_id", referencedColumnName = "id"))
    private Set<Review> reviews = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReviewerID() {
        return reviewerID;
    }

    public Reviewer reviewerID(String reviewerID) {
        this.reviewerID = reviewerID;
        return this;
    }

    public void setReviewerID(String reviewerID) {
        this.reviewerID = reviewerID;
    }

    public String getUid() {
        return uid;
    }

    public Reviewer uid(String uid) {
        this.uid = uid;
        return this;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public Reviewer username(String username) {
        this.username = username;
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public Reviewer firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Reviewer lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public Reviewer email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Reviewer phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public Reviewer streetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
        return this;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public Reviewer postalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public Reviewer city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public Reviewer stateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
        return this;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    public ReviewerProfile getProfile() {
        return profile;
    }

    public Reviewer profile(ReviewerProfile reviewerProfile) {
        this.profile = reviewerProfile;
        return this;
    }

    public void setProfile(ReviewerProfile reviewerProfile) {
        this.profile = reviewerProfile;
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public Reviewer reviews(Set<Review> reviews) {
        this.reviews = reviews;
        return this;
    }

    public Reviewer addReview(Review review) {
        this.reviews.add(review);
        review.getReviewers().add(this);
        return this;
    }

    public Reviewer removeReview(Review review) {
        this.reviews.remove(review);
        review.getReviewers().remove(this);
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
        Reviewer reviewer = (Reviewer) o;
        if (reviewer.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reviewer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Reviewer{" +
            "id=" + getId() +
            ", reviewerID='" + getReviewerID() + "'" +
            ", uid='" + getUid() + "'" +
            ", username='" + getUsername() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", streetAddress='" + getStreetAddress() + "'" +
            ", postalCode='" + getPostalCode() + "'" +
            ", city='" + getCity() + "'" +
            ", stateProvince='" + getStateProvince() + "'" +
            "}";
    }
}
