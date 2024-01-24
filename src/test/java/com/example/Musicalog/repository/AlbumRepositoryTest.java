package com.example.Musicalog.repository;

import com.example.Musicalog.domain.Album;
import com.example.Musicalog.domain.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@DataMongoTest
public class AlbumRepositoryTest {

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldFindAll() {
        //given
        Album album = new Album(ID1, TITLE1, ARTIST1, MediaType.VINYL, STOCK, COVER);
        Album album2 = new Album(ID2, TITLE2, ARTIST2, MediaType.VINYL, STOCK, COVER);
        when(repository.findAll()).thenReturn(Flux.just(album, album2));

        //when
        StepVerifier.create(repository.findAll())
                .expectNext(album)
                .expectNext(album2)
                .verifyComplete();
    }

    @Test
    void shouldFindAll_NotFound() {
        //given
        when(repository.findAll()).thenReturn(Flux.empty());

        //when
        StepVerifier.create(repository.findAll())
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
        StepVerifier.create(repository.findByTitleContainingIgnoreCase("album"))
                .expectNext(album)
                .expectNext(album2)
                .verifyComplete();
    }

    @Test
    void shouldFindAllByTitle_NotFound() {
        //given
        when(repository.findByTitleContainingIgnoreCase("album")).thenReturn(Flux.empty());

        //when
        StepVerifier.create(repository.findByTitleContainingIgnoreCase("album"))
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void shouldFindAllByArtistName() {
        //given
        Album album = new Album(ID1, TITLE1, ARTIST1, MediaType.VINYL, STOCK, COVER);
        Album album2 = new Album(ID2, TITLE2, ARTIST2, MediaType.VINYL, STOCK, COVER);
        when(repository.findByTitleContainingIgnoreCase("Art")).thenReturn(Flux.just(album, album2));

        //when
        StepVerifier.create(repository.findByTitleContainingIgnoreCase("Art"))
                .expectNext(album)
                .expectNext(album2)
                .verifyComplete();
    }

    @Test
    void shouldFindAllByArtistName_NotFound() {
        //given
        when(repository.findByTitleContainingIgnoreCase("Art")).thenReturn(Flux.empty());

        //when
        StepVerifier.create(repository.findByTitleContainingIgnoreCase("Art"))
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void shouldFindById() {
        //given
        Album album = new Album(ID1, TITLE1, ARTIST1, MediaType.VINYL, STOCK, COVER);
        when(repository.findById(ID1)).thenReturn(Mono.just(album));

        //when
        StepVerifier.create(repository.findById(ID1))
                .expectNext(album)
                .verifyComplete();
    }

    @Test
    void shouldFindById_NotFound() {
        //given
        when(repository.findById(ID2)).thenReturn(Mono.empty());

        //when
        StepVerifier.create(repository.findById(ID2))
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void shouldSave() {
        //given
        Album album = new Album(ID1, TITLE1, ARTIST1, MediaType.VINYL, STOCK, COVER);
        when(repository.save(album)).thenReturn(Mono.just(album));

        //when
        repository.save(album)
                .as(StepVerifier::create)
                .expectNextMatches(savedAlbum -> savedAlbum.id() != null && savedAlbum.id().equals(ID1))
                .verifyComplete();
    }

    @Test
    void shouldSave_alreadyExists() {
        //given
        Album album = new Album(ID1, TITLE1, ARTIST1, MediaType.VINYL, STOCK, COVER);
        when(repository.save(album)).thenReturn(Mono.just(album));

        //when
        Mono<Album> saveMono = repository.save(album)
                .then(repository.save(album));

        //then
        StepVerifier.create(saveMono)
                .expectNext(album)
                .verifyComplete();
    }

    @Test
    void shouldRemove() {
        //given
        when(repository.deleteById(ID1)).thenReturn(Mono.empty());

        //when
        StepVerifier.create(repository.deleteById(ID1))
                .expectNextCount(0)
                .verifyComplete();
    }
}
