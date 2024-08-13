package com.ogiraffers.profile_bella.repository;

import com.ogiraffers.profile_bella.model.entity.ProfileEntity;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {
}
