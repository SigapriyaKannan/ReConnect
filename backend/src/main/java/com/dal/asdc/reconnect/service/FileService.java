package com.dal.asdc.reconnect.service;

import com.dal.asdc.reconnect.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {

    @Autowired
    private ProfileService profileService;

    private final String uploadImagesDirectory = "uploads/images";
    private final String uploadResumesDirectory = "uploads/resumes";

    public void uploadResume(int userId, MultipartFile file) throws IOException {
        Path directory = Paths.get(uploadResumesDirectory);
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path fileNameAndPath = Paths.get(uploadResumesDirectory, fileName);
        Files.write(fileNameAndPath, file.getBytes());

        profileService.updateResumePath(userId, fileNameAndPath.toString());
    }

    public void uploadProfilePicture(int userId, MultipartFile file) throws IOException {
        Path directory = Paths.get(uploadImagesDirectory);
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path fileNameAndPath = Paths.get(uploadImagesDirectory, fileName);
        Files.write(fileNameAndPath, file.getBytes());

        profileService.updateProfilePicturePath(userId, fileNameAndPath.toString());
    }

    public byte[] getResume(int userId) throws IOException {
        String resumePath = profileService.getResumePath(userId);
        if (resumePath != null) {
            return Files.readAllBytes(Paths.get(resumePath));
        }
        return null;
    }

    public byte[] getProfilePicture(int userId) throws IOException {
        String profilePicturePath = profileService.getProfilePicturePath(userId);
        if (profilePicturePath != null) {
            return Files.readAllBytes(Paths.get(profilePicturePath));
        }
        return null;
    }
}
