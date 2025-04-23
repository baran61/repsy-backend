package com.repsy.repsyapi.config;

import com.repsy.repsyapi.storage.StorageService;
import com.repsy.repsyapi.storage.filesystem.FileSystemStorageService;
import com.repsy.repsyapi.storage.objectstorage.MinIOStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {

    @Bean
    public StorageService storageService(
            @Value("${storage.strategy}") String strategy,
            FileSystemStorageService fileSystemStorageService,
            MinIOStorageService minIOStorageService
    ) {
        return strategy.equals("object-storage")
                ? minIOStorageService
                : fileSystemStorageService;
    }
}