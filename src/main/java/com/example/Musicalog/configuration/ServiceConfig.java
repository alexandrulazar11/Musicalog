package com.example.Musicalog.configuration;

import com.example.Musicalog.controller.AlbumController;
import com.example.Musicalog.repository.AlbumRepository;
import com.example.Musicalog.service.AlbumService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {
    @Bean
    public AlbumService albumService(AlbumRepository albumRepository) {
        return new AlbumService(albumRepository);
    }

    @Bean
    public AlbumController albumController(AlbumService albumService) {
        return new AlbumController(albumService);
    }
}
