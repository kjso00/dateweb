package com.ogiraffers.profile_bella.service;

import com.ogiraffers.profile_bella.model.dto.ProfileDTO;
import com.ogiraffers.profile_bella.model.entity.ProfileEntity;
import com.ogiraffers.profile_bella.model.entity.ProfileFileEntity;
import com.ogiraffers.profile_bella.repository.ProfileFileRepository;
import com.ogiraffers.profile_bella.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProfileService {

    @Autowired
    public ProfileRepository profileRepository;

    @Autowired
    public ProfileFileRepository profileFileRepository;

    public ProfileDTO save(ProfileDTO profileDTO) throws IOException {
        ProfileEntity profileEntity = toSaveFileEntity(profileDTO);

        MultipartFile profileFile = profileDTO.getProfileFile();
        if (profileFile != null && !profileFile.isEmpty()) {
            String originalFilename = profileFile.getOriginalFilename();
            String storedFilename = System.currentTimeMillis() + "_" + originalFilename;
            String savePath = "C:/lovematch_img/" + storedFilename;

            // 파일 저장
            profileFile.transferTo(new File(savePath));

            // ProfileEntity에 storedFileName 설정
            profileEntity.setStoredFileName(storedFilename);

            // ProfileFileEntity 생성 및 저장
            ProfileFileEntity profileFileEntity = new ProfileFileEntity();
            profileFileEntity.setOriginalFileName(originalFilename);
            profileFileEntity.setStoredFileName(storedFilename);
            profileFileEntity.setProfileEntity(profileEntity);

            profileEntity.getProfileEntities().add(profileFileEntity);
        }

        profileEntity = profileRepository.save(profileEntity);
        return findById(profileEntity.getId());
    }

    public ProfileEntity toSaveFileEntity(ProfileDTO profileDTO){
        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setProfileName(profileDTO.getProfileName());
        profileEntity.setProfilePassword(profileDTO.getProfilePassword());
        profileEntity.setProfileGender(profileDTO.getProfileGender());
        profileEntity.setProfileAge(profileDTO.getProfileAge());
        profileEntity.setProfileHeight(profileDTO.getProfileHeight());
        profileEntity.setProfileMBTI(profileDTO.getProfileMBTI());
        profileEntity.setProfileLocation(profileDTO.getProfileLocation());
        profileEntity.setTotalScore(0);
        return profileEntity;
    }

    public ProfileFileEntity toProfileFileEntity(ProfileEntity profileEntity, String originalFilename, String storedFilename) {
        ProfileFileEntity profileFileEntity = new ProfileFileEntity();
        profileFileEntity.setOriginalFileName(originalFilename);
        profileFileEntity.setStoredFileName(storedFilename);
        profileFileEntity.setProfileEntity(profileEntity);
        return profileFileEntity;
    }

    public ProfileDTO update(ProfileDTO profileDTO) {
        ProfileEntity profileEntity = toUpdateEntity(profileDTO);
        profileRepository.save(profileEntity);
        return findById(profileEntity.getId());
    }

        public List<ProfileDTO> findAll() {
            List<ProfileEntity> profiles = profileRepository.findAll();
            return profiles.stream()
                    .map(this::convertToProfileDTO)
                    .collect(Collectors.toList());
        }

    private ProfileDTO convertToProfileDTO(ProfileEntity profile) {
        ProfileDTO dto = new ProfileDTO();

        dto.setId(profile.getId());
        dto.setProfileName(profile.getProfileName());
        dto.setProfileGender(profile.getProfileGender());
        dto.setStoredFileName(profile.getStoredFileName());

        // 프로필 이미지 정보 설정
        if (!profile.getProfileEntities().isEmpty()) {
            ProfileFileEntity fileEntity = profile.getProfileEntities().get(0);
            dto.setStoredFileName(fileEntity.getStoredFileName());
        }

        return dto;
    }

    public ProfileDTO findById(Long id) {
        Optional<ProfileEntity> profileEntityOptional = profileRepository.findById(id);
        if(profileEntityOptional.isPresent()) {
            ProfileEntity profileEntity = profileEntityOptional.get();
            ProfileDTO profileDTO = toProfileDTO(profileEntity);

            if (!profileEntity.getProfileEntities().isEmpty()) {
                ProfileFileEntity fileEntity = profileEntity.getProfileEntities().get(0);
                profileDTO.setOriginalFileName(fileEntity.getOriginalFileName());
                profileDTO.setStoredFileName(fileEntity.getStoredFileName());
            }

            return profileDTO;
        } else {
            return null;
        }
    }

    public ProfileDTO toProfileDTO(ProfileEntity profileEntity) {
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setId(profileEntity.getId());
        profileDTO.setProfileName(profileEntity.getProfileName());
        profileDTO.setProfilePassword(profileEntity.getProfilePassword());
        profileDTO.setProfileGender(profileEntity.getProfileGender());
        profileDTO.setProfileAge(profileEntity.getProfileAge());
        profileDTO.setProfileHeight(profileEntity.getProfileHeight());
        profileDTO.setProfileMBTI(profileEntity.getProfileMBTI());
        profileDTO.setProfileLocation(profileEntity.getProfileLocation());
        profileDTO.setTotalScore(profileEntity.getTotalScore());
        return profileDTO;
    }

    public ProfileEntity toUpdateEntity(ProfileDTO profileDTO) {
        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setId(profileDTO.getId());
        profileEntity.setProfileName(profileDTO.getProfileName());
        profileEntity.setProfilePassword(profileDTO.getProfilePassword());
        profileEntity.setProfileGender(profileDTO.getProfileGender());
        profileEntity.setProfileAge(profileDTO.getProfileAge());
        profileEntity.setProfileHeight(profileDTO.getProfileHeight());
        profileEntity.setProfileMBTI(profileDTO.getProfileMBTI());
        profileEntity.setProfileLocation(profileDTO.getProfileLocation());
        profileEntity.setTotalScore(profileDTO.getTotalScore());
        return profileEntity;
    }
}
