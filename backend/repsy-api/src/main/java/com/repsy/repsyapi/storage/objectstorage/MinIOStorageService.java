package com.repsy.repsyapi.storage.objectstorage;

import com.repsy.repsyapi.storage.StorageService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.GetObjectArgs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service("objectStorage")
public class MinIOStorageService implements StorageService {

    private final MinioClient minioClient;
    private final String bucket;

    public MinIOStorageService(
            @Value("${minio.url}") String url,
            @Value("${minio.access-key}") String accessKey,
            @Value("${minio.secret-key}") String secretKey,
            @Value("${minio.bucket}") String bucket
    ) {
        this.minioClient = MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
        this.bucket = bucket;
    }

    @Override
    public void save(String packageName, String version, MultipartFile file) throws IOException {
        String objectName = packageName + "/" + version + "/" + file.getOriginalFilename();

        try (InputStream is = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .stream(is, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
        } catch (Exception e) {
            throw new IOException("Failed to upload to MinIO", e);
        }
    }

    @Override
    public byte[] load(String packageName, String version, String fileName) throws IOException {
        String objectName = packageName + "/" + version + "/" + fileName;

        try (InputStream is = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucket)
                        .object(objectName)
                        .build())) {
            return is.readAllBytes();
        } catch (Exception e) {
            throw new IOException("Failed to read from MinIO", e);
        }
    }
}