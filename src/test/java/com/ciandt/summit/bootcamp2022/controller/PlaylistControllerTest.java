package com.ciandt.summit.bootcamp2022.controller;

import com.ciandt.summit.bootcamp2022.dto.Data;
import com.ciandt.summit.bootcamp2022.dto.MusicDto;
import com.ciandt.summit.bootcamp2022.dto.UsernameDto;
import com.ciandt.summit.bootcamp2022.entity.*;
import com.ciandt.summit.bootcamp2022.services.PlaylistServiceImpl;
import com.ciandt.summit.bootcamp2022.services.exceptions.CommomUserException;
import com.ciandt.summit.bootcamp2022.services.exceptions.MusicAlreadyExistException;
import com.ciandt.summit.bootcamp2022.services.exceptions.ResourceNotFoundException;

import com.ciandt.summit.bootcamp2022.utils.exceptions.InvalidLogDataException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.ResultActions;

import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static javax.servlet.http.HttpServletResponse.*;

import static org.mockito.ArgumentMatchers.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import java.util.List;


@WebMvcTest(controllers = PlaylistController.class)
public class PlaylistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlaylistServiceImpl playlistService;

    @Autowired
    private ObjectMapper objectMapper;
    private String playlistExistingId;
    private String playlistNotExistId;
    private String musicExistId;
    private String musicNotExistId;

    private String musicDTONotExistId;
    private Playlist playlist;
    private MusicDto musicDto;
    private UsernameDto usernameDto;
    private Artist artist;
    private List<Music> musicList = new ArrayList<>();
    private String userId;
    private List<Playlist> playlistMusic = new ArrayList<>();
    private List<Music> musicList2 = new ArrayList<>();
    private Music music1;
    private Music music2;
    private Music music3;
    private Music music4;
    private Music music5;
    private UserType userType;
    private User user;
    private List<User> userList;
    private Music musicReturned;


    @BeforeEach
    void setUp() throws Exception {
        playlistExistingId = "a39926f4-6acb-4497-884f-d4e5296ef652";
        playlistNotExistId = "070d9496-ae38-4587-8ca6-2ed9b36fb198";
        musicDTONotExistId = "32844fdd-bb76-4c0a-9627-e34ddc9fd892";
        userType = new UserType("1a2c3461-27f8-4976-afa6-8b5e51c024e4", "Comum", userList);
        user = new User("a39926f4-6acb-4497-884f-d4e5296ef652", "Joao", playlist, userType);
        userId = "a39926f4-6acb-4497-884f-d4e5296ef652";
        playlist = new Playlist(playlistExistingId, musicList, user);
        artist = new Artist("32844fdd-bb76-4c0a-9627-e34ddc9fd892", "The Beatles", musicList2);
        musicDto = new MusicDto("67f5976c-eb1e-404e-8220-2c2a8a23be47", "Hippy Hippy Shake", artist);
        usernameDto = new UsernameDto(new Data("joao",
                "ZIIKXbvDLcs30v/7nzGxxwRHW6AHBEp94vEtSCFGZqK8ojfKYv39J92PI5Tw9EIHZLhtGJUaY2KZHwysFlfWvA=="));
        musicReturned = new Music("67f5976c-eb1e-404e-8220-2c2a8a23be47", "Hippy Hippy Shake", artist, playlistMusic);

        Mockito.doNothing().when(playlistService).saveMusicToPlaylistCheckingUserTpe(playlistExistingId,userId, musicDto,usernameDto);


    }

    @Test
    public void shouldSaveMusicToPlaylistAndReturn201WhenCommomLess5() throws Exception {

        String json = objectMapper.writeValueAsString(musicDto);
        ResultActions result = mockMvc.perform(put("/playlist/{playlistId}/{userId}/music", playlistExistingId, userId)
                       .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("name", "joao")
                .header("token", "ZIIKXbvDLcs30v/7nzGxxwRHW6AHBEp94vEtSCFGZqK8ojfKYv39J92PI5Tw9EIHZLhtGJUaY2KZHwysFlfWvA=="));


        result.andExpect(status().isCreated());

    }

    @Test
    public void shouldSaveMusicToPlaylistAndReturn201WhenPremum() throws Exception {
        user.getUserType().setDescription("Premium");
        Mockito.doNothing().when(playlistService).saveMusicToPlaylistCheckingUserTpe(playlistExistingId, userId, musicDto, usernameDto);

        String json = objectMapper.writeValueAsString(musicDto);
        ResultActions result = mockMvc.perform(put("/playlist/{playlistId}/{userId}/music", playlistExistingId, userId)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .header("name", "joao")
                .header("token", "ZIIKXbvDLcs30v/7nzGxxwRHW6AHBEp94vEtSCFGZqK8ojfKYv39J92PI5Tw9EIHZLhtGJUaY2KZHwysFlfWvA=="));


        result.andExpect(status().isCreated());

    }

    @Test
    public void shouldSaveMusicToPlaylistAndReturn400WhenCommomPlus5() throws Exception {

        Mockito.doThrow(CommomUserException.class).when(playlistService).saveMusicToPlaylistCheckingUserTpe(playlistExistingId, userId, musicDto, usernameDto);
        String json = objectMapper.writeValueAsString(musicDto);
        ResultActions result = mockMvc.perform(put("/playlist/{playlistId}/{userId}/music", playlistExistingId, userId)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .header("name", "joao")
                .header("token", "ZIIKXbvDLcs30v/7nzGxxwRHW6AHBEp94vEtSCFGZqK8ojfKYv39J92PI5Tw9EIHZLhtGJUaY2KZHwysFlfWvA=="));


        result.andExpect(status().isCreated());

    }

    @Test
    public void shouldSaveMusicToPlaylistAndReturn400WhenPlaylistNotExist() throws Exception {
        Mockito.doThrow(ResourceNotFoundException.class).when(playlistService).saveMusicToPlaylistCheckingUserTpe(anyString(), anyString(), any(MusicDto.class), any(usernameDto.getClass()));
        String json = objectMapper.writeValueAsString(musicDto);
        ResultActions result = mockMvc.perform(put("/playlist/{playlistId}/{userId}/music", playlistNotExistId, userId)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .header("name", "joao")
                .header("token", "ZIIKXbvDLcs30v/7nzGxxwRHW6AHBEp94vEtSCFGZqK8ojfKYv39J92PI5Tw9EIHZLhtGJUaY2KZHwysFlfWvA=="));


        result.andExpect(status().isBadRequest());

    }

    @Test
    public void shouldSaveMusicToPlaylistAndReturn400WhenMusicNotExist() throws Exception {
        Mockito.doThrow(ResourceNotFoundException.class).when(playlistService).saveMusicToPlaylistCheckingUserTpe(anyString(), anyString(), any(MusicDto.class), any(usernameDto.getClass()));
        String json = objectMapper.writeValueAsString(musicDto);
        ResultActions result = mockMvc.perform(put("/playlist/{playlistId}/{userId}/music", playlistExistingId, userId)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .header("name", "joao")
                .header("token", "ZIIKXbvDLcs30v/7nzGxxwRHW6AHBEp94vEtSCFGZqK8ojfKYv39J92PI5Tw9EIHZLhtGJUaY2KZHwysFlfWvA=="));


        result.andExpect(status().isBadRequest());

    }

    @Test
    public void shouldSaveMusicToPlaylistAndReturn400WhenMusicAlreadyExistInPlaylist() throws Exception {
        Mockito.doThrow(MusicAlreadyExistException.class).when(playlistService).saveMusicToPlaylistCheckingUserTpe(anyString(), anyString(), any(MusicDto.class), any(usernameDto.getClass()));
        String json = objectMapper.writeValueAsString(musicDto);
        ResultActions result = mockMvc.perform(put("/playlist/{playlistId}/{userId}/music", playlistExistingId, userId)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .header("name", "joao")
                .header("token", "ZIIKXbvDLcs30v/7nzGxxwRHW6AHBEp94vEtSCFGZqK8ojfKYv39J92PI5Tw9EIHZLhtGJUaY2KZHwysFlfWvA=="));


        result.andExpect(status().isBadRequest());

    }

    @Test
    public void shouldSaveMusicToPlaylistAndReturn401() throws Exception {
        usernameDto.setData(new Data("joao", ""));
        Mockito.doThrow(InvalidLogDataException.class).when(playlistService).saveMusicToPlaylistCheckingUserTpe(anyString(), anyString(), any(MusicDto.class), any(usernameDto.getClass()));
        String json = objectMapper.writeValueAsString(musicDto);
        ResultActions result = mockMvc.perform(put("/playlist/{playlistId}/{userId}/music", playlistNotExistId, userId)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .header("name", "joao")
                .header("token", ""));


        result.andExpect(status().isUnauthorized());

    }

    @Test
    public void shouldDeleteMusicFromPlaylistAndReturn401() throws Exception {

        playlistExistingId = "a39926f4-6acb-4497-884f-d4e5296ef652";
        musicExistId = "32844fdd-bb76-4c0a-9627-e34ddc9fd892";


        doThrow(InvalidLogDataException.class).when(playlistService).deleteMusicFromPlaylist(anyString(),anyString(),any(usernameDto.getClass()));
        this.mockMvc.perform(delete("/playlist/{playlistId}/musicas/{musicaId}", "a39926f4-6acb-4497-884f-d4e5296ef652","32844fdd-bb76-4c0a-9627-e34ddc9fd892")
                .header("name", "joao").header("token", "ZIIKXbvDLcs30v/7nzGxxwRHW6AHBEp94vEtSCFGZqK8ojfKYv39J92PI5Tw9EIHZLhtGJUaY2KZHwysFlfWvA==")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().is(SC_UNAUTHORIZED)).andReturn();


    }

    @Test
    public void shouldDeleteMusicFromPlaylistAndReturn200() throws Exception {

        playlistExistingId = "a39926f4-6acb-4497-884f-d4e5296ef652";
        musicExistId = "32844fdd-bb76-4c0a-9627-e34ddc9fd892";

        mockMvc.perform(delete("/playlist/a39926f4-6acb-4497-884f-d4e5296ef652/musicas/32844fdd-bb76-4c0a-9627-e34ddc9fd892")
                .header("name", "joao").header("token", "ZIIKXbvDLcs30v/7nzGxxwRHW6AHBEp94vEtSCFGZqK8ojfKYv39J92PI5Tw9EIHZLhtGJUaY2KZHwysFlfWvA==")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());


    }

    @Test
    public void shouldDeleteMusicFromPlaylistAndReturnWithoutAuthorization401() throws Exception {

        playlistExistingId = "a39926f4-6acb-4497-884f-d4e5296ef652";
        musicExistId = "32844fdd-bb76-4c0a-9627-e34ddc9fd892";
        playlistNotExistId = "070d9496-ae38-4587-8ca6-2ed9b36fb198";

        doThrow(InvalidLogDataException.class).when(playlistService).deleteMusicFromPlaylist(anyString(),anyString(),any(usernameDto.getClass()));
        mockMvc.perform(delete("/playlist/{playlistId}/musicas/{musicaId}",playlistExistingId,musicExistId)
                .header("name", "karin").header("token", "2732847")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(SC_UNAUTHORIZED));


    }

    @Test
    public void shouldDeleteMusicFromPlaylistDontExistAndReturn400() throws Exception {

        playlistExistingId = "a39926f4-6acb-4497-884f-d4e5296ef652";
        musicExistId = "32844fdd-bb76-4c0a-9627-e34ddc9fd892";
        playlistNotExistId = "070d9496-ae38-4587-8ca6-2ed9b36fb198";

        doThrow(ResourceNotFoundException.class).when(playlistService).deleteMusicFromPlaylist(anyString(),anyString(),any(usernameDto.getClass()));
        mockMvc.perform(delete("/playlist/{playlistId}/musicas/{musicaId}",playlistNotExistId,musicExistId)
                .header("name", "joao").header("token", "ZIIKXbvDLcs30v/7nzGxxwRHW6AHBEp94vEtSCFGZqK8ojfKYv39J92PI5Tw9EIHZLhtGJUaY2KZHwysFlfWvA==")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(SC_BAD_REQUEST));


    }

    @Test
    public void shouldDeleteMusicDontExistFromPlaylistAndReturn400() throws Exception {

        playlistExistingId = "a39926f4-6acb-4497-884f-d4e5296ef652";
        musicExistId = "32844fdd-bb76-4c0a-9627-e34ddc9fd892";
        playlistNotExistId = "070d9496-ae38-4587-8ca6-2ed9b36fb198";
        musicNotExistId = "32844fdd-bb76-4c0a-9627-e383yweiuhrfskjfd892";

        doThrow(ResourceNotFoundException.class).when(playlistService).deleteMusicFromPlaylist(anyString(),anyString(),any(usernameDto.getClass()));
        mockMvc.perform(delete("/playlist/{playlistId}/musicas/{musicaId}",playlistExistingId,musicNotExistId)
                .header("name", "joao").header("token", "ZIIKXbvDLcs30v/7nzGxxwRHW6AHBEp94vEtSCFGZqK8ojfKYv39J92PI5Tw9EIHZLhtGJUaY2KZHwysFlfWvA==")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(SC_BAD_REQUEST));


    }

}









