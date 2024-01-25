package com.example.Musicalog.controller;

import com.example.Musicalog.configuration.ServiceConfig;
import com.example.Musicalog.domain.Album;
import com.example.Musicalog.domain.MediaType;
import com.example.Musicalog.service.AlbumService;
import com.example.Musicalog.service.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@Import({ ServiceConfig.class })
public class AlbumControllerTest {

    private static final String  ID1     = "AlbumId";
    private static final String  ID2     = "AlbumId2";
    private static final String  TITLE1  = "Album Title 1";
    private static final String  TITLE2  = "Album Title 2";
    private static final String  ARTIST1 = "Album Artist 1";
    private static final String  ARTIST2 = "Album Artist 2";
    private static final Integer STOCK   = 111;
    private static final byte[]  COVER   = new byte[10];

    @MockBean
    private AlbumService service;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.webTestClient = WebTestClient.bindToController(new AlbumController(service)).build();
    }

    @Test
    void shouldGetAllAlbums() {
        //given
        Album album = new Album(ID1, TITLE1, ARTIST1, MediaType.VINYL, STOCK, COVER);
        Album album2 = new Album(ID2, TITLE2, ARTIST2, MediaType.VINYL, STOCK, COVER);
        when(service.findAll()).thenReturn(Flux.just(album, album2));

        //when
        webTestClient.get().uri("/api/album/all")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Album.class)
                .consumeWith(response -> {
                    List<Album> albums = response.getResponseBody();
                    assert albums != null;
                    assertThat(albums.get(0).id()).isEqualTo(album.id());
                    assertThat(albums.get(1).id()).isEqualTo(album2.id());
                });

        //then
        verify(service, times(1)).findAll();
    }

    @Test
    void shouldGetAllAlbums_NotFound() {
        //given
        when(service.findAll()).thenReturn(Flux.empty());

        //when
        webTestClient.get().uri("/api/album/all")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Album.class).isEqualTo(List.of());

        //then
        verify(service, times(1)).findAll();
    }

    @Test
    void shouldGetAllAlbumsByTitle() {
        //given
        Album album = new Album(ID1, TITLE1, ARTIST1, MediaType.VINYL, STOCK, COVER);
        Album album2 = new Album(ID2, TITLE2, ARTIST2, MediaType.VINYL, STOCK, COVER);
        when(service.findAllByTitle("Album")).thenReturn(Flux.just(album, album2));

        //when
        webTestClient.get().uri(uriBuilder -> uriBuilder
                        .path("/api/album/all/title")
                        .queryParam("title", "Album")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Album.class)
                .consumeWith(response -> {
                    List<Album> albums = response.getResponseBody();
                    assert albums != null;
                    assertThat(albums.get(0).id()).isEqualTo(album.id());
                    assertThat(albums.get(1).id()).isEqualTo(album2.id());
                    assertThat(albums.get(0).title()).isEqualTo(album.title());
                    assertThat(albums.get(1).title()).isEqualTo(album2.title());
                });

        //then
        verify(service, times(1)).findAllByTitle("Album");
    }

    @Test
    void shouldGetAllAlbumsByTitle_NotFound() {
        //given
        when(service.findAllByTitle("Album")).thenReturn(Flux.empty());

        //when
        webTestClient.get().uri(uriBuilder -> uriBuilder
                        .path("/api/album/all/title")
                        .queryParam("title", "Album")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Album.class).isEqualTo(List.of());

        //then
        verify(service, times(1)).findAllByTitle("Album");
    }

    @Test
    void shouldGetAllAlbumsByArtistName() {
        //given
        Album album = new Album(ID1, TITLE1, ARTIST1, MediaType.VINYL, STOCK, COVER);
        Album album2 = new Album(ID2, TITLE2, ARTIST2, MediaType.VINYL, STOCK, COVER);
        when(service.findAllByArtistName("Art")).thenReturn(Flux.just(album, album2));

        //when
        webTestClient.get().uri(uriBuilder -> uriBuilder
                        .path("/api/album/all/artist")
                        .queryParam("artistName", "Art")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Album.class)
                .consumeWith(response -> {
                    List<Album> albums = response.getResponseBody();
                    assert albums != null;
                    assertThat(albums.get(0).id()).isEqualTo(album.id());
                    assertThat(albums.get(1).id()).isEqualTo(album2.id());
                    assertThat(albums.get(0).artistName()).isEqualTo(album.artistName());
                    assertThat(albums.get(1).artistName()).isEqualTo(album2.artistName());
                });

        //then
        verify(service, times(1)).findAllByArtistName("Art");
    }

    @Test
    void shouldGetAllAlbumsByArtistName_NotFound() {
        //given
        when(service.findAllByArtistName("Art")).thenReturn(Flux.empty());

        //when
        webTestClient.get().uri(uriBuilder -> uriBuilder
                        .path("/api/album/all/artist")
                        .queryParam("artistName", "Art")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Album.class).isEqualTo(List.of());

        //then
        verify(service, times(1)).findAllByArtistName("Art");
    }

    @Test
    void shouldFindAlbumById() {
        //given
        Album album = new Album(ID1, TITLE1, ARTIST1, MediaType.VINYL, STOCK, COVER);
        when(service.findById(ID1)).thenReturn(Mono.just(album));

        //when
        webTestClient.get().uri("/api/album/{id}", ID1)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Album.class)
                .consumeWith(response -> {
                    Album albumResult = response.getResponseBody();
                    assert albumResult != null;
                    assertThat(albumResult.id()).isEqualTo(album.id());
                    assertThat(albumResult.title()).isEqualTo(album.title());
                    assertThat(albumResult.artistName()).isEqualTo(album.artistName());
                });

        //then
        verify(service, times(1)).findById(ID1);
    }

    @Test
    void shouldFindAlbumById_NotFound() {
        //given
        when(service.findById(ID2)).thenReturn(Mono.empty());

        //when
        webTestClient.get().uri("/api/album/{id}", ID2)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Album.class);

        //then
        verify(service, times(1)).findById(ID2);
    }

    @Test
    void shouldSave() {
        //given
        Album album = new Album(ID1, TITLE1, ARTIST1, MediaType.VINYL, STOCK, COVER);
        when(service.save(any())).thenReturn(Mono.just(album));

        //when
        webTestClient.post().uri("/api/album/save")
                .bodyValue(album)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Album.class)
                .consumeWith(response -> {
                    Album albumResult = response.getResponseBody();
                    assert albumResult != null;
                    assertThat(albumResult.id()).isEqualTo(album.id());
                    assertThat(albumResult.title()).isEqualTo(album.title());
                    assertThat(albumResult.artistName()).isEqualTo(album.artistName());
                });
    }

    @Test
    void shouldSave_alreadyExists() {
        //given
        Album album = new Album(ID1, TITLE1, ARTIST1, MediaType.VINYL, STOCK, COVER);
        when(service.save(any())).thenReturn(Mono.just(album));

        //when
        webTestClient.post().uri("/api/album/save")
                .bodyValue(album)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Album.class)
                .consumeWith(response -> {
                    Album albumResult = response.getResponseBody();
                    assert albumResult != null;
                    assertThat(albumResult.id()).isEqualTo(album.id());
                    assertThat(albumResult.title()).isEqualTo(album.title());
                    assertThat(albumResult.artistName()).isEqualTo(album.artistName());
                });

        //when
        webTestClient.post().uri("/api/album/save")
                .bodyValue(album)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Album.class)
                .consumeWith(response -> {
                    Album albumResult = response.getResponseBody();
                    assert albumResult != null;
                    assertThat(albumResult.id()).isEqualTo(album.id());
                    assertThat(albumResult.title()).isEqualTo(album.title());
                    assertThat(albumResult.artistName()).isEqualTo(album.artistName());
                });

        //then
        verify(service, times(2)).save(any());
    }

    @Test
    void shouldUpdate() {
        //given
        Album album = new Album(ID1, TITLE1, ARTIST1, MediaType.VINYL, STOCK, COVER);
        Album album2 = new Album(ID1, TITLE2, ARTIST2, MediaType.VINYL, STOCK, COVER);
        when(service.findById(ID1)).thenReturn(Mono.just(album));
        when(service.update(any())).thenReturn(Mono.just(album2));

        //when
        webTestClient.put().uri("/api/album/update")
                .bodyValue(album2)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Album.class)
                .consumeWith(response -> {
                    Album albumResult = response.getResponseBody();
                    assert albumResult != null;
                    assertThat(albumResult.id()).isEqualTo(album2.id());
                    assertThat(albumResult.title()).isEqualTo(album2.title());
                    assertThat(albumResult.artistName()).isEqualTo(album2.artistName());
                });

        //then
        verify(service, times(1)).update(any());
    }

    @Test
    void shouldUpdate_NotFound() {
        //given
        Album album2 = new Album(ID2, TITLE2, ARTIST2, MediaType.VINYL, STOCK, COVER);
        when(service.update(any())).thenReturn(Mono.error(new NotFoundException("Album not found")));

        //when
        webTestClient.put().uri("/api/album/update")
                .bodyValue(album2)
                .exchange()
                .expectStatus().isNotFound();

        //then
        verify(service, times(1)).update(any());
    }

    @Test
    void shouldRemove() {
        //given
        when(service.remove(ID1)).thenReturn(Mono.empty());

        //when
        webTestClient.delete().uri("/api/album/{id}", ID1)
                .exchange()
                .expectStatus().isOk();

        //then
        verify(service, times(1)).remove(ID1);
    }
}
