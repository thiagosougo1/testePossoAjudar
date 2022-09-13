package com.ciandt.summit.bootcamp2022.services;

import com.ciandt.summit.bootcamp2022.dto.MusicDto;
import com.ciandt.summit.bootcamp2022.dto.UserDTO;
import com.ciandt.summit.bootcamp2022.dto.UsernameDto;
import com.ciandt.summit.bootcamp2022.entity.Music;
import com.ciandt.summit.bootcamp2022.entity.Playlist;
import com.ciandt.summit.bootcamp2022.entity.User;
import com.ciandt.summit.bootcamp2022.repositories.MusicRepository;
import com.ciandt.summit.bootcamp2022.repositories.PlaylistRepository;
import com.ciandt.summit.bootcamp2022.repositories.UserRepository;
import com.ciandt.summit.bootcamp2022.services.exceptions.CommomUserException;
import com.ciandt.summit.bootcamp2022.services.exceptions.MusicAlreadyExistException;
import com.ciandt.summit.bootcamp2022.services.exceptions.ResourceNotFoundException;
import com.ciandt.summit.bootcamp2022.utils.TokenService;
import com.ciandt.summit.bootcamp2022.utils.exceptions.InvalidLogDataException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class PlaylistServiceImpl implements PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private MusicRepository musicRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    private static Logger logger = LogManager.getLogger(PlaylistServiceImpl.class);

    @Transactional
    @Override
    public void saveMusicToPlaylistCheckingUserTpe(String playlistId, String userId, MusicDto musicDto, UsernameDto usernameDto){
        try{
            tokenService.validateToken(usernameDto);
            User user = userRepository.getById(userId);
            if(user == null){
                throw new ResourceNotFoundException("User does not existent in the database");
            }

            Playlist playlist = user.getPlaylist();
            if(!playlist.getId().equals(playlistId)){
                throw new ResourceNotFoundException("This playlist does not belongs to this user");
            }

            Music music = musicRepository.getById(musicDto.getId());
            if(user.getUserType().getDescription().equals("Premium") || playlist.getMusicList().size() < 5){
                for (Music musicFind : playlist.getMusicList()) {
                    if (musicFind.getId() == music.getId()){
                        logger.error("Music Already exist in this playlist.");
                        throw new MusicAlreadyExistException("Music Already exist in this playlist.");
                    }
                }
                playlist.getMusicList().add(music);
                music.getPlaylist().add(playlist);
                playlistRepository.save(playlist);
                logger.info("Music add in the playlist");
            }  else {
                throw new CommomUserException("You have reached the maximum number of songs in your playlist. To add more songs, subscribe to the premium plan.");
            }
        } catch (EntityNotFoundException e) {
            logger.error("This Id not exist in database");
            throw new ResourceNotFoundException("this Id not exist in database");
        } catch (InvalidLogDataException e ) {
            logger.error("Invalid Token name");
            throw new InvalidLogDataException(e.getMessage());
        }
    }

    @Transactional
    @Override
    public void deleteMusicFromPlaylist(String playlistId, String musicId, UsernameDto usernameDto) {
        try {
            tokenService.validateToken(usernameDto);
            Optional<Playlist> playlist = playlistRepository.findById(playlistId);
            String music = playlistRepository.findMusicByPlaylist(playlistId, musicId);
            if (playlist.isEmpty()) {
                throw new ResourceNotFoundException("Playlist not exits!");
            } else if (music == null) {
                throw new ResourceNotFoundException("Music doesn't exist in this playlist");
            }
            playlistRepository.deleteMusicFromPlaylist(playlistId, musicId);
            logger.info("Music was excluded from playlist");
        } catch (InvalidLogDataException e ) {
            logger.error("Invalid Token name");
            throw new InvalidLogDataException(e.getMessage());
        }
    }


}
