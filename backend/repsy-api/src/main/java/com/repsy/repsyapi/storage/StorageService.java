package com.repsy.repsyapi.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {
    void save(String packageName, String version, MultipartFile file) throws IOException;

    byte[] load(String packageName, String version, String fileName) throws IOException;
}