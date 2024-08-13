package com.ogiraffers.profile_bella.controller;

import com.ogiraffers.profile_bella.model.dto.ProfileDTO;
import com.ogiraffers.profile_bella.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("/save")
    public String save() {
        return "profile/save";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute ProfileDTO profileDTO, Model model) throws IOException {
        ProfileDTO savedProfile = profileService.save(profileDTO);
        model.addAttribute("profile", savedProfile);
        return "profile/saved";
    }

    @GetMapping("/list") //DB 에서 data 가져와야해서 이때는 Model 객체 사용 해야한다.
    public String findAll(Model model){
        List<ProfileDTO> profileDTOList = profileService.findAll();
        model.addAttribute("profileDTOList", profileDTOList);
        return "profile/list";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model){
        ProfileDTO profileDTO = profileService.findById(id);
        model.addAttribute("profileUpdate", profileDTO);
        return "profile/update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute ProfileDTO profileDTO, Model model){
        ProfileDTO profile = profileService.update(profileDTO);
        model.addAttribute("profile", profile);
        return "profile/saved";
    }


}
