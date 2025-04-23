package com.repsy.repsyapi.storage.filesystem;

import com.repsy.repsyapi.storage.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service("fileSystemStorage")
public class FileSystemStorageService implements StorageService {

    @Value("${file.storage.path:/tmp/repsy}")
    private String basePath;

    @Override
    public void save(String packageName, String version, MultipartFile file) throws IOException {
        Path packagePath = Paths.get(basePath, packageName, version);
        Files.createDirectories(packagePath);
        Path filePath = packagePath.resolve(file.getOriginalFilename());
        file.transferTo(filePath);
    }

    @Override
    public byte[] load(String packageName, String version, String fileName) throws IOException {
        Path filePath = Paths.get(basePath, packageName, version, fileName);
        return Files.readAllBytes(filePath);
    }
}