package com.dal.asdc.reconnect.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class FileController {

    @Value("${upload.images.directory}")
    private String uploadImagesDirectory;

    public FileController() {

    }

    @GetMapping("/uploads/images/{fileName:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String fileName) {
        try {
            // Load file as Resource
            Path filePath = Paths.get(uploadImagesDirectory).resolve(fileName);
//            MediaType mediaType = getMediaTypeForFileName(fileName);

            // Check if file exists
//            if (file.exists()) {
//                // Return ResponseEntity with file content
//                return ResponseEntity.ok()
//                        .contentType(mediaType) // Adjust MediaType based on file type
//                        .body(file);
//            } else {
//                // Return 404 Not Found if file does not exist
//                return ResponseEntity.notFound().build();
//            }

            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
                return ResponseEntity.ok()
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            // Handle exception, e.g., log error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Method to determine media type based on file extension
    private MediaType getMediaTypeForFileName(String fileName) {
        String[] parts = fileName.split("\\.");
        if (parts.length > 0) {
            String extension = parts[parts.length - 1].toLowerCase();
            switch (extension) {
                case "jpeg":
                case "jpg":
                    return MediaType.IMAGE_JPEG;
                case "png":
                    return MediaType.IMAGE_PNG;
                case "gif":
                    return MediaType.IMAGE_GIF;
                case "bmp":
                    return MediaType.parseMediaType("image/bmp");
                case "webp":
                    return MediaType.parseMediaType("image/webp");
                case "svg":
                    return MediaType.parseMediaType("image/svg+xml");
                default:
                    return MediaType.APPLICATION_OCTET_STREAM; // Default to binary data
            }
        }
        return MediaType.APPLICATION_OCTET_STREAM; // Default to binary data
    }
}
