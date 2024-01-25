package com.example.Musicalog.service;

import com.example.Musicalog.service.exception.NotFoundException;
import com.example.Musicalog.domain.Album;
import com.example.Musicalog.repository.AlbumRepository;
import com.example.Musicalog.service.exception.ServiceException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AlbumService {

    private final AlbumRepository repository;

    public AlbumService(AlbumRepository repository) {
        this.repository = repository;
    }

    public Flux<Album> findAll() {
        return repository.findAll();
    }

    public Flux<Album> findAllByTitle(String title) {
        return repository.findByTitleContainingIgnoreCase(title);
    }

    public Flux<Album> findAllByArtistName(String artistName) {
        return repository.findByArtistNameContainingIgnoreCase(artistName);
    }

    public Mono<Album> findById(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Album not found with id: %s when updating".formatted(id))));
    }

    public Mono<Album> save(Album album) {
        return repository.save(album)
                .switchIfEmpty(Mono.error(new ServiceException("Album with id %s could not be saved".formatted(album.id()))));
    }

    public Mono<Album> update(Album album) {
        return repository.findById(album.id())
                .flatMap(existingAlbum -> {
                    var editedAlbum = new Album(
                            existingAlbum.id(),
                            album.title(),
                            album.artistName(),
                            album.type(),
                            album.stock(),
                            album.cover()
                    );
                    return repository.save(editedAlbum);
                })
                .switchIfEmpty(Mono.error(new NotFoundException("Album not found with id: %s when updating".formatted(album.id()))));
    }

    public Mono<Void> remove(String albumId) {
        return repository.deleteById(albumId);
    }
}
