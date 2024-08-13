package com.ogiraffers.profile_bella.model.entity;

import jakarta.persistence.*;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "profile_file_table")
public class ProfileFileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column
    public String originalFileName;

    @Column
    public String storedFileName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name= "profile_id")
    public ProfileEntity profileEntity;

    public ProfileFileEntity() {
    }

    public ProfileFileEntity(Long id, String originalFileName, String storedFileName, ProfileEntity profileEntity) {
        this.id = id;
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.profileEntity = profileEntity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getStoredFileName() {
        return storedFileName;
    }

    public void setStoredFileName(String storedFileName) {
        this.storedFileName = storedFileName;
    }

    public ProfileEntity getProfileEntity() {
        return profileEntity;
    }

    public void setProfileEntity(ProfileEntity profileEntity) {
        this.profileEntity = profileEntity;
    }

    @Override
    public String toString() {
        return "ProfileFileEntity{" +
                "id=" + id +
                ", originalFileName='" + originalFileName + '\'' +
                ", storedFileName='" + storedFileName + '\'' +
                ", profileEntity=" + profileEntity +
                '}';
    }

}
