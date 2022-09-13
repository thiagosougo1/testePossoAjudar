package com.ciandt.summit.bootcamp2022.services;

import com.ciandt.summit.bootcamp2022.dto.UserDTO;
import com.ciandt.summit.bootcamp2022.dto.UsernameDto;

public interface UserService {

    UserDTO getUserById(String id, UsernameDto usernameDto);
}
