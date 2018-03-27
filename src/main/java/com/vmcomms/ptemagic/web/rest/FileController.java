package com.vmcomms.ptemagic.web.rest;

import com.vmcomms.ptemagic.config.Constants;
import com.codahale.metrics.annotation.Timed;
import com.vmcomms.ptemagic.domain.User;
import com.vmcomms.ptemagic.repository.UserRepository;
import com.vmcomms.ptemagic.security.AuthoritiesConstants;
import com.vmcomms.ptemagic.service.MailService;
import com.vmcomms.ptemagic.service.UserService;
import com.vmcomms.ptemagic.service.dto.UserDTO;
import com.vmcomms.ptemagic.web.rest.errors.BadRequestAlertException;
import com.vmcomms.ptemagic.web.rest.errors.EmailAlreadyUsedException;
import com.vmcomms.ptemagic.web.rest.errors.LoginAlreadyUsedException;
import com.vmcomms.ptemagic.web.rest.vm.ManagedUserVM;
import com.vmcomms.ptemagic.web.rest.util.HeaderUtil;
import com.vmcomms.ptemagic.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/file")
public class FileController {

    private final Logger log = LoggerFactory.getLogger(FileController.class);

    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "D://etc//";

    @PostMapping("/upload") // //new annotation since 4.3
    public String singleFileUpload(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
//            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
//            return "redirect:uploadStatus";
        }

        try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

//            redirectAttributes.addFlashAttribute("message",
//                    "You successfully uploaded '" + file.getOriginalFilename() + "'");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "OK";
    }
}
