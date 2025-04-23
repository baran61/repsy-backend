package com.repsy.repsyapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.repsy.repsyapi.model.MetaData;
import com.repsy.repsyapi.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/")
public class UploadController {

    private final StorageService storageService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public UploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("{packageName}/{version}")
    public ResponseEntity<String> uploadPackage(
            @PathVariable String packageName,
            @PathVariable String version,
            @RequestParam("package") MultipartFile packageFile,
            @RequestParam("meta") MultipartFile metaFile
    ) {
        if (!packageFile.getOriginalFilename().endsWith(".rep")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid package file. Must be .rep");
        }

        try {
            // meta.json'ı parse et
            MetaData metaData = objectMapper.readValue(metaFile.getBytes(), MetaData.class);

            // .rep dosyasını kaydet
            storageService.save(packageName, version, packageFile);

            // meta.json'u da dosya olarak kaydet
            storageService.save(packageName, version, metaFile);

            return ResponseEntity.ok("Package uploaded successfully");

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid file format or upload failed");
        }
    }
}