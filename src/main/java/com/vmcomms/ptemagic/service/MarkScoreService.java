package com.vmcomms.ptemagic.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

/**
 * Service Interface for managing Exam.
 */
public interface MarkScoreService {

    void markScore(Long examId);
    
    void finishExamByMarking(Long examId);
    
    String processUploadToCloud(MultipartFile file, String type) throws IOException;
}
