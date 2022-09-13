package com.ciandt.summit.bootcamp2022.services;

import com.ciandt.summit.bootcamp2022.dto.MusicDto;
import com.ciandt.summit.bootcamp2022.dto.UsernameDto;
import com.ciandt.summit.bootcamp2022.utils.exceptions.InvalidLogDataException;
import org.springframework.transaction.annotation.Transactional;

public interface PlaylistService {

    @Transactional
    void deleteMusicFromPlaylist(String playlistId, String musicId, UsernameDto usernameDto) throws InvalidLogDataException;

    @Transactional
    void saveMusicToPlaylistCheckingUserTpe(String playlistId, String userId, MusicDto musicDto, UsernameDto usernameDto) throws InvalidLogDataException;
}
