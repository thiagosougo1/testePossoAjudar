package com.ciandt.summit.bootcamp2022.services;

import com.ciandt.summit.bootcamp2022.dto.UserDTO;
import com.ciandt.summit.bootcamp2022.dto.UsernameDto;
import com.ciandt.summit.bootcamp2022.entity.User;
import com.ciandt.summit.bootcamp2022.repositories.UserRepository;
import com.ciandt.summit.bootcamp2022.services.exceptions.ResourceNotFoundException;
import com.ciandt.summit.bootcamp2022.utils.TokenService;
import com.ciandt.summit.bootcamp2022.utils.exceptions.InvalidLogDataException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    private static Logger logger = LogManager.getLogger(PlaylistServiceImpl.class);

    @Transactional
    @Override
    public UserDTO getUserById(String id, UsernameDto usernameDto) {
        try {
            tokenService.validateToken(usernameDto);
            User user = userRepository.getById(id);
            UserDTO dto = new UserDTO(user);
            return dto;
        } catch (InvalidLogDataException e ) {
            logger.error("Invalid Token name");
            throw new InvalidLogDataException(e.getMessage());
        } catch (EntityNotFoundException e) {
            logger.error("User not found!");
            throw new ResourceNotFoundException("User not found!");
        }


    }
}
