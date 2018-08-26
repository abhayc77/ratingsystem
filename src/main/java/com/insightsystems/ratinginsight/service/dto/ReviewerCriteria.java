package com.insightsystems.ratinginsight.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Reviewer entity. This class is used in ReviewerResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /reviewers?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ReviewerCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter reviewerID;

    private StringFilter uid;

    private StringFilter username;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter email;

    private StringFilter phoneNumber;

    private StringFilter streetAddress;

    private StringFilter postalCode;

    private StringFilter city;

    private StringFilter stateProvince;

    private LongFilter profileId;

    private LongFilter reviewId;

    public ReviewerCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getReviewerID() {
        return reviewerID;
    }

    public void setReviewerID(StringFilter reviewerID) {
        this.reviewerID = reviewerID;
    }

    public StringFilter getUid() {
        return uid;
    }

    public void setUid(StringFilter uid) {
        this.uid = uid;
    }

    public StringFilter getUsername() {
        return username;
    }

    public void setUsername(StringFilter username) {
        this.username = username;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(StringFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public StringFilter getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(StringFilter streetAddress) {
        this.streetAddress = streetAddress;
    }

    public StringFilter getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(StringFilter postalCode) {
        this.postalCode = postalCode;
    }

    public StringFilter getCity() {
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public StringFilter getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(StringFilter stateProvince) {
        this.stateProvince = stateProvince;
    }

    public LongFilter getProfileId() {
        return profileId;
    }

    public void setProfileId(LongFilter profileId) {
        this.profileId = profileId;
    }

    public LongFilter getReviewId() {
        return reviewId;
    }

    public void setReviewId(LongFilter reviewId) {
        this.reviewId = reviewId;
    }

    @Override
    public String toString() {
        return "ReviewerCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (reviewerID != null ? "reviewerID=" + reviewerID + ", " : "") +
                (uid != null ? "uid=" + uid + ", " : "") +
                (username != null ? "username=" + username + ", " : "") +
                (firstName != null ? "firstName=" + firstName + ", " : "") +
                (lastName != null ? "lastName=" + lastName + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (phoneNumber != null ? "phoneNumber=" + phoneNumber + ", " : "") +
                (streetAddress != null ? "streetAddress=" + streetAddress + ", " : "") +
                (postalCode != null ? "postalCode=" + postalCode + ", " : "") +
                (city != null ? "city=" + city + ", " : "") +
                (stateProvince != null ? "stateProvince=" + stateProvince + ", " : "") +
                (profileId != null ? "profileId=" + profileId + ", " : "") +
                (reviewId != null ? "reviewId=" + reviewId + ", " : "") +
            "}";
    }

}
