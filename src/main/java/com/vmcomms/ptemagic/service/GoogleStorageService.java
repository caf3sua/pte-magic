package com.vmcomms.ptemagic.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

/**
 * Service Interface for managing Exam.
 */
public interface GoogleStorageService {

    String upload(MultipartFile file, String type) throws IOException;
    
    void removeUnusedMedia(String bucketName) throws IOException;
}
