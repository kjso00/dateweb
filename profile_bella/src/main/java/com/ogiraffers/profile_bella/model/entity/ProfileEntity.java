package com.ogiraffers.profile_bella.model.entity;

import com.ogiraffers.profile_bella.model.dto.ProfileDTO;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "profile_table")
public class ProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @Column(name = "profileName")
    public String profileName;

    @Column(name = "profilePassword")
    public String profilePassword;

    @Column(name = "profileGender")
    public String profileGender;

    @Column(name = "profileAge")
    public int profileAge;

    @Column(name = "profileHeight")
    public int profileHeight;

    @Column(name = "profileMBTI")
    public String profileMBTI;

    @Column(name = "profileLocation")
    public String profileLocation;

    @Column(name = "totalScore")
    public int totalScore;

    @Column(name = "storedFileName")
    private String storedFileName;

    @OneToMany(mappedBy = "profileEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch=FetchType.LAZY)
    public List<ProfileFileEntity> profileEntities = new ArrayList<>();

    public ProfileEntity() {
    }

    public ProfileEntity(Long id, String profileName, String profilePassword, String profileGender, int profileAge, int profileHeight, String profileMBTI, String profileLocation, int totalScore, String storedFileName, List<ProfileFileEntity> profileEntities) {
        this.id = id;
        this.profileName = profileName;
        this.profilePassword = profilePassword;
        this.profileGender = profileGender;
        this.profileAge = profileAge;
        this.profileHeight = profileHeight;
        this.profileMBTI = profileMBTI;
        this.profileLocation = profileLocation;
        this.totalScore = totalScore;
        this.profileEntities = profileEntities;
        this.storedFileName = storedFileName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getProfilePassword() {
        return profilePassword;
    }

    public void setProfilePassword(String profilePassword) {
        this.profilePassword = profilePassword;
    }

    public String getProfileGender() {
        return profileGender;
    }

    public void setProfileGender(String profileGender) {
        this.profileGender = profileGender;
    }

    public int getProfileAge() {
        return profileAge;
    }

    public void setProfileAge(int profileAge) {
        this.profileAge = profileAge;
    }

    public int getProfileHeight() {
        return profileHeight;
    }

    public void setProfileHeight(int profileHeight) {
        this.profileHeight = profileHeight;
    }

    public String getProfileMBTI() {
        return profileMBTI;
    }

    public void setProfileMBTI(String profileMBTI) {
        this.profileMBTI = profileMBTI;
    }

    public String getProfileLocation() {
        return profileLocation;
    }

    public void setProfileLocation(String profileLocation) {
        this.profileLocation = profileLocation;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }
    public String getStoredFileName() {
        return storedFileName;
    }

    public void setStoredFileName(String storedFileName) {
        this.storedFileName = storedFileName;
    }

    public List<ProfileFileEntity> getProfileEntities() {
        return profileEntities;
    }

    public void setProfileEntities(List<ProfileFileEntity> profileEntities) {
        this.profileEntities = profileEntities;
    }



    @Override
    public String toString() {
        return "ProfileEntity{" +
                "id=" + id +
                ", profileName='" + profileName + '\'' +
                ", profilePassword='" + profilePassword + '\'' +
                ", profileGender='" + profileGender + '\'' +
                ", profileAge=" + profileAge +
                ", profileHeight=" + profileHeight +
                ", profileMBTI='" + profileMBTI + '\'' +
                ", profileLocation='" + profileLocation + '\'' +
                ", totalScore=" + totalScore +
                ", profileEntities=" + profileEntities +
                ", storedFileName='" + storedFileName + '\'' +
                '}';
    }
}
