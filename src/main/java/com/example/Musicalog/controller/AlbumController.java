package com.example.Musicalog.controller;

import com.example.Musicalog.domain.Album;
import com.example.Musicalog.service.AlbumService;
import com.example.Musicalog.service.exception.NotFoundException;
import com.example.Musicalog.service.exception.ServiceException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/album")
public class AlbumController {

    private final AlbumService service;

    public AlbumController(AlbumService albumService) {
        this.service = albumService;
    }

    @GetMapping("/all")
    public Flux<Album> getAllAlbums() {
        return service.findAll();
    }

    @GetMapping("/all/title")
    public Flux<Album> getAllAlbumsByTitle(@RequestParam String title) {
        return service.findAllByTitle(title);
    }

    @GetMapping("/all/artist")
    public Flux<Album> getAllAlbumsByArtistName(@RequestParam String artistName) {
        return service.findAllByArtistName(artistName);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Album>> getAlbumById(@PathVariable String id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .onErrorResume(NotFoundException.class, e ->
                        Mono.just(ResponseEntity.notFound().build()));
    }

    @PostMapping("/save")
    public Mono<ResponseEntity<Album>> saveAlbum(@RequestBody Album album) {
        return service.save(album)
                .map(ResponseEntity::ok)
                .onErrorResume(ServiceException.class, e ->
                        Mono.just(ResponseEntity.internalServerError().build()));
    }

    @PutMapping("/update")
    public Mono<ResponseEntity<Album>> updateAlbum(@RequestBody Album album) {
        return service.update(album)
                .map(ResponseEntity::ok)
                .onErrorResume(NotFoundException.class, e ->
                        Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/{id}")
    public Mono<Void> removeAlbum(@PathVariable String id) {
        return service.remove(id);
    }
}
