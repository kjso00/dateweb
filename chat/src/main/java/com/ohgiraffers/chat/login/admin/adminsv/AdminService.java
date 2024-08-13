package com.ohgiraffers.chat.login.admin.adminsv;

import com.ohgiraffers.chat.login.admin.UserDTO;
import com.ohgiraffers.chat.login.admin.adminrepo.AdminRepository;
import com.ohgiraffers.chat.login.model.entity.Role;
import com.ohgiraffers.chat.login.model.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {
    private final AdminRepository adminRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public List<UserDTO> allUserList() {
        List<UserEntity> entities = adminRepository.findAll();
        List<UserDTO> users = new ArrayList<>();
        for (UserEntity entity : entities) { // 엔티티리스트에 넣은걸 entity에 하나하나 꺼내서 담음
            Role role = Role.valueOf(entity.getRoleKey());
            UserDTO dto = new UserDTO(entity.getId(), entity.getUserName(), entity.getEmail(), role.getKey());

            System.out.println(entity.getRoleKey());
            System.out.println(entity.getRole());
            System.out.println(Role.valueOf(entity.getRoleKey()));
            users.add(dto);
        }
        return users;
    }

    public UserDTO userDetail(Integer id){
        Optional<UserEntity> entity = adminRepository.findById(id);
        UserDTO dto = null;

        // 엔티티가 존재하면
        if(entity.isPresent()) {
            dto = new UserDTO();
            dto.setId(entity.get().getId());
            dto.setName(entity.get().getUserName());
            dto.setEmail(entity.get().getEmail());
            dto.setRole(entity.get().getRoleKey());
        }
        return dto;
    }
}