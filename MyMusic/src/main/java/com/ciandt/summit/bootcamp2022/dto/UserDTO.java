package com.ciandt.summit.bootcamp2022.dto;

import com.ciandt.summit.bootcamp2022.entity.User;
import com.ciandt.summit.bootcamp2022.entity.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {

    private String id;
    private String name;
    private UserType userType;

    public UserDTO(User entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.userType = entity.getUserType();
    }
}
