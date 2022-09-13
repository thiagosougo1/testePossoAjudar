package com.ciandt.summit.bootcamp2022.services;

import com.ciandt.summit.bootcamp2022.dto.Data;
import com.ciandt.summit.bootcamp2022.dto.MusicDto;
import com.ciandt.summit.bootcamp2022.dto.UserDTO;
import com.ciandt.summit.bootcamp2022.dto.UsernameDto;
import com.ciandt.summit.bootcamp2022.entity.*;
import com.ciandt.summit.bootcamp2022.repositories.MusicRepository;
import com.ciandt.summit.bootcamp2022.repositories.PlaylistRepository;
import com.ciandt.summit.bootcamp2022.repositories.UserRepository;
import com.ciandt.summit.bootcamp2022.services.exceptions.CommomUserException;
import com.ciandt.summit.bootcamp2022.services.exceptions.MusicAlreadyExistException;
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
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class PlaylistServiceImplTest {

    @InjectMocks
    private PlaylistServiceImpl playlistService;

    @Mock
    private PlaylistRepository playlistRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MusicRepository musicRepository;

    @Mock
    private TokenService tokenService;

    private String playlistExistingId;
    private String playlistNotExistId;
    private UserDTO userDTO;

    private String musicDTONotExistId;
    private Playlist playlist;
    private MusicDto musicDto;
    private UsernameDto usernameDto;
    private Artist artist;
    private List<Music> musicList = new ArrayList<>();
    private List<User> usersList = new ArrayList<>();
    private User user;
    private String userId;
    private List<Playlist> playlistMusic = new ArrayList<>();
    private List<Music> musicList2 = new ArrayList<>();

    private Music musicReturned;
    private Music music1;
    private Music music2;
    private Music music3;
    private Music music4;
    private Music music5;

    private UserType userType;
    @BeforeEach
    void setUp() throws Exception {
        userType = new UserType("1a2c3461-27f8-4976-afa6-8b5e51c024e4", "Comum", usersList);
        user = new User("a39926f4-6acb-4497-884f-d4e5296ef652", "Joao", playlist, userType);
        userId = "a39926f4-6acb-4497-884f-d4e5296ef652";
        playlistExistingId = "a39926f4-6acb-4497-884f-d4e52975642";
        playlistNotExistId = "070d9496-ae38-4587-8ca6-2ed9b36fb198";
        musicDTONotExistId = "32844fdd-bb76-4c0a-9627-e34ddc9fd892";
        playlist = new Playlist(playlistExistingId, musicList, user);
        artist = new Artist("32844fdd-bb76-4c0a-9627-e34ddc9fd892", "The Beatles", musicList2);
        music1 = new Music("c870a14d-3169-40df-b6ec-b0c2e3a9b05f", "XOXO", artist, playlistMusic);
        music2 = new Music("dcfa5554-5377-4bfc-aec1-d2e43a39e909", "Breaking", artist, playlistMusic);
        music3 = new Music("580c3f5f-4c63-4cd4-a63e-5d4dcb44b606", "Another", artist, playlistMusic);
        music4 = new Music("e612c830-2c09-42e1-bfdc-c9b8ee07fb4b", "Flying", artist, playlistMusic);
        music5 = new Music("cb5c07db-2c5b-425c-acc5-6aeaa3195fe1", "Xau", artist, playlistMusic);
        user.setPlaylist(playlist);

        musicDto = new MusicDto("67f5976c-eb1e-404e-8220-2c2a8a23be47", "Hippy Hippy Shake", artist);
        usernameDto = new UsernameDto(new Data("joao",
                "ZIIKXbvDLcs30v/7nzGxxwRHW6AHBEp94vEtSCFGZqK8ojfKYv39J92PI5Tw9EIHZLhtGJUaY2KZHwysFlfWvA=="));
        musicReturned = new Music("67f5976c-eb1e-404e-8220-2c2a8a23be47", "Hippy Hippy Shake", artist, playlistMusic);
        musicReturned = new Music("67f5976c-eb1e-404e-8220-2c2a8a23be47", "Hippy Hippy Shake", artist, playlistMusic);
        Mockito.when(userRepository.getById(userId)).thenReturn(user);
        Mockito.when(playlistRepository.getById(playlistExistingId)).thenReturn(playlist);
        Mockito.when(musicRepository.getById(musicDto.getId())).thenReturn(musicReturned);
        Mockito.when(tokenService.createToken(usernameDto)).thenReturn("ZIIKXbvDLcs30v/7nzGxxwRHW6AHBEp94vEtSCFGZqK8ojfKYv39J92PI5Tw9EIHZLhtGJUaY2KZHwysFlfWvA==");
        Mockito.doNothing().when(tokenService).validateToken(usernameDto);

    }

    @Test
    public void shuouldSaveMusicToPlaylistWhenCommomLess5() {
        Assertions.assertDoesNotThrow(() -> {
            playlistService.saveMusicToPlaylistCheckingUserTpe(playlist.getId(),userId, musicDto, usernameDto);
        });
    }

    @Test
    public void shuouldSaveMusicToPlaylistWhenIsPremium() {
        userType.setDescription("Premium");
        playlist.getMusicList().add(music1);
        playlist.getMusicList().add(music2);
        playlist.getMusicList().add(music3);
        playlist.getMusicList().add(music4);
        playlist.getMusicList().add(music5);

        Assertions.assertDoesNotThrow(() -> {
            playlistService.saveMusicToPlaylistCheckingUserTpe(playlist.getId(),userId, musicDto, usernameDto);
        });
    }

    @Test
    public void shuouldReturn400WhenICommomPLus5() {
        playlist.getMusicList().add(music1);
        playlist.getMusicList().add(music2);
        playlist.getMusicList().add(music3);
        playlist.getMusicList().add(music4);
        playlist.getMusicList().add(music5);

        Assertions.assertThrows(CommomUserException.class, () -> {
            playlistService.saveMusicToPlaylistCheckingUserTpe(playlistExistingId, userId, musicDto, usernameDto);
        });
    }
    @Test
    public void shouldRertunNotFoundWhenNotExistsPlaylistId() {
        playlist = new Playlist(playlistNotExistId, musicList, user);
        Mockito.when(playlistRepository.getById(playlistNotExistId)).thenThrow(ResourceNotFoundException.class);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            playlistService.saveMusicToPlaylistCheckingUserTpe(playlistNotExistId, userId, musicDto, usernameDto);
        });
    }

    @Test
    public void shouldRertunNotFoundWhenNotExistsMusicDtoId() {
        musicDto.setId("32844fdd-bb76-4c0a-9627-e34ddc9fd892");
        Mockito.when(musicRepository.getById(musicDto.getId())).thenThrow(ResourceNotFoundException.class);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            playlistService.saveMusicToPlaylistCheckingUserTpe(playlistNotExistId, userId, musicDto, usernameDto);
        });
    }

    @Test
    public void ShouldReturnInvalidLogDataExceptionWhenInvalidToken() {
        usernameDto.getData().setToken("");
        Mockito.doThrow(InvalidLogDataException.class).when(tokenService).validateToken(usernameDto);

        Assertions.assertThrows(InvalidLogDataException.class, () -> {
            playlistService.saveMusicToPlaylistCheckingUserTpe(playlistNotExistId, userId, musicDto, usernameDto);
        });
    }

    @Test
    public void ShouldReturnMusicAlreadyExistExceptionWhenMusicIsAlreadyInPlaylist() {
        playlist.getMusicList().add(musicReturned);

        Assertions.assertThrows(MusicAlreadyExistException.class, () -> {
            playlistService.saveMusicToPlaylistCheckingUserTpe(playlistExistingId, userId, musicDto, usernameDto);
        });

        Mockito.verify(musicRepository, Mockito.times(1)).getById(musicDto.getId());
    }

    @Test
    public void deleteMusicWithSuccess() {
        Mockito.doReturn(Optional.ofNullable(playlist))
                .when(playlistRepository)
                .findById(Mockito.anyString());
        Mockito.doReturn(musicReturned.getId())
                .when(playlistRepository)
                .findMusicByPlaylist(Mockito.anyString(), Mockito.anyString());

        Assertions.assertDoesNotThrow(() -> {
            playlistService.deleteMusicFromPlaylist(playlistExistingId, musicDto.getId(), usernameDto);
        });
    }

    @Test
    public void shouldThrowErrorWhenPlaylistIsEmpty() {

        Mockito.doReturn(musicReturned.getId())
                .when(playlistRepository)
                .findMusicByPlaylist(Mockito.anyString(), Mockito.anyString());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            playlistService.deleteMusicFromPlaylist(playlistNotExistId, musicDto.getId(), usernameDto);
        });
    }

    @Test
    public void shouldThrowErrorWhenMusicIsNull() {

        Mockito.doReturn(Optional.ofNullable(playlist))
                .when(playlistRepository)
                .findById(Mockito.anyString());
        Mockito.doReturn(null)
                .when(playlistRepository)
                .findMusicByPlaylist(Mockito.anyString(), Mockito.anyString());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            playlistService.deleteMusicFromPlaylist(playlistExistingId, musicDto.getId(), usernameDto);
        });

    }

    @Test
    public void shoudlThrowInvalidLogDataToken() {
        Mockito.doThrow(InvalidLogDataException.class).when(tokenService).validateToken(Mockito.any(UsernameDto.class));

        Assertions.assertThrows(InvalidLogDataException.class, () -> {
            playlistService.saveMusicToPlaylistCheckingUserTpe(playlistNotExistId, userId, musicDto, usernameDto);
        });
    }
}
