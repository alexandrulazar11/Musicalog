package com.example.Musicalog.service;

import com.example.Musicalog.domain.Album;
import com.example.Musicalog.domain.MediaType;
import com.example.Musicalog.repository.AlbumRepository;
import com.example.Musicalog.service.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@DataMongoTest
public class AlbumServiceTest {

    private static final String ID1 = "AlbumId";
    private static final String ID2 = "AlbumId2";
    private static final String TITLE1 = "Album Title 1";
    private static final String TITLE2 = "Album Title 2";
    private static final String ARTIST1 = "Album Artist 1";
    private static final String ARTIST2 = "Album Artist 2";
    private static final Integer STOCK = 111;
    private static final byte[] COVER = new byte[10];

    @Mock
    private AlbumRepository repository;

    private AlbumService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new AlbumService(repository);
    }

    @Test
    void shouldFindAll() {
        //given
        Album album = new Album(ID1, TITLE1, ARTIST1, MediaType.VINYL, STOCK, COVER);
        Album album2 = new Album(ID2, TITLE2, ARTIST2, MediaType.VINYL, STOCK, COVER);
        when(repository.findAll()).thenReturn(Flux.just(album, album2));

        //when
        Flux<Album> result = service.findAll();

        //then
        StepVerifier.create(result)
                .expectNext(album)
                .expectNext(album2)
                .verifyComplete();
    }

    @Test
    void shouldFindAll_NotFound() {
        //given
        when(repository.findAll()).thenReturn(Flux.empty());

        //when
        Flux<Album> result = service.findAll();

        //then
        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void shouldFindAllByTitle() {
        //given
        Album album = new Album(ID1, TITLE1, ARTIST1, MediaType.VINYL, STOCK, COVER);
        Album album2 = new Album(ID2, TITLE2, ARTIST2, MediaType.VINYL, STOCK, COVER);
        when(repository.findByTitleContainingIgnoreCase("album")).thenReturn(Flux.just(album, album2));

        //when
        Flux<Album> result = service.findAllByTitle("album");

        //then
        StepVerifier.create(result)
                .expectNext(album)
                .expectNext(album2)
                .verifyComplete();
    }

    @Test
    void shouldFindAllByTitle_NotFound() {
        //given
        when(repository.findByTitleContainingIgnoreCase("album")).thenReturn(Flux.empty());

        //when
        Flux<Album> result = service.findAllByTitle("album");

        //then
        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void shouldFindAllByArtistName() {
        //given
        Album album = new Album(ID1, TITLE1, ARTIST1, MediaType.VINYL, STOCK, COVER);
        Album album2 = new Album(ID2, TITLE2, ARTIST2, MediaType.VINYL, STOCK, COVER);
        when(repository.findByArtistNameContainingIgnoreCase("Art")).thenReturn(Flux.just(album, album2));

        //when
        Flux<Album> result = service.findAllByArtistName("Art");

        //then
        StepVerifier.create(result)
                .expectNext(album)
                .expectNext(album2)
                .verifyComplete();
    }

    @Test
    void shouldFindAllByArtistName_NotFound() {
        //given
        when(repository.findByArtistNameContainingIgnoreCase("Art")).thenReturn(Flux.empty());

        //when
        Flux<Album> result = service.findAllByArtistName("Art");

        //then
        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void shouldFindById() {
        //given
        Album album = new Album(ID1, TITLE1, ARTIST1, MediaType.VINYL, STOCK, COVER);
        when(repository.findById(ID1)).thenReturn(Mono.just(album));

        //when
        Mono<Album> result = service.findById(ID1);

        //then
        StepVerifier.create(result)
                .expectNext(album)
                .verifyComplete();
    }

    @Test
    void shouldFindById_NotFound() {
        //given
        when(repository.findById(ID2)).thenReturn(Mono.empty());

        //when
        Mono<Album> result = service.findById(ID2);

        //then
        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void shouldSave() {
        //given
        Album album = new Album(ID1, TITLE1, ARTIST1, MediaType.VINYL, STOCK, COVER);
        when(repository.save(album)).thenReturn(Mono.just(album));

        //when
        Mono<Album> result = service.save(album);

        //then
        StepVerifier.create(result)
                .expectNext(album)
                .verifyComplete();
    }

    @Test
    void shouldSave_alreadyExists() {
        //given
        Album album = new Album(ID1, TITLE1, ARTIST1, MediaType.VINYL, STOCK, COVER);
        when(repository.save(album)).thenReturn(Mono.just(album));

        //when
        Mono<Album> result = service.save(album)
                .then(service.save(album));

        //then
        StepVerifier.create(result)
                .expectNext(album)
                .verifyComplete();
    }

    @Test
    void shouldUpdate() {
        //given
        Album album = new Album(ID1, TITLE1, ARTIST1, MediaType.VINYL, STOCK, COVER);
        Album album2 = new Album(ID1, TITLE2, ARTIST2, MediaType.VINYL, STOCK, COVER);
        when(repository.findById(ID1)).thenReturn(Mono.just(album));
        when(repository.save(album2)).thenReturn(Mono.just(album2));

        //when
        Mono<Album> result = service.update(album2);

        //then
        StepVerifier.create(result)
                .expectNext(album2)
                .verifyComplete();
    }

    @Test
    void shouldUpdate_NotFound() {
        //given
        Album album2 = new Album(ID2, TITLE2, ARTIST2, MediaType.VINYL, STOCK, COVER);
        when(repository.findById(ID2)).thenReturn(Mono.empty());

        //when
        Mono<Album> result = service.update(album2);

        //then
        StepVerifier.create(result)
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    void shouldRemove() {
        //given
        when(repository.deleteById(ID1)).thenReturn(Mono.empty());

        //when
        Mono<Void> result = service.remove(ID1);

        //when
        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }
}
