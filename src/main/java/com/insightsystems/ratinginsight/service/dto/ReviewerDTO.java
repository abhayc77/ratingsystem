package com.insightsystems.ratinginsight.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Reviewer entity.
 */
public class ReviewerDTO implements Serializable {

    private Long id;

    private String reviewerID;

    private String uid;

    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String streetAddress;

    private String postalCode;

    private String city;

    private String stateProvince;

    private Long profileId;

    private Set<ReviewDTO> reviews = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReviewerID() {
        return reviewerID;
    }

    public void setReviewerID(String reviewerID) {
        this.reviewerID = reviewerID;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long reviewerProfileId) {
        this.profileId = reviewerProfileId;
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

        ReviewerDTO reviewerDTO = (ReviewerDTO) o;
        if (reviewerDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reviewerDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReviewerDTO{" +
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
            ", profile=" + getProfileId() +
            "}";
    }
}
