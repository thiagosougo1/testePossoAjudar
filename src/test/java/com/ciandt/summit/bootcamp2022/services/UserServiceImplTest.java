package com.ciandt.summit.bootcamp2022.services;

import com.ciandt.summit.bootcamp2022.dto.Data;
import com.ciandt.summit.bootcamp2022.dto.UserDTO;
import com.ciandt.summit.bootcamp2022.dto.UsernameDto;
import com.ciandt.summit.bootcamp2022.entity.*;
import com.ciandt.summit.bootcamp2022.repositories.UserRepository;
import com.ciandt.summit.bootcamp2022.services.exceptions.ResourceNotFoundException;
import com.ciandt.summit.bootcamp2022.utils.TokenService;
import com.ciandt.summit.bootcamp2022.utils.exceptions.InvalidLogDataException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl serviceImpl;

    @Mock
    private TokenService tokenService;

    @Mock
    private UserRepository userRepository;

    private UsernameDto usernameDto;
    private String userExistsId;
    private String userNoExistsId;
    private Playlist playlist;
    private Artist artist;
    private UserDTO userDTO;

    private UserType userType;
    private User user;
    private List<Music> musicList = new ArrayList<>();
    private List<User> usersList = new ArrayList<>();
    private List<Playlist> playlistMusic = new ArrayList<>();
    private List<Music> musicList2 = new ArrayList<>();

    @BeforeEach
    void setUp() throws Exception {
        usernameDto = new UsernameDto(new Data("joao",
                "ZIIKXbvDLcs30v/7nzGxxwRHW6AHBEp94vEtSCFGZqK8ojfKYv39J92PI5Tw9EIHZLhtGJUaY2KZHwysFlfWvA=="));
        userExistsId = "a39926f4-6acb-4497-884f-d4e5296ef652";
        userNoExistsId = "a39926f4-6acb-4497-884f-d4e529";
        playlist = new Playlist("fe5c979a-469b-4c4b-ab5e-64f72f653ea6", musicList, user);
        user = new User("fe5c979a-469b-4c4b-ab5e-64f72f653ea5", "joao", playlist, userType );
        userType = new UserType("1a2c3461-27f8-4976-afa6-8b5e51c024e4", "Comum", usersList);
        userDTO = new UserDTO("fe5c979a-469b-4c4b-ab5e-64f72f653ea5", "joao", userType );
        Mockito.doNothing().when(tokenService).validateToken(usernameDto);
    }

    @Test
    void shouldReturn400WhenUserNotExist() {
        Mockito.when(userRepository.getById(userNoExistsId)).thenThrow(ResourceNotFoundException.class);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            serviceImpl.getUserById(userNoExistsId, usernameDto);
        });
    }

    @Test
    void shouldReturn401WhenTokenIsNotValid() {
        Mockito.doThrow(InvalidLogDataException.class).when(tokenService).validateToken(usernameDto);
        Mockito.when(userRepository.getById(userExistsId)).thenReturn(user);
        Assertions.assertThrows(InvalidLogDataException.class, () -> {
            serviceImpl.getUserById(userExistsId, usernameDto);
        });
    }

    @Test
    void shouldReturn200WhenUserExists() {
        Mockito.when(userRepository.getById(userExistsId)).thenReturn(user);
        Assertions.assertDoesNotThrow(() -> {
            serviceImpl.getUserById(userExistsId, usernameDto);
        });
    }

}
