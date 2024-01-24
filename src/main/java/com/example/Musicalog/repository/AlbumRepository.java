package com.example.Musicalog.repository;

import com.example.Musicalog.domain.Album;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface AlbumRepository extends ReactiveMongoRepository<Album, String> {

    Flux<Album> findByTitleContainingIgnoreCase(String title);

    Flux<Album> findByArtistNameContainingIgnoreCase(String title);
}
