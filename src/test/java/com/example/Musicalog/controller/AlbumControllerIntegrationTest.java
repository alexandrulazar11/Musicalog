package com.example.Musicalog.controller;

import com.example.Musicalog.domain.Album;
import com.example.Musicalog.domain.MediaType;
import com.example.Musicalog.repository.AlbumRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureWebTestClient
public class AlbumControllerIntegrationTest {

    private static final String  ID1     = "AlbumId";
    private static final String  ID2     = "AlbumId2";
    private static final String  TITLE1  = "Album Title Rock";
    private static final String  TITLE2  = "Album Title Pop";
    private static final String  TITLE3  = "Album Title Folk";
    private static final String  ARTIST1 = "Album Artist Rock";
    private static final String  ARTIST2 = "Album Artist Pop";
    private static final String  ARTIST3 = "Album Artist Folk";
    private static final Integer STOCK   = 111;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private AlbumRepository albumRepository;

    @BeforeEach
    public void setUp() {
        insertSampleAlbums();
    }

    @Test
    public void testGetAllAlbums() {
        webTestClient.get().uri("/api/album/all")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Album.class)
                .consumeWith(response -> {
                    List<Album> albums = response.getResponseBody();
                    assert albums != null;
                    var album1Found = albums.stream().filter(album -> album.id().equals(ID1)).findAny().orElse(null);
                    var album2Found = albums.stream().filter(album -> album.id().equals(ID2)).findAny().orElse(null);
                    assertThat(album1Found).isNotNull();
                    assertThat(album2Found).isNotNull();
                });
    }

    @Test
    public void testGetAllAlbums_ByTitle() {
        webTestClient.get().uri(uriBuilder -> uriBuilder
                        .path("/api/album/all/title")
                        .queryParam("title", "Rock")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Album.class)
                .consumeWith(response -> {
                    List<Album> albums = response.getResponseBody();
                    assert albums != null;
                    var album1Found = albums.stream().filter(album -> album.id().equals(ID1)).findAny().orElse(null);
                    assertThat(album1Found).isNotNull();
                });
    }

    @Test
    public void testGetAllAlbums_ByTitle_NotFound() {
        webTestClient.get().uri(uriBuilder -> uriBuilder
                        .path("/api/album/all/title")
                        .queryParam("title", "First")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Album.class)
                .consumeWith(response -> {
                    List<Album> albums = response.getResponseBody();
                    assertThat(albums).isEmpty();
                });
    }

    @Test
    public void testGetAllAlbums_ByArtist() {
        webTestClient.get().uri(uriBuilder -> uriBuilder
                        .path("/api/album/all/artist")
                        .queryParam("artistName", "Pop")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Album.class)
                .consumeWith(response -> {
                    List<Album> albums = response.getResponseBody();
                    assert albums != null;
                    var albumFound = albums.stream().filter(album -> album.id().equals(ID2)).findAny().orElse(null);
                    assertThat(albumFound).isNotNull();
                });
    }

    @Test
    public void testGetAllAlbums_ByArtist_NotFound() {
        webTestClient.get().uri(uriBuilder -> uriBuilder
                        .path("/api/album/all/artist")
                        .queryParam("artistName", "First")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Album.class)
                .consumeWith(response -> {
                    List<Album> albums = response.getResponseBody();
                    assertThat(albums).isEmpty();
                });
    }

    @Test
    public void testGetAlbumById() {
        webTestClient.get().uri("/api/album/{id}", ID1)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Album.class)
                .consumeWith(response -> {
                    Album album = response.getResponseBody();
                    assert album != null;
                    assertThat(album.id()).isEqualTo(ID1);
                });
    }

    @Test
    public void testGetAlbumById_NotFound() {
        webTestClient.get().uri("/api/album/{id}", "Album5")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void testUpdateAlbum() {
        webTestClient.put().uri("/api/album/update")
                .bodyValue(new Album(ID2, TITLE3, ARTIST3, MediaType.CD, STOCK, null))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Album.class)
                .consumeWith(response -> {
                    Album album = response.getResponseBody();
                    assert album != null;
                    assertThat(album.id()).isEqualTo(ID2);
                    assertThat(album.title()).isEqualTo(TITLE3);
                    assertThat(album.artistName()).isEqualTo(ARTIST3);
                });
    }

    @Test
    public void testUpdateAlbum_NotFound() {
        webTestClient.put().uri("/api/album/update")
                .bodyValue(new Album("Album5", TITLE3, ARTIST3, MediaType.CD, STOCK, null) )
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void testDeleteAlbum() {
        webTestClient.delete().uri("/api/album/{id}", ID2)
                .exchange()
                .expectStatus().isOk();

        albumRepository.findById(ID2)
                .as(StepVerifier::create)
                .verifyComplete();
    }

    private void insertSampleAlbums() {
        Album album = new Album(ID1, TITLE1, ARTIST1, MediaType.VINYL, STOCK, null);
        Album album2 = new Album(ID2, TITLE2, ARTIST2, MediaType.VINYL, STOCK, null);

        albumRepository.save(album).block();
        albumRepository.save(album2).block();
    }
}
