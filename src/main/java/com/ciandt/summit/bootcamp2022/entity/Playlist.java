package com.ciandt.summit.bootcamp2022.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="Playlists")
public class Playlist {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name="Id")
    private String id;

    @ManyToMany
    @JoinTable(
            name = "PlaylistMusicas",
            joinColumns = @JoinColumn(name = "PlaylistId"),
            inverseJoinColumns = @JoinColumn(name = "MusicaId")
    )
    private List<Music> musicList = new ArrayList<>();

    @OneToOne(mappedBy = "playlist")
    private User user;

    public Playlist(List<Music> musicList, User user) {
        this.musicList = musicList;
        this.user = user;
    }
}
