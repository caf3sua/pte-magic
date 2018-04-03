package com.vmcomms.ptemagic.web.rest;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vmcomms.ptemagic.dto.FileDTO;
import com.vmcomms.ptemagic.service.MarkScoreService;
import com.vmcomms.ptemagic.web.rest.errors.BadRequestAlertException;

@RestController
@RequestMapping("/api/file")
public class FileController {

    private final Logger log = LoggerFactory.getLogger(FileController.class);

    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "D://etc//";
    
    @Autowired
    private MarkScoreService markScoreService;

    @PostMapping("/upload/{type}") // //new annotation since 4.3
    public FileDTO singleFileUpload(@RequestParam("file") MultipartFile file, @PathVariable String type) {

    	FileDTO fileDTO = new FileDTO();
    	
        if (file.isEmpty()) {
        	throw new BadRequestAlertException("A new question cannot already have an ID", "File", "file-not-found");
        }

        try {

            // Get the file and save it somewhere
//            byte[] bytes = file.getBytes();
//            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
//            Files.write(path, bytes);

        	String filename = markScoreService.processUploadToCloud(file, type);
        	
        	fileDTO.setFilename(filename);
        	
        	return fileDTO;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
