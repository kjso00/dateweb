package com.ogiraffers.profile_bella.repository;


import com.ogiraffers.profile_bella.model.entity.ProfileFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileFileRepository extends JpaRepository<ProfileFileEntity, Long> {
}
